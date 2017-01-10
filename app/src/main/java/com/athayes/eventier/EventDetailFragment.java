package com.athayes.eventier;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * A fragment representing a single Event detail screen.
 * This fragment is either contained in a {@link EventListActivity}
 * in two-pane mode (on tablets) or a {@link EventDetailActivity}
 * on handsets.
 */
public class EventDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private String eventID;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments().containsKey(ARG_ITEM_ID)) {
//            // Load the dummy content specified by the fragment
//            // arguments. In a real-world scenario, use a Loader
//            // to load content from a content provider.
//            mItem = GlobalVariables.getInstance().getITEM_MAP().get(getArguments().getString(ARG_ITEM_ID));
//            Activity activity = this.getActivity();
//        }

        // TODO The event id from Facebook API...
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            eventID = getArguments().get(ARG_ITEM_ID).toString();
            System.out.println("ARG_ITEM_ID=" + eventID);
            Activity activity = this.getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (eventID != null) {
            // Facebook API call
            getEventFromFacebook(eventID, rootView);
        } else {
            System.out.println("Event id null!!!!");
        }
        return rootView;
    }

    public void getEventFromFacebook(String eventID, final View rootView) {
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Please wait...");
        //progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        // TODO Facebook Access
        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                eventID,
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        // Async call, manage data here, rather than returning a value
                        try {
                            JSONObject JSONEvent = response.getJSONObject();

                            Event thisEvent = EventService.getFromJSONObject(JSONEvent);
                            System.out.println("----TEST----");
                            System.out.println(thisEvent.title);
                            setUpTextViews(rootView, thisEvent);
                            progress.dismiss();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
        ).executeAsync();


    }

    public void setUpTextViews(View rootView, Event thisEvent) {
        ((TextView) rootView.findViewById(R.id.titleValueLabel)).setText(thisEvent.title);
        ((TextView) rootView.findViewById(R.id.pitchValueLabel)).setText(thisEvent.pitch);
        ((TextView) rootView.findViewById(R.id.hostValueLabel)).setText(thisEvent.host);
        ((TextView) rootView.findViewById(R.id.timeValueLabel)).setText(thisEvent.time);
        ((TextView) rootView.findViewById(R.id.locationValueLabel)).setText(thisEvent.location);
        ((TextView) rootView.findViewById(R.id.dateValueLabel)).setText(thisEvent.date);

        ((TextView) rootView.findViewById(R.id.hostLabel)).setVisibility(View.VISIBLE);
        ((TextView) rootView.findViewById(R.id.timeLabel)).setVisibility(View.VISIBLE);
        ((TextView) rootView.findViewById(R.id.locationLabel)).setVisibility(View.VISIBLE);
        ((TextView) rootView.findViewById(R.id.dateLabel)).setVisibility(View.VISIBLE);
    }
}
