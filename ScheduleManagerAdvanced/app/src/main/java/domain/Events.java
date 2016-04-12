package domain;

import java.io.Serializable;
import java.util.Calendar;

public class Events implements Serializable
{
    private String date;
    private String time;
    private String description;
    private Calendar calendarDate;
    private Calendar calendarTime;

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getDate()
    {
        return date;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public String getTime()
    {
        return time;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Events)) {
            return false;
        }

        Events that = (Events) other;

        // Custom equality check here.
        return this.date.equals(that.date)
                && this.time.equals(that.time) && (!(this.description.equals(that.description)) || (this.description.equals(that.description)));
    }
}
