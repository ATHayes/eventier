package com.athayes.eventier.converters;

import android.util.Log;

import com.athayes.eventier.models.Event;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anthonyhayes on 03/01/2017.
 *
 * Can convert an array of JSON objects or a single JSON object into an Event
 */


public class EventConverter {
    // Todo - break event down to different objects to match JSON
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
        String title = "No title";
        String description = "No description";
        String id = "";
        String startTime = "";
        String endTime = "";
        String coverUrl = "";

        try {
            title = event.getString("name");
            id = event.getString("id");
            description = event.getString("description");
        } catch (Exception ex) {
            Log.d("Graph_API", "No title/id/description for "+ host + " event: " + title);
        }

        try {
            //Event start time
            startTime = event.getString("start_time");
        } catch (Exception ex) {
            Log.d("Graph_API", "No start time for "+ host + " event: " + title);
        }

        try {
            //Event end time
            endTime = event.getString("end_time");
        } catch (Exception ex) {
            Log.d("Graph_API", "No end time for "+ host + " event: " + title);

        }

        //Formats
        SimpleDateFormat databaseFormat = new SimpleDateFormat("dd/MM/yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");

        // Place node (JSON object)/ optional
        try {
            JSONObject place = event.getJSONObject("place");
            placeName = place.getString("name");
        } catch (Exception ex) {
            Log.d("Graph_API", "No placeName for "+ host + " event: " + title);
        }

        // Location node (JSON Object) /optional
        try {
            JSONObject place = event.getJSONObject("place");
            JSONObject location = place.getJSONObject("location");
            city = location.getString("city");
            street = location.getString("street");
        } catch (Exception ex) {
            Log.d("Graph_API", "No location for "+ host + " event: " + title);

        }

        try{
            JSONObject cover = event.getJSONObject("cover");
            coverUrl = cover.getString("source");
        }
        catch (Exception ex){
            Log.d("Graph_API", "No cover url for "+ host + " event: " + title);
        }

        Event event1 = new Event(
                id,
                title,
                description,
                host,
                placeName,
                startTime,
                endTime,
                coverUrl);
        return event1;
    }
}
