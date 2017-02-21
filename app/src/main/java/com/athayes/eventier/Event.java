package com.athayes.eventier;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by anthonyhayes on 05/11/2016.
 */

public class Event implements Comparable<Event> {

    public final String id;
    public final String title;
    public final String pitch;
    public final String host;
    public final String location;
    public final String startTime;
    public final String endTime;

    public Event(String id, String title, String pitch, String host, String location, String startTime, String endTime) {
        this.id = id;
        this.title = title;
        this.pitch = pitch;
        this.host = host;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getHost() {
        return host;
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public String getPitch() {
        return pitch;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    @Override
    public int compareTo(Event o) {
        Calendar thisCalendar = Calendar.getInstance();
        Calendar compareCalendar = Calendar.getInstance();

        String myFormat = "dd/MM/yy";
        SimpleDateFormat databaseFormat = new SimpleDateFormat(myFormat);

        try {
            compareCalendar.setTime(databaseFormat.parse(o.getStartTime()));
        } catch (Exception ex) {
        }

        try {
            thisCalendar.setTime(databaseFormat.parse(getStartTime()));
        } catch (Exception ex) {
        }
        return thisCalendar.compareTo(compareCalendar);
    }
}
