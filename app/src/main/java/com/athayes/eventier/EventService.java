package com.athayes.eventier;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by anthonyhayes on 03/01/2017.
 */


public class EventService {

    // Refactor with getFromJSONObject
    public static List<Event> getFromJSONArray(JSONArray events, String host) {
        // List
        List<Event> ITEMS = new ArrayList<>();

        // loop all Events
        for (int i = 0; i < events.length(); i++) {
            String city = "No city specified";
            String street = "No street specified";
            String placeName = "No place name specified";
            String title = "No title";
            String id = "";
            String description = "No description";
            JSONObject event = null;
            Calendar cal = null;

            try {
                event = events.getJSONObject(i);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                title = event.getString("name");
                id = event.getString("id");
                description = event.getString("description");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                //Event DateTime
                String raw = event.getString("start_time");
                cal = ISO8601.toCalendar(raw);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // Event date
            SimpleDateFormat databaseFormat = new SimpleDateFormat("dd/MM/yy");
            String eventDate = databaseFormat.format(cal.getTime());

            //Event time
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
            String eventTime = timeFormat.format(cal.getTime());

            // Place node (JSON object)/ optional
            try {
                JSONObject place = event.getJSONObject("place");
                placeName = place.getString("name");
            } catch (Exception ex) {
                //System.out.println("place error");
            }

            // Location node (JSON Object) /optional
            try {
                JSONObject place = event.getJSONObject("place");
                JSONObject location = place.getJSONObject("location");
                city = location.getString("city");
                street = location.getString("street");
            } catch (Exception ex) {
                //System.out.println("location error");
            }
            //Create event object
            Event event1 = new Event(
                    id,
                    title,
                    description,
                    host,
                    placeName,
                    eventTime,
                    eventDate);
            //Add to list
            ITEMS.add(event1);
        }
        return ITEMS;
    }


    public static Event getFromJSONObject(JSONObject event) {

        String city = "No city specified";
        String street = "No street specified";
        String placeName = "No place name specified";
        String host = "Netsoc";
        String title = "No title";
        String description = "No description";
        String id = "";
        Calendar cal = null;

        try {
            title = event.getString("name");
            id = event.getString("id");
            description = event.getString("description");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            //Event DateTime
            String raw = event.getString("start_time");
            cal = ISO8601.toCalendar(raw);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Event date
        SimpleDateFormat databaseFormat = new SimpleDateFormat("dd/MM/yy");
        String eventDate = databaseFormat.format(cal.getTime());

        //Event time
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String eventTime = timeFormat.format(cal.getTime());

        // Place node (JSON object)/ optional
        try {
            JSONObject place = event.getJSONObject("place");
            placeName = place.getString("name");
        } catch (Exception ex) {
            //System.out.println("place error");
        }

        // Location node (JSON Object) /optional
        try {
            JSONObject place = event.getJSONObject("place");
            JSONObject location = place.getJSONObject("location");
            city = location.getString("city");
            street = location.getString("street");
        } catch (Exception ex) {
            //System.out.println("location error");
        }
        //Create event object
        Event event1 = new Event(
                id,
                title,
                description,
                host,
                placeName,
                eventTime,
                eventDate);

        return event1;
    }
}
