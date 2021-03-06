package com.athayes.eventier.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.athayes.eventier.EventDetailActivity;
import com.athayes.eventier.GlobalVariables;
import com.athayes.eventier.R;
import com.athayes.eventier.converters.EventConverter;
import com.athayes.eventier.models.Event;
import com.athayes.eventier.models.FacebookPage;
import com.athayes.eventier.utils.ISO8601;
import com.athayes.eventier.utils.NetworkTest;
import com.athayes.eventier.utils.SpacingItemDecoration;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.squareup.picasso.Picasso;

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
 * {@link EventListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private Boolean mTwoPane = false;
    private RecyclerView recyclerView;
    private View emptyView;
    private ProgressBar progressBar;

    //Logging
    private static final String TAG = "EventListFragment";

    //List of events, used for temporary storage - async methods
    List<Event> allEvents = new ArrayList<>();

    // Counters for GraphRequestBatches
    int totalBatches = 0;
    int batchesProcessed = 0;

    public EventListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventListFragment newInstance(String param1, String param2) {
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.event_recycler_view);
        recyclerView.addItemDecoration(new SpacingItemDecoration(getActivity(), 16));
        emptyView = rootView.findViewById(R.id.empty_view);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        final Calendar todayCalendar = Calendar.getInstance();
        getEventsFromFacebook(todayCalendar);

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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
            extends RecyclerView.Adapter<EventListFragment.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Event> mValues;

        public SimpleItemRecyclerViewAdapter(List<Event> items) {
            mValues = items;
        }

        @Override
        public EventListFragment.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.event_list_content, parent, false);
            return new EventListFragment.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final EventListFragment.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).host);
            holder.mTitleView.setText(mValues.get(position).title);

            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
            try {
                Calendar startTimeCal = ISO8601.toCalendar(mValues.get(position).startTime);
                holder.mTimeView.setText(timeFormat.format(startTimeCal.getTime()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.mLocationView.setText(mValues.get(position).location);
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
            try {
                Picasso.with(getActivity())
                        .load(mValues.get(position).coverUrl)
                        .into(holder.mCoverPhoto);
                Log.d("Picasso", "Loaded cover for " + mValues.get(position).host);
            } catch (Exception ex) {
                // Set the image - has to be done in code rather than a default in xml - see https://stackoverflow.com/questions/26359608/picasso-loads-into-wrong-imageview-when-using-view-holder-pattern
                holder.mCoverPhoto.setImageResource(R.drawable.placeholder);
                Log.d("Picasso", "Failed to load cover url for " + mValues.get(position).host);
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView, mTitleView, mTimeView, mLocationView;
            public Event mItem;
            public ImageView mCoverPhoto;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mTitleView = (TextView) view.findViewById(R.id.titleValueLabel);
                mTimeView = (TextView) view.findViewById(R.id.timeValueLabel);
                mLocationView = (TextView) view.findViewById(R.id.locationValueLabel);
                mCoverPhoto = (ImageView) view.findViewById(R.id.cover_photo);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTitleView.getText() + "'";
            }
        }
    }

    // Overrided method - pass in a list of items
    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Event> ITEMS) {
        recyclerView.swapAdapter(new EventListFragment.SimpleItemRecyclerViewAdapter(ITEMS), false);
    }

    public void getEventsFromFacebook(Calendar dateCalendar) {
        getEventsFromFacebook(dateCalendar, dateCalendar);
    }

    public void getEventsFromFacebook(Calendar sinceCalendar, Calendar untilCalendar) {
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        allEvents.clear();

        // When selecting from global variables, check shared preferences
        // if shared preferences is null, use ucc
        // if "Toronto", use new fb pages
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        // Check location in sharedPreferences
        String location = getResources().getString(R.string.preference_file_key);
        // Todo - break out into a method somewhere external the the activity
        // Todo - also consider if all these checks should only happen in the activity
        switch (location) {
            case "Toronto":
                GlobalVariables.getInstance().setPageCollection("Toronto");
                break;
            case "Cork (UCC)":
                GlobalVariables.getInstance().setPageCollection("Cork (UCC)");
                break;
            default:
                break;
        }

        ArrayList<FacebookPage> facebookPages = GlobalVariables.getInstance().getFacebookPages();
        ArrayList<GraphRequestBatch> requestBatchList = new ArrayList<>();

        int numberOfPages = facebookPages.size();
        int pagesPerBatch = 50;

        // Round up (ceil)
        totalBatches = (numberOfPages / pagesPerBatch) + ((numberOfPages == 0) ? 0 : 1);

        // Reset counter
        batchesProcessed = 0;

        for (int i = 0; i <= (totalBatches - 1); i++) {
            int startIndex = (i * pagesPerBatch);
            int endIndex = 49 + (i * pagesPerBatch);

            // Avoid overflow error
            if (endIndex >= numberOfPages) {
                endIndex = numberOfPages;
            }

            ArrayList<FacebookPage> sublistPages = new ArrayList<>(facebookPages.subList(startIndex, endIndex));

            requestBatchList.add(facebookPageRequestBatch(
                    sublistPages,
                    sinceCalendar,
                    untilCalendar));

            requestBatchList.get(i).addCallback(
                    new GraphRequestBatch.Callback() {
                        @Override
                        public void onBatchCompleted(GraphRequestBatch batch) {
                            batchesProcessed += 1;
                            if (batchesProcessed >= totalBatches) {

                                // Reset counters
                                totalBatches = 0;
                                batchesProcessed = 0;

                                Collections.sort(allEvents);

                                assert recyclerView != null;
                                if (!allEvents.isEmpty()) {
                                    progressBar.setVisibility(View.GONE);
                                    emptyView.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    setupRecyclerView((RecyclerView) recyclerView, allEvents);
                                } else {
                                    // No data found
                                    progressBar.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.GONE);
                                    emptyView.setVisibility(View.VISIBLE);
                                    NetworkTest networkTest = new NetworkTest(getActivity().getBaseContext());
                                    if (!networkTest.isNetworkAvailable()) {
                                        Toast.makeText(getActivity().getBaseContext(), "Events not found: No internet connection.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    }
            );
        }

        for (GraphRequestBatch requestBatch : requestBatchList) {
            requestBatch.executeAsync();
        }
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
    public GraphRequest getEvents(final String facebookID, final String hostName, Calendar sinceCalendar, Calendar untilCalendar) {
        SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String sinceAPIString = apiFormat.format(sinceCalendar.getTime());
        final String untilAPIString = apiFormat.format(untilCalendar.getTime());
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + facebookID + "/events?fields=description,end_time,name,place,start_time,id,cover&since=" + sinceAPIString + "T00:00:00&until=" + untilAPIString + "T23:59:59",
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
                            Log.d("Graph_API", "Events for Facebook Page: "+ hostName + " since: " + sinceAPIString + " until: " + untilAPIString + " not found");
                        }
                    }
                }
        );
        return request;
    }
}
