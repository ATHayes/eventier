package com.athayes.eventier.models;

import com.athayes.eventier.utils.ISO8601;

import java.text.ParseException;
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

        try {
            thisCalendar = ISO8601.toCalendar(getStartTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            compareCalendar = ISO8601.toCalendar(o.getStartTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return thisCalendar.compareTo(compareCalendar);
    }
}