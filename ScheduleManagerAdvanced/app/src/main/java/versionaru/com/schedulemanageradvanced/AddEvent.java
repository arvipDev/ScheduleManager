package versionaru.com.schedulemanageradvanced;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import domain.Events;

public class AddEvent extends AppCompatActivity implements View.OnClickListener, Serializable{

    // Variables

    private Button b_pickDate;
    private Button b_pickTime;
    private Button b_addSchedule;
    private EditText et_description;
    private RelativeLayout rl_add_event;

    private Calendar calendarTime = Calendar.getInstance();
    private Calendar calendarDate = Calendar.getInstance();

    private Events events = new Events();;
    private List<Events> scheduleList = new ArrayList<>();

    private String date = null, time = null, description = null;
    private String onButtonText1 = "Pick events date", onButtonText2 = "Pick events time";
    private String fileName = "schedules.txt";
    private int beforeAdding, afterAdding;

    // This is a handle so that we can call methods on our service
    private int dateFlag = 0;
    private boolean available = true;

    private static int themeTag;

    //--------------------------------------------------------------------------------------------------------
    // OnCreate and Listners -
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        b_pickDate = (Button)findViewById(R.id.b_adddate);
        b_pickDate.setOnClickListener(this);
        b_pickDate.setText(onButtonText1);

        b_pickTime = (Button)findViewById(R.id.b_addtime);
        b_pickTime.setOnClickListener(this);
        b_pickTime.setText(onButtonText2);

        et_description = (EditText)findViewById(R.id.et_description);
        et_description.setGravity(Gravity.CENTER);

        b_addSchedule = (Button)findViewById(R.id.b_finish);
        b_addSchedule.setOnClickListener(this);

        b_pickDate.setTextColor(ContextCompat.getColor(AddEvent.this,
                R.color.theme_two_item_view_date_color));
        b_pickTime.setTextColor(ContextCompat.getColor(AddEvent.this,
                R.color.theme_two_item_view_time_color));
        et_description.setTextColor(ContextCompat.getColor(AddEvent.this,
                R.color.theme_two_item_view_description_color));
        b_addSchedule.setTextColor(ContextCompat.getColor(AddEvent.this,
                R.color.theme_two_item_view_description_color));

        rl_add_event = (RelativeLayout)findViewById(R.id.rl_add_event);

