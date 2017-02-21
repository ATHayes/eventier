package com.athayes.eventier;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        String startTime = "";
        String endTime = "";

        try {
            title = event.getString("name");
            id = event.getString("id");
            description = event.getString("description");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            //Event start time
            startTime = event.getString("start_time");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            //Event end time
            endTime = event.getString("end_time");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Formats
        SimpleDateFormat databaseFormat = new SimpleDateFormat("dd/MM/yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");

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

        Event event1 = new Event(
                id,
                title,
                description,
                host,
                placeName,
                startTime,
                endTime);
        return event1;
    }
}
