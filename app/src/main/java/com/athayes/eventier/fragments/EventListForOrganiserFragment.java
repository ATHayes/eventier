package com.athayes.eventier.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.athayes.eventier.EventDetailActivity;
import com.athayes.eventier.EventListForOrganiserActivity;
import com.athayes.eventier.R;
import com.athayes.eventier.converters.EventConverter;
import com.athayes.eventier.models.Event;
import com.athayes.eventier.models.FacebookPage;
import com.athayes.eventier.utils.ISO8601;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventListForOrganiserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventListForOrganiserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventListForOrganiserFragment extends Fragment {

    private EventListForOrganiserFragment.OnFragmentInteractionListener mListener;
    private Boolean mTwoPane = false;
    private View recyclerView;
    private View emptyView;
    private ProgressBar progressBar;

    FacebookPage selectedFacebookPage;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    //Todo replace with object
    public static final String ARG_FACEBOOKPAGE_ID = "facebookpage_id";
    public static final String ARG_FACEBOOKPAGE_NAME = "facebookpage_name";

    //Logging
    private static final String TAG = "EventListForOrganiserFragment";

    //List of events, used for temporary storage - async methods
    List<Event> allEvents = new ArrayList<>();

    // Counters for GraphRequestBatches
    int totalBatches = 0;
    int batchesProcessed = 0;

    public EventListForOrganiserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventListForOrganiserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventListForOrganiserFragment newInstance(String param1, String param2) {
        EventListForOrganiserFragment fragment = new EventListForOrganiserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String id = getArguments().get(ARG_FACEBOOKPAGE_ID).toString();
            String name = getArguments().get(ARG_FACEBOOKPAGE_NAME).toString();
            selectedFacebookPage = new FacebookPage(name, id);
        }

        EventListForOrganiserActivity activity = (EventListForOrganiserActivity) getActivity();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);
        recyclerView = rootView.findViewById(R.id.event_recycler_view);
        emptyView = rootView.findViewById(R.id.empty_view);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        // Calendars
        final Calendar untilCalendar = Calendar.getInstance();
        final Calendar todayCalendar = Calendar.getInstance();

        untilCalendar.add(Calendar.WEEK_OF_YEAR, 3);

        getEventsFromFacebook(todayCalendar, untilCalendar);

        if (rootView.findViewById(R.id.event_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EventListForOrganiserFragment.OnFragmentInteractionListener) {
            mListener = (EventListForOrganiserFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<EventListForOrganiserFragment.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Event> mValues;

        public SimpleItemRecyclerViewAdapter(List<Event> items) {
            mValues = items;
        }

        @Override
        public EventListForOrganiserFragment.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.event_list_content, parent, false);
            return new EventListForOrganiserFragment.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final EventListForOrganiserFragment.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).host);
            holder.mTitleView.setText(mValues.get(position).title);

            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
            Calendar startTimeCal = null;
            try {
                startTimeCal = ISO8601.toCalendar(mValues.get(position).startTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.mTimeView.setText(timeFormat.format(startTimeCal.getTime()));
            holder.mLocationView.setText(mValues.get(position).location);

            holder.mDateView.setVisibility(View.VISIBLE);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE d MMMM");
            String dateString = dateFormat.format(startTimeCal.getTime());
            holder.mDateView.setText(dateString);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(EventDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        EventDetailFragment fragment = new EventDetailFragment();
                        fragment.setArguments(arguments);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.event_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, EventDetailActivity.class);
                        intent.putExtra(EventDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView, mTitleView, mTimeView, mLocationView, mDateView;
            public Event mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mTitleView = (TextView) view.findViewById(R.id.titleValueLabel);
                mTimeView = (TextView) view.findViewById(R.id.timeValueLabel);
                mLocationView = (TextView) view.findViewById(R.id.locationValueLabel);
                mDateView = (TextView) view.findViewById(R.id.dateValueLabel);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTitleView.getText() + "'";
            }
        }
    }

    // Overrided method - pass in a list of items
    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Event> ITEMS) {
        recyclerView.swapAdapter(new EventListForOrganiserFragment.SimpleItemRecyclerViewAdapter(ITEMS), false);
    }


    public void getEventsFromFacebook(Calendar dateCalendar) {
        getEventsFromFacebook(dateCalendar, dateCalendar);
    }


    public void addToList(List<Event> ITEMS) {
        allEvents.addAll(ITEMS);
    }

    public GraphRequestBatch facebookPageRequestBatch(ArrayList<FacebookPage> facebookPages, Calendar since, Calendar until) {
        GraphRequestBatch requestBatch = new GraphRequestBatch();
        for (FacebookPage page : facebookPages) {
            requestBatch.add(getEvents(page.getFacebookID(), page.getName(), since, until));
        }
        return requestBatch;
    }

    // Get events since x until y
    public GraphRequest getEvents(String facebookID, final String hostName, Calendar sinceCalendar, Calendar untilCalendar) {
        SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sinceAPIString = apiFormat.format(sinceCalendar.getTime());
        String untilAPIString = apiFormat.format(untilCalendar.getTime());
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + facebookID + "/events?since=" + sinceAPIString + "T00:00:00&until=" + untilAPIString + "T23:59:59",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        // Async call, manage data here, rather than returning a value
                        //TODO check if response object is null - will be more efficient
                        try {
                            JSONArray events = response.getJSONObject().getJSONArray("data");
                            List<Event> ITEMS = EventConverter.getFromJSONArray(events, hostName);
                            addToList(ITEMS);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
        );
        return request;
    }

    public void getEventsFromFacebook(Calendar sinceCalendar, Calendar untilCalendar) {
        SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sinceAPIString = apiFormat.format(sinceCalendar.getTime());
        String untilAPIString = apiFormat.format(untilCalendar.getTime());
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + selectedFacebookPage.getFacebookID()
                        + "/events?since="
                        + sinceAPIString +
                        "T00:00:00&until="
                        + untilAPIString +
                        "T23:59:59",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        // Async call, manage data here, rather than returning a value
                        //TODO check if response object is null - will be more efficient
                        try {
                            JSONArray events = response.getJSONObject().getJSONArray("data");
                            List<Event> ITEMS = EventConverter.getFromJSONArray(events, selectedFacebookPage.getName());
                            //set up the ui
                            assert recyclerView != null;
                            Collections.sort(ITEMS);
                            if (!ITEMS.isEmpty()) {
                                progressBar.setVisibility(View.GONE);
                                emptyView.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                setupRecyclerView((RecyclerView) recyclerView, ITEMS);
                            } else {
                                // No data found
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                                emptyView.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
        );
        request.executeAsync();
    }
}
