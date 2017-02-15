package com.athayes.eventier;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONObject;

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

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            eventID = getArguments().get(ARG_ITEM_ID).toString();
            //System.out.println("ARG_ITEM_ID=" + eventID);
            Activity activity = this.getActivity();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_detail, container, false);

        Button btnShare = (Button) rootView.findViewById(R.id.share_button);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBody = "https://www.facebook.com/events/" + eventID;
                String shareSub = "Upcoming";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);

                try {
                    startActivity(Intent.createChooser(shareIntent, "Share Using"));
                } catch (android.content.ActivityNotFoundException ex) {
                }
            }
        });
        // Show the dummy content as text in a TextView.
        if (eventID != null) {
            // Facebook API call
            getEventFromFacebook(eventID, rootView);
        } else {
            System.out.println("Event id null");
        }
        return rootView;
    }

    public void getEventFromFacebook(String eventID, final View rootView) {
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
                            setUpTextViews(rootView, thisEvent);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
        ).executeAsync();

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                eventID + "/?fields=cover",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        // Async call, manage data here, rather than returning a value
                        try {
                            JSONObject JSONCover = response.getJSONObject();
                            String uri = "";
                            try {
                                JSONObject cover = JSONCover.getJSONObject("cover");
                                uri = cover.getString("source");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            //TODO better name
                            mCallback.onCoverRetrieved(uri);
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

        ((TextView) rootView.findViewById(R.id.timeValueLabel)).setText(thisEvent.time);
        ((TextView) rootView.findViewById(R.id.locationValueLabel)).setText(thisEvent.location);
        ((TextView) rootView.findViewById(R.id.dateValueLabel)).setText(thisEvent.date);

        ((TextView) rootView.findViewById(R.id.timeLabel)).setVisibility(View.VISIBLE);
        ((TextView) rootView.findViewById(R.id.locationLabel)).setVisibility(View.VISIBLE);
        ((TextView) rootView.findViewById(R.id.dateLabel)).setVisibility(View.VISIBLE);

        ((Button) rootView.findViewById(R.id.share_button)).setVisibility(View.VISIBLE);
        ((Button) rootView.findViewById(R.id.save_button)).setVisibility(View.VISIBLE);
    }

    OnCoverRetrievedListener mCallback;

    // Container Activity must implement this interface
    public interface OnCoverRetrievedListener {
        public void onCoverRetrieved(String uri);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        if (context instanceof Activity) {
            a = (Activity) context;
        }

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnCoverRetrievedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}
