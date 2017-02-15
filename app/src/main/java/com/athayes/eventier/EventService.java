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

    public static List<Event> getFromJSONArray(JSONArray events, String host) {
        // List
        List<Event> ITEMS = new ArrayList<>();

        for (int i = 0; i < events.length(); i++) {
            try {
                JSONObject JSONevent = events.getJSONObject(i);
                Event event = getFromJSONObject(JSONevent, host);
                ITEMS.add(event);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return ITEMS;
    }


    public static Event getFromJSONObject(JSONObject event) {
        Event event1 = getFromJSONObject(event, "None");
        return event1;
    }

    public static Event getFromJSONObject(JSONObject event, String host) {

        String city = "No city specified";
        String street = "No street specified";
        String placeName = "No place name specified";
//        String photoUrl = "No photo url";

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

//        // Photo url
//        try {
//            JSONObject cover = event.getJSONObject("cover");
//            photoUrl = cover.getString("source");
//        } catch (Exception ex) {
//            //ex.printStackTrace();
//        }
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
