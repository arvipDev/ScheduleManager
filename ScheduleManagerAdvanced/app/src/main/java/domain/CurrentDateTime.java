package domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CurrentDateTime
{
    private String  cdate, ctime;
    private Date dt;
    private Calendar cal = Calendar.getInstance();

    public CurrentDateTime()
    {
        this.cdate = null;
        this.ctime = null;
    }

    public String getCurrentDate()
    {
        DateFormat df = new SimpleDateFormat("dd/MM/yyy");
        cdate = df.format(dt);
        return cdate;
    }

    public String getCurrenTime()
    {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        ctime = df.format(dt);
        return ctime;
    }

    public boolean Validate()
    {
        if(cdate == null || cdate.equals(" ")) return false;
        if(ctime == null || ctime.equals(" ")) return false;
        return true;
    }
}
