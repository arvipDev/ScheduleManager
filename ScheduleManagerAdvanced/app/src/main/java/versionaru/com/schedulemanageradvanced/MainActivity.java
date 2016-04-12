package versionaru.com.schedulemanageradvanced;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import domain.Events;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private Button button_addschedule;
    private Calendar calendar = Calendar.getInstance();
    private ListView listview;
    private List<Events> schedulelist = new ArrayList<>();
    private String fileName = "schedules.txt";

    private EventDetails ed = new EventDetails();

    //private static String TAG = MainActivity.class.getSimpleName();

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private DrawerLayout mDrawerLayout;
    ArrayList<NavItem> mNavItems = new ArrayList<>();

    private RelativeLayout rl_main, rl_profileBox;
    private EventDetails newEvent = new EventDetails();
    private int themes = 0;

    //--------------------------------------------------------------------------------------------------------
    // OnCreate and Listners -

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_addschedule = (Button)findViewById(R.id.b_addschedule);
        button_addschedule.setOnClickListener(this);

        listview = (ListView)findViewById(R.id.lv_listoptions);
        rl_main = (RelativeLayout)findViewById(R.id.mainContent);
        rl_profileBox = (RelativeLayout)findViewById(R.id.profileBox);
        mDrawerList = (ListView) findViewById(R.id.navList);

        populateList();
        populateListView();

        mNavItems.add(new NavItem("Home", "List of events", R.drawable.ic_action_home));
        mNavItems.add(new NavItem("Themes", "Change your theme", R.drawable.ic_themes));
        mNavItems.add(new NavItem("About", "Get to know about the application", R.drawable.ic_action_about));


        selectTheme(themes);

        drawerLayout();
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        populateList();
        populateListView();
    }

    @Override
    //when button is clicked
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.b_addschedule:
                Intent addIntent = new Intent("android.intent.action.ADDSCHEDULE");
                AddEvent ae = new AddEvent();
                ae.setThemeTag(themes);
                Bundle bundle = new Bundle();
                startActivity(addIntent);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
        }
        return true;
    }

    //--------------------------------------------------------------------------------------------------------
    //Themes

    public void selectTheme(int i)
    {

        if(themes == 0)
        {
            rl_main.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    R.color.theme_one_main_activity_background_layout_color));
            rl_profileBox.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    R.color.theme_one_main_activity_drawer_layout_color));
            listview.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    R.color.theme_one_main_activity_listview_color));
            button_addschedule.setBackgroundResource(R.drawable.button_grey);
            mDrawerList.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    R.color.theme_one_main_activity_drawer_navlist_color));

        }
        else if (themes == 4)
        {
            rl_main.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    R.color.theme_two_main_activity_background_layout_color));
            rl_profileBox.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    R.color.theme_two_main_activity_drawer_layout_color));
            listview.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    R.color.theme_two_main_activity_listview_color));
            button_addschedule.setBackgroundResource(R.drawable.button_pink);
            mDrawerList.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    R.color.theme_two_main_activity_drawer_navlist_color));

        }

        else if (themes == 1)
        {
            rl_main.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    R.color.theme_three_main_activity_background_layout_color));
            rl_profileBox.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    R.color.theme_three_main_activity_drawer_layout_color));
            listview.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    R.color.theme_three_main_activity_listview_color));
            button_addschedule.setBackgroundResource(R.drawable.button_blue);
            mDrawerList.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    R.color.theme_three_main_activity_drawer_navlist_color));

        }
        else if (themes == 2)
        {
            rl_main.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    R.color.theme_four_main_activity_background_layout_color));
            rl_profileBox.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    R.color.theme_four_main_activity_drawer_layout_color));
            listview.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    R.color.theme_four_main_activity_listview_color));
            button_addschedule.setBackgroundResource(R.drawable.button_green);
            mDrawerList.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    R.color.theme_four_main_activity_drawer_navlist_color));

        }
        else if (themes == 3)
        {
            rl_main.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    R.color.theme_five_main_activity_background_layout_color));
            rl_profileBox.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    R.color.theme_five_main_activity_drawer_layout_color));
            listview.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    R.color.theme_five_main_activity_listview_color));
            button_addschedule.setBackgroundResource(R.drawable.button_red);
            mDrawerList.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    R.color.theme_five_main_activity_drawer_navlist_color));

        }
    }


    //--------------------------------------------------------------------------------------------------------
    //

    public void drawerLayout()
    {
        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //selectItemFromDrawer(position);
                switch(position)
                {
                    case 0:
                        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                        mDrawerLayout.closeDrawers();
                        break;

                    case 2:
                        AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(MainActivity.this);

                        // set title
                        alertDialogBuilder1.setTitle("About");

                        String line1 = "Application name - Schedule Manager Advanced";
                        String line2 = "Approximate total size - 3 MB";
                        String line3 = "Application uses device's internal memory";
                        String line4 = "Version 2.0";
                        String line5 = "Developed by - Arvind Purushotham";


                        // set dialog message
                        alertDialogBuilder1
                                .setMessage(line1 +"\n"+ line2 +"\n"+ line3 +"\n"+ line4 +"\n"+ line5)
                                .setCancelable(false)
                                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, close
                                        // the dialog
                                        dialog.cancel();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog1 = alertDialogBuilder1.create();

                        // show it
                        alertDialog1.show();
                        break;

                    case 1:
                        //creating a dialog
                        AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(MainActivity.this);
                        final CharSequence[] items = {" Grey "," Ocean "," Jungle "," Volcano " , " Pink Panther "};

                        // set title
                        alertDialogBuilder2.setTitle("Themes");
                        alertDialogBuilder2.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int item) {

                                switch (item) {
                                    case 0:
                                        themes = 0;
                                        break;
                                    case 1:
                                        themes = 1;
                                        break;
                                    case 2:
                                        themes = 2;
                                        break;
                                    case 3:
                                        themes = 3;
                                        break;
                                    case 4:
                                        themes = 4;
                                        break;
                                    default:
                                        themes = 0;
                                        break;
                                }
                            }
                        }).setCancelable(false)
                            .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    selectTheme(themes);
                                    dialog.cancel();
                                }
                            });

                        // create alert dialog
                        AlertDialog alertDialog2 = alertDialogBuilder2.create();

                        // show it
                        alertDialog2.show();
                        break;
                }
            }
        });
    }

    class NavItem {
        String mTitle;
        String mSubtitle;
        int mIcon;

        public NavItem(String title, String subtitle, int icon) {
            mTitle = title;
            mSubtitle = subtitle;
            mIcon = icon;
        }
    }

    class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mNavItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.drawer_item, null);
            }
            else {
                view = convertView;
            }

            TextView titleView = (TextView) view.findViewById(R.id.drawer_title);
            TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
            ImageView iconView = (ImageView) view.findViewById(R.id.icon);

            titleView.setText( mNavItems.get(position).mTitle );
            subtitleView.setText( mNavItems.get(position).mSubtitle );
            iconView.setImageResource(mNavItems.get(position).mIcon);

            return view;
        }
    }

    private class MyListAdapter extends ArrayAdapter<Events>
    {
        public MyListAdapter()
        {
            super(MainActivity.this, R.layout.item_view, schedulelist);
        }


        public View getView(int position, View convertView, ViewGroup parent )
        {
            sortListDate();
            //make sure we have a view to work with
            View itemView = convertView;
            if(itemView == null)
            {
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }

            //Update the list
            populateList();

            //find the schedule from the list to work with
            Events currentschedule = schedulelist.get(position);

            //fill the ListView

            TextView datetext = (TextView)itemView.findViewById(R.id.tv_date);
            TextView timetext = (TextView)itemView.findViewById(R.id.tv_time);
            TextView descriptiontext = (TextView)itemView.findViewById(R.id.tv_description);

            try
            {
                datetext.setText("" + currentschedule.getDate().toString());
                timetext.setText("" + currentschedule.getTime());
                descriptiontext.setText("" + currentschedule.getDescription());
            }
            catch(NullPointerException e)
            {
                e.printStackTrace();
            }
            return itemView;
        }
    }

    //--------------------------------------------------------------------------------------------------------
    // Sorting the list - schedulelist

    // Helper to sortlist() - converts String date to Date and sets date to calendar
    public Calendar stringToDate(Events e1)
    {
        Calendar cal = Calendar.getInstance();
        try {
            DateFormat format = new SimpleDateFormat("MMM/dd/yyyy", Locale.ENGLISH);
            Date date = null;
            date = format.parse(e1.getDate());
            cal.setTime(date);
            return cal;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

// Sorts the list with the most recent event at the top
    public void sortListDate()
    {
        //Sorting
        Collections.sort(schedulelist, new Comparator<Events>() {
            @Override
            public int compare(Events e1, Events e2)
            {
                Calendar c1 = stringToDate(e1);
                Calendar c2 = stringToDate(e2);
                return c1.compareTo(c2);
            }
        });
        writeFile();
    }

    //--------------------------------------------------------------------------------------------------------
    //

    public void readFile() {
        FileInputStream fis = null;
        try {
            fis = openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            schedulelist = (ArrayList<Events>)is.readObject();
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

        //SQLite
        //ScheduleManagerSvcSQLiteImpl sqlite = new ScheduleManagerSvcSQLiteImpl(this, null, null, 1);
        //schedulelist = sqlite.retrieveEvents();
    }

    public void writeFile()
    {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(schedulelist);
            os.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //ScheduleManagerSvcSQLiteImpl sqlite = new ScheduleManagerSvcSQLiteImpl(this, null, null, 1);
        //sqlite.setEventList(schedulelist);
    }

    //populates the list by reading from file and sorting it.
    public void populateList()
    {
        schedulelist = retrieveSchedule();
        sortListDate();
    }

    public void populateListView()
    {
        readFile();
        ArrayAdapter<Events> adapter = new MyListAdapter();
        listview = (ListView)findViewById(R.id.lv_listoptions);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(onListClick);
    }

    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent i = new Intent("android.intent.action.EVENTDETAILS");
            i.putExtra("position", position);
            EventDetails ed = new EventDetails();
            ed.setThemeTag(themes);
            startActivity(i);
        }
    };

    // gets list for the file
    public List<Events> retrieveSchedule()
    {
        readFile();
        return schedulelist;
    }

    //--------------------------------------------------------------------------------------------------------
    //Themes


}