        setTheme();

    }


    @Override
    public void onClick(View v)
    {
        String d = null, t = null;
        switch(v.getId())
        {
            case R.id.b_adddate:
                setDate();
                d = date;
                break;
            case R.id.b_addtime:
                setTime();
                t = time;
                break;
            case R.id.b_finish:
                updateDescription();
                setSchedule(date, time, description);
                toaster();
                et_description.setText("");
                b_pickTime.setText(onButtonText2);
                b_pickDate.setText(onButtonText1);

                // et_description.setText(onButtonText3);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_schedule, menu);
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

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    //--------------------------------------------------------------------------------------------------------
    // Additional Methods (Helpers) -

    private void setTheme()
    {
        Log.e("inside setTheme", String.valueOf(this.themeTag));
        switch (this.themeTag)
        {
            case 0:
                b_pickDate.setBackgroundResource(R.drawable.button_grey);
                b_pickTime.setBackgroundResource(R.drawable.button_grey);
                b_addSchedule.setBackgroundResource(R.drawable.button_grey);
                et_description.setBackgroundColor(ContextCompat.getColor(AddEvent.this,
                        R.color.theme_one_main_activity_listview_color));
                rl_add_event.setBackgroundColor(ContextCompat.getColor(AddEvent.this,
                        R.color.theme_one_main_activity_background_layout_color));
                break;

            case 4:
                b_addSchedule.setBackgroundResource(R.drawable.button_pink);
                b_pickTime.setBackgroundResource(R.drawable.button_pink);
                b_pickDate.setBackgroundResource(R.drawable.button_pink);
                et_description.setBackgroundColor(ContextCompat.getColor(AddEvent.this,
                        R.color.theme_two_main_activity_listview_color));
                rl_add_event.setBackgroundColor(ContextCompat.getColor(AddEvent.this,
                        R.color.theme_two_main_activity_background_layout_color));
                break;

            case 1:
                b_pickDate.setBackgroundResource(R.drawable.button_blue);
                b_pickTime.setBackgroundResource(R.drawable.button_blue);
                b_addSchedule.setBackgroundResource(R.drawable.button_blue);
                et_description.setBackgroundColor(ContextCompat.getColor(AddEvent.this,
                        R.color.theme_three_main_activity_listview_color));
                rl_add_event.setBackgroundColor(ContextCompat.getColor(AddEvent.this,
                        R.color.theme_three_main_activity_background_layout_color));
                break;

            case 2:
                b_pickDate.setBackgroundResource(R.drawable.button_green);
                b_pickTime.setBackgroundResource(R.drawable.button_green);
                b_addSchedule.setBackgroundResource(R.drawable.button_green);
                et_description.setBackgroundColor(ContextCompat.getColor(AddEvent.this,
                        R.color.theme_four_main_activity_listview_color));
                rl_add_event.setBackgroundColor(ContextCompat.getColor(AddEvent.this,
                        R.color.theme_four_main_activity_background_layout_color));
                break;

            case 3:
                b_pickDate.setBackgroundResource(R.drawable.button_red);
                b_pickTime.setBackgroundResource(R.drawable.button_red);
                b_addSchedule.setBackgroundResource(R.drawable.button_red);
                et_description.setBackgroundColor(ContextCompat.getColor(AddEvent.this,
                        R.color.theme_five_main_activity_listview_color));
                rl_add_event.setBackgroundColor(ContextCompat.getColor(AddEvent.this,
                        R.color.theme_five_main_activity_background_layout_color));
                break;
        }
    }

    public void setThemeTag(int themeTag)
    {
        Log.e("Inside setThemeTag", String.valueOf(themeTag));
        this.themeTag = themeTag;
        Log.e("Inside setThemeTag", String.valueOf(this.themeTag));
    }

    public void toaster()
    {
        if(available == false)
        {
            Toast.makeText(getApplicationContext(), "Unavailable on this date and time",
                    Toast.LENGTH_LONG).show();
        }
        else if(beforeAdding+1 == afterAdding)
        {
            Toast.makeText(getApplicationContext(), "Events Added",
                    Toast.LENGTH_LONG).show();
        }

        else if (beforeAdding == afterAdding && events.getDescription() == null && events.getTime() != null && events.getDate() != null)
        {
            Toast.makeText(getApplicationContext(), "Description was not entered",
                    Toast.LENGTH_LONG).show();
        }
        else if (beforeAdding == afterAdding)
        {
            Toast.makeText(getApplicationContext(), "Unable to add this events",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void setSchedule(String date, String time, String description)
    {
        beforeAdding = scheduleList.size();
        events.setDate(date);
        events.setTime(time);
        events.setDescription(description);
        storeSchedule(events);
        afterAdding = scheduleList.size();
    }

    public void storeSchedule (Events sm)
    {
        readFile();
        if(date != null && time != null && description != null && scheduleList.contains(sm) == false)
        {
            scheduleList.add(sm);
            writeFile();
            date = null;
            time = null;
            description = null;
        }
        else
        {
            available = false;
            date = null;
            time = null;
            description = null;
        }
    }

    public void writeFile()
    {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(scheduleList);
            os.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    public void readFile() {
        FileInputStream fis = null;
        try {
            fis = openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            scheduleList = (ArrayList<Events>)is.readObject();
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

    //--------------------------------------------------------------------------------------------------------
    // DatePickerDialog and their helper methods -

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

    public void updateDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy");
        String show = "Unable to travel back in time";
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
            b_pickDate.setText(show);
        }
        else if (calendarDate.getTime().after(today))
        {
            date = sdf.format(calendarDate.getTime());
            show = date;
            dateFlag = 0;
            b_pickDate.setText(show);
        }
        else if (calendarDate.getTime().before(today))
        {
            dateFlag = -1;
            date = null;
            b_pickDate.setText(show);
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
        if(et_description.getText().toString().equals("Add description") == false)
        {
            description = et_description.getText().toString();
        }
        else
        {
            description = null;
        }
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
            b_pickTime.setText(show);
        }
        else if(dateFlag == 1 && t1.equals(t2))
        {
            show = "Are you already in an events??";
            time = null;
            b_pickTime.setText(show);
        }

        else if(dateFlag == 1 && calendarTime.compareTo(calendarCompare) == -1)
        {
            show = "Unable to travel back in time";
            time = null;
            b_pickTime.setText(show);
        }
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Tomorrow and before now and after

        else if(dateFlag == 0)
        {
            time = sdf.format(calendarTime.getTime());
            show = time;
            b_pickTime.setText(show);
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Yesterday and before now and after
        else if (dateFlag == -1 )
        {
            show = "Unable to travel back in time";
            time = null;
            b_pickTime.setText(show);
        }
    }

    //--------------------------------------------------------------------------------------------------------
}