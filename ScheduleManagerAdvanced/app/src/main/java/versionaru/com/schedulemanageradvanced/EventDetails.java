package versionaru.com.schedulemanageradvanced;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import domain.Events;

public class EventDetails extends AppCompatActivity implements View.OnClickListener {

    private List<Events> slist = new ArrayList<>();
    private Events sm = new Events();

    private TextView tv_ed_displayDate, tv_ed_displayTime, tv_ed_displayDescription;
    private TextView tv_ed_date, tv_ed_time, tv_ed_description;
    private Button b_ed_editEvent, b_ed_deleteEvents, b_ed_share;

    private RelativeLayout rl_event_details;

    private String fileName = "schedules.txt";
    private int beforeDelete, afterDelete;
    private int position;
    private int beforeAdding, afterAdding;

    private Button b_ed_editDate, b_ed_editTime;
    private Calendar calendarTime = Calendar.getInstance();
    private Calendar calendarDate = Calendar.getInstance();
    private String date, time, description;
    private Events sm1 = new Events();
    private boolean newEvent = false;

    private static int themeTag;
    private String shareEvent;

    //--------------------------------------------------------------------------------------------------------
    // OnCreate and Listners -

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSchedule();

        try
        {
            tv_ed_date = (TextView)findViewById(R.id.tv_ed_date);
            tv_ed_time = (TextView)findViewById(R.id.tv_ed_time);
            tv_ed_description = (TextView)findViewById(R.id.tv_ed_description);

            tv_ed_displayDate = (TextView)findViewById(R.id.tv_ed_displayDate);
            tv_ed_displayDate.setText(sm.getDate());

            tv_ed_displayTime = (TextView)findViewById(R.id.tv_ed_displayTime);
            tv_ed_displayTime.setText(sm.getTime());

            tv_ed_displayDescription = (TextView)findViewById(R.id.tv_ed_displayDescription);
            tv_ed_displayDescription.setText(sm.getDescription());

            b_ed_deleteEvents = (Button)findViewById(R.id.b_ed_deleteEvent);
            b_ed_deleteEvents.setOnClickListener(this);

            b_ed_editEvent = (Button)findViewById(R.id.b_ed_editEvent);
            b_ed_editEvent.setOnClickListener(this);

            b_ed_editDate = (Button)findViewById(R.id.b_ed_editDate);
            b_ed_editDate.setOnClickListener(this);

            b_ed_editTime = (Button)findViewById(R.id.b_ed_editTime);
            b_ed_editTime.setOnClickListener(this);

            b_ed_share = (Button)findViewById(R.id.b_ed_share);
            b_ed_share.setOnClickListener(this);

            rl_event_details = (RelativeLayout)findViewById(R.id.rl_event_details);

            tv_ed_date.setTextColor(Color.BLACK);
            tv_ed_time.setTextColor(Color.BLACK);
            b_ed_deleteEvents.setTextColor(Color.WHITE);
            tv_ed_description.setTextColor(Color.BLACK);
            b_ed_editEvent.setTextColor(Color.WHITE);
            tv_ed_displayTime.setTextColor(Color.BLACK);
            tv_ed_displayDate.setTextColor(Color.BLACK);
            tv_ed_displayDescription.setTextColor(Color.BLACK);
            b_ed_editDate.setTextColor(Color.BLACK);
            b_ed_editTime.setTextColor(Color.BLACK);
            b_ed_share.setTextColor(Color.WHITE);

    }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }

        shareEvent = "Date: " + sm.getDate() + "\r\n " + "Time: " + sm.getTime() + "\r\n " + "Details: " + sm.getDescription();
        setTheme();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.b_ed_deleteEvent:
                deleteSchedule(this.sm);
                toasterDelete();
                finish();
            case R.id.b_ed_editDate:
                setDate();
                break;
            case R.id.b_ed_editTime:
                setTime();
                break;
            case R.id.b_ed_editEvent:
                updateDescription();
                setSchedule(date, time, description);
                toasterUpdate();
                break;
            case R.id.b_ed_share:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_SUBJECT, "My Schedule");
                share.putExtra(Intent.EXTRA_TEXT, shareEvent);
                startActivity(Intent.createChooser(share, "Share at"));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
    //--------------------------------------------------------------------------------------------------------
    // Additional helper methods

    public void readFile()
    {
        FileInputStream fis = null;
        try {
            fis = openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            slist = (ArrayList<Events>)is.readObject();
            is.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void writeFile()
    {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(slist);
            os.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    public void toasterDelete()
    {
        Toast.makeText(getApplicationContext(), "Events deleted",
                Toast.LENGTH_LONG).show();
    }

    public void toasterUpdate() {
        Toast.makeText(getApplicationContext(), "Events updated",
                Toast.LENGTH_LONG).show();
    }

    public void getSchedule()
    {
        position = getIntent().getIntExtra("position", 0);
        readFile();
        sm = slist.get(position);
    }

    public String getExistingDate()
    {
        position = getIntent().getIntExtra("position", 0);
        readFile();
        sm = slist.get(position);
        return sm.getDate();
    }

    public String getExistingTime()
    {
        position = getIntent().getIntExtra("position", 0);
        readFile();
        sm = slist.get(position);
        return sm.getTime();
    }

    public String getExistingDescription()
    {
        position = getIntent().getIntExtra("position", 0);
        readFile();
        sm = slist.get(position);
        return sm.getDescription();
    }

    private void deleteSchedule(Events smd)
    {
        beforeDelete = slist.size();
        slist.remove(smd);
        afterDelete = slist.size();
        writeFile();
    }
    //--------------------------------------------------------------------------------------------------------
    // setting Deatils

    public void setThemeTag(int themeTag)
    {
        Log.e("Inside setThemeTag", String.valueOf(themeTag));
        this.themeTag = themeTag;
        Log.e("Inside setThemeTag", String.valueOf(this.themeTag));
    }
    private void setTheme()
    {
        Log.e("inside setTheme", String.valueOf(this.themeTag));
        switch (this.themeTag)
        {
            case 0:
                b_ed_share.setBackgroundResource(R.drawable.button_grey);
                b_ed_deleteEvents.setBackgroundResource(R.drawable.button_grey);
                b_ed_editEvent.setBackgroundResource(R.drawable.button_grey);
                tv_ed_displayDate.setBackgroundResource(R.color.theme_one_main_activity_background_layout_color);
                tv_ed_displayTime.setBackgroundResource(R.color.theme_one_main_activity_background_layout_color);
                tv_ed_displayDescription.setBackgroundResource(R.color.theme_one_main_activity_background_layout_color);
                tv_ed_date.setBackgroundResource(R.color.theme_one_main_activity_background_layout_color);
                tv_ed_time.setBackgroundResource(R.color.theme_one_main_activity_background_layout_color);
                tv_ed_description.setBackgroundResource(R.color.theme_one_main_activity_background_layout_color);
                rl_event_details.setBackgroundColor(ContextCompat.getColor(EventDetails.this,
                        R.color.theme_one_main_activity_background_layout_color));
                break;

            case 4:
                b_ed_share.setBackgroundResource(R.drawable.button_pink);
                b_ed_deleteEvents.setBackgroundResource(R.drawable.button_pink);
                b_ed_editEvent.setBackgroundResource(R.drawable.button_pink);
                tv_ed_displayDate.setBackgroundResource(R.color.theme_two_main_activity_background_layout_color);
                tv_ed_displayTime.setBackgroundResource(R.color.theme_two_main_activity_background_layout_color);
                tv_ed_displayDescription.setBackgroundResource(R.color.theme_two_main_activity_background_layout_color);
                tv_ed_time.setBackgroundResource(R.color.theme_two_main_activity_background_layout_color);
                tv_ed_date.setBackgroundResource(R.color.theme_two_main_activity_background_layout_color);
                tv_ed_description.setBackgroundResource(R.color.theme_two_main_activity_background_layout_color);
                rl_event_details.setBackgroundColor(ContextCompat.getColor(EventDetails.this,
                        R.color.theme_two_main_activity_background_layout_color));
                break;

            case 1:
                b_ed_share.setBackgroundResource(R.drawable.button_blue);
                b_ed_deleteEvents.setBackgroundResource(R.drawable.button_blue);
                b_ed_editEvent.setBackgroundResource(R.drawable.button_blue);
                tv_ed_displayDate.setBackgroundResource(R.color.theme_three_main_activity_background_layout_color);
                tv_ed_displayTime.setBackgroundResource(R.color.theme_three_main_activity_background_layout_color);
                tv_ed_displayDescription.setBackgroundResource(R.color.theme_three_main_activity_background_layout_color);
                tv_ed_date.setBackgroundResource(R.color.theme_three_main_activity_background_layout_color);
                tv_ed_time.setBackgroundResource(R.color.theme_three_main_activity_background_layout_color);
                tv_ed_description.setBackgroundResource(R.color.theme_three_main_activity_background_layout_color);
                rl_event_details.setBackgroundColor(ContextCompat.getColor(EventDetails.this,
                        R.color.theme_three_main_activity_background_layout_color));
                break;

            case 2:
                b_ed_share.setBackgroundResource(R.drawable.button_green);
                b_ed_deleteEvents.setBackgroundResource(R.drawable.button_green);
                b_ed_editEvent.setBackgroundResource(R.drawable.button_green);
                tv_ed_displayDate.setBackgroundResource(R.color.theme_four_main_activity_background_layout_color);
                tv_ed_displayTime.setBackgroundResource(R.color.theme_four_main_activity_background_layout_color);
                tv_ed_displayDescription.setBackgroundResource(R.color.theme_four_main_activity_background_layout_color);
                tv_ed_date.setBackgroundResource(R.color.theme_four_main_activity_background_layout_color);
                tv_ed_time.setBackgroundResource(R.color.theme_four_main_activity_background_layout_color);
                tv_ed_description.setBackgroundResource(R.color.theme_four_main_activity_background_layout_color);
                rl_event_details.setBackgroundColor(ContextCompat.getColor(EventDetails.this,
                        R.color.theme_four_main_activity_background_layout_color));
                break;

            case 3:
                b_ed_share.setBackgroundResource(R.drawable.button_red);
                b_ed_deleteEvents.setBackgroundResource(R.drawable.button_red);
                b_ed_editEvent.setBackgroundResource(R.drawable.button_red);
                tv_ed_displayDate.setBackgroundResource(R.color.theme_five_main_activity_background_layout_color);
                tv_ed_displayTime.setBackgroundResource(R.color.theme_five_main_activity_background_layout_color);
                tv_ed_displayDescription.setBackgroundResource(R.color.theme_five_main_activity_background_layout_color);
                tv_ed_date.setBackgroundResource(R.color.theme_five_main_activity_background_layout_color);
                tv_ed_time.setBackgroundResource(R.color.theme_five_main_activity_background_layout_color);
                tv_ed_description.setBackgroundResource(R.color.theme_five_main_activity_background_layout_color);
                rl_event_details.setBackgroundColor(ContextCompat.getColor(EventDetails.this,
                        R.color.theme_five_main_activity_background_layout_color));
                break;
        }
    }

    public void setSchedule(String date, String time, String description)
    {
        beforeAdding = slist.size();
        if (date != null && time != null && description != null)
        {
            beforeAdding = slist.size();
            sm1.setDate(date);
            sm1.setTime(time);
            sm1.setDescription(description);
            storeSchedule(sm1);
            if(slist.contains(sm)) beforeAdding = afterAdding+1;
            newEvent = true;
            afterAdding = slist.size();
        }
        else if (date == null && time == null && description == null)
        {
            beforeAdding = slist.size();
            sm1.setDate(getExistingDate());
            sm1.setTime(getExistingTime());
            sm1.setDescription(getExistingDescription());
            storeSchedule(sm1);
            if(slist.contains(sm)) beforeAdding = afterAdding+1;
            newEvent = false;
            afterAdding = slist.size();
        }
        else if  (date == null && time != null && description != null)
        {
            beforeAdding = slist.size();
            sm1.setDate(getExistingDate());
            sm1.setTime(time);
            sm1.setDescription(description);
            storeSchedule(sm1);
            if(slist.contains(sm)) beforeAdding = afterAdding+1;
            newEvent = true;
            afterAdding = slist.size();
        }
        else if  (date != null && time == null && description != null)
        {
            beforeAdding = slist.size();
            sm1.setDate(date);
            sm1.setTime(getExistingTime());
            sm1.setDescription(description);
            storeSchedule(sm1);
            if(slist.contains(sm)) beforeAdding = afterAdding+1;
            newEvent = true;
            afterAdding = slist.size();
        }
        else if  (date != null && time != null && description == null)
        {
            beforeAdding = slist.size();
            sm1.setDate(date);
            sm1.setTime(time);
            sm1.setDescription(getExistingDescription());
            storeSchedule(sm1);
            if(slist.contains(sm)) beforeAdding = afterAdding+1;
            newEvent = true;
            afterAdding = slist.size();
        }
        else if  (date == null && time == null && description != null)
        {
            beforeAdding = slist.size();
            sm1.setDate(getExistingDate());
            sm1.setTime(getExistingTime());
            sm1.setDescription(description);
            storeSchedule(sm1);
            if(slist.contains(sm)) beforeAdding = afterAdding+1;
            newEvent = true;
            afterAdding = slist.size();
        }
        else if  (date == null && time != null && description == null)
        {
            beforeAdding = slist.size();
            sm1.setDate(getExistingDate());
            sm1.setTime(time);
            sm1.setDescription(getExistingDescription());
            storeSchedule(sm1);
            if(slist.contains(sm)) beforeAdding = afterAdding+1;
            newEvent = true;
            afterAdding = slist.size();
        }
        else if  (date != null && time == null && description == null)
        {
            beforeAdding = slist.size();
            sm1.setTime(getExistingTime());
            sm1.setDescription(getExistingDescription());
            storeSchedule(sm1);
            if(slist.contains(sm)) beforeAdding = afterAdding+1;
            newEvent = true;
            afterAdding = slist.size();
        }

    }


    public void storeSchedule (Events sm)
    {
        readFile();
        slist.remove(position);
        slist.add(sm1);
        writeFile();
        date = null;
        time = null;
        description = null;
    }

    //--------------------------------------------------------------------------------------------------------
    // DatePickerDialog and its supporting methods


    public void setDate()
    {
        new DatePickerDialog(this, d, calendarDate.get(Calendar.YEAR),
                calendarDate.get(Calendar.MONTH),  calendarDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendarDate.set(Calendar.YEAR, year);
            calendarDate.set(Calendar.MONTH, monthOfYear);
            calendarDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        }
    };

    private int dateFlag = 0;
    // dateFlag = 1 if today, dateFlag = 0 if tomorrow, dateFlag = -1 if yesterday

    public void updateDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy");
        String show = "Invalid entry";
        Date today = new Date();

        String d1, d2;
        Calendar c1 = Calendar.getInstance();
        d1 = sdf.format(c1.getTime());
        d2 = sdf.format(calendarDate.getTime());

        if(d1.equals(d2))
        {
            date = sdf.format(calendarDate.getTime());
            dateFlag = 1;
            show = date;
            tv_ed_displayDate.setText(show);
        }
        else if (calendarDate.getTime().after(today))
        {
            date = sdf.format(calendarDate.getTime());
            show = date;
            dateFlag = 0;
            tv_ed_displayDate.setText(show);
        }
        else if (calendarDate.getTime().before(today))
        {
            dateFlag = -1;
            date = null;
            tv_ed_displayDate.setText(show);
        }
        else
        {
            date = null;
        }
    }
    //------------------------------------------------------------------------------------------------
    // Description

    public void updateDescription()
    {
        description = tv_ed_description.getText().toString();
    }

    //--------------------------------------------------------------------------------------------------------
    // TimePickerDialog and their helper methods -

    public void setTime()
    {
        new TimePickerDialog(this, t, calendarTime.get(Calendar.HOUR_OF_DAY),
                calendarTime.get(Calendar.MINUTE), false).show();
    }

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendarTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendarTime.set(Calendar.MINUTE, minute);
            updateTime();
        }
    };

    private int timeFlag = 0;
    // timeFlag = 1 if today, timeFlag = 0 if tomorrow, timeFlag = -1 if yesterday

    public void updateTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        Calendar calendarCompare = Calendar.getInstance();
        String show = "Unable to travel back in time";
        time = null;

        String t1, t2;
        Calendar c1 = Calendar.getInstance();
        t1 = sdf.format(c1.getTime());
        t2 = sdf.format(calendarTime.getTime());

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Today and before now and after

        if(dateFlag == 1 && calendarTime.compareTo(calendarCompare) == 1)
        {
            time = sdf.format(calendarTime.getTime());
            show = time;
            tv_ed_displayTime.setText(show);
        }
        else if(dateFlag == 1 && t1.equals(t2))
        {
            show = "Invalid entry";
            time = null;
            tv_ed_displayTime.setText(show);
        }

        else if(dateFlag == 1 && calendarTime.compareTo(calendarCompare) == -1)
        {
            show = "Invalid entry";
            time = null;
            tv_ed_displayTime.setText(show);
        }
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Tomorrow and before now and after

        else if(dateFlag == 0)
        {
            time = sdf.format(calendarTime.getTime());
            show = time;
            tv_ed_displayTime.setText(show);
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Yesterday and before now and after
        else if (dateFlag == -1 )
        {
            show = "Invalid entry";
            time = null;
            tv_ed_displayTime.setText(show);
        }
    }
}
