package service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import domain.Events;

public class ScheduleManagerSvcSQLiteImpl extends SQLiteOpenHelper implements IScheduleManagerSQLite
{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "events.db";
    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_ID = "id";
    public static final String COLUMNONE_DATE = "DATE";
    public static final String COLUMNTWO_TIME = "TIME";
    public static final String COLUMNTHREE_DESCRIPTION = "DESCRIPTION";

    private List<Events> eventsList = new ArrayList<>();
    private Events events;

    //------------------------------------------------------------------------------------------------------
    public ScheduleManagerSvcSQLiteImpl(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMNONE_DATE + " TEXT,"
                + COLUMNTWO_TIME + " TEXT, "
                + COLUMNTHREE_DESCRIPTION + " TEXT " + ")";
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_EVENTS);
        onCreate(db);
    }

    //------------------------------------------------------------------------------------------------------
    // get the list of events from MainActivity and pick the events from the list

    //this method sends a list of events to be insterted into DB. Use this in MainActivity
    public void setEventList (List<Events> eventsList)
    {
        this.eventsList = eventsList;
        insertEvent();
    }

    //------------------------------------------------------------------------------------------------------
    // Add , Delete and Convert to String

    public void addEvent (Events events)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMNONE_DATE, events.getDate());
        values.put(COLUMNTWO_TIME, events.getTime());
        values.put(COLUMNTHREE_DESCRIPTION, events.getDescription());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    //This method inserts events to the table. Use this method in MainActivity
    public void insertEvent()
    {
        for(int i = 0; i < eventsList.size(); i++)
        {
            Events evemt = eventsList.get(i);
            addEvent(evemt);
        }
    }

    public void deleteEvent(int position)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EVENTS + " WHERE " + COLUMN_ID + " = " + position + " ;");
        db.close();
    }

    public List<Events> retrieveEvents()
    {
        List<Events> sendList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 1; i <= eventsList.size(); i++)
        {
            Events events = new Events();
            String query = "Select * FROM " + TABLE_EVENTS +
                    " WHERE " + COLUMN_ID + " = " + i + " ;";

            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                events.setDate(cursor.getString(0));
                events.setTime(cursor.getString(1));
                events.setDescription(cursor.getString(2));
                cursor.close();
                sendList.add(events);
                return sendList;
            }
            else {
                return null;
            }
        }
        return null;
    }
}
