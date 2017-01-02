package com.athayes.eventier;

import java.util.Date;


/**
 * Created by anthonyhayes on 05/11/2016.
 */

public class Event {

    public final String id;
    public final String title;
    public final String pitch;
    public final String host;
    public final String location;
    public final String time;

    // Firebase doesn't support the date class in Java - we'll store the date as a string
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

}
