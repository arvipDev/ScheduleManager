package service;

import java.util.List;

import domain.Events;

public interface IScheduleManagerSQLite {

    public void setEventList(List<Events> eventsList);
    public void deleteEvent(int position);
    public List<Events> retrieveEvents();
}
