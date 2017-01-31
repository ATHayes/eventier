package com.athayes.eventier;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by anthonyhayes on 05/11/2016.
 */

public class Event implements Comparable<Event> {

    public final String id;
    public final String title;

    public String getDate() {
        return date;
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

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public final String pitch;
    public final String host;
    public final String location;
    public final String time;

    // FireBase doesn't support the date class in Java - we'll store the date as a string
    public final String date;

    public Event(String id, String title, String pitch, String host, String location, String time, String date) {
        this.id = id;
        this.title = title;
        this.pitch = pitch;
        this.host = host;
        this.location = location;
        this.time = time;
        this.date = date;
    }

    @Override
    public String toString() {
        return title;
    }

    //ToDo - single date/time or save as calendar object
    @Override
    public int compareTo(Event o) {
        Calendar thisCalendar = Calendar.getInstance();
        Calendar compareCalendar = Calendar.getInstance();

        String myFormat = "dd/MM/yy";
        SimpleDateFormat databaseFormat = new SimpleDateFormat(myFormat);

        try {
            compareCalendar.setTime(databaseFormat.parse(o.getDate()));
        } catch (Exception ex) {

        }

        try {
            thisCalendar.setTime(databaseFormat.parse(getDate()));
        } catch (Exception ex) {

        }
        return thisCalendar.compareTo(compareCalendar);
    }
}
