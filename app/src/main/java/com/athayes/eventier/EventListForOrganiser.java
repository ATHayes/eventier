package com.athayes.eventier;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.athayes.eventier.adapters.EventService;
import com.athayes.eventier.fragments.AdFragment;
import com.athayes.eventier.fragments.EventDetailFragment;
import com.athayes.eventier.models.Event;
import com.athayes.eventier.models.FacebookPage;
import com.athayes.eventier.utils.ISO8601;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * An activity representing a list of Events. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link EventDetailActivity} representing
 * item pitch. On tablets, the activity presents the list of items and
 * item pitch side-by-side using two vertical panes.
 */
public class EventListForOrganiser extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, AdFragment.OnFragmentInteractionListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    public static final String ARG_ITEM_ID = "item_id";
    // Code number for new event ActivityForResult
    static final int CREATE_EVENT_REQUEST = 1;

    // Firebase instance variables
    private String mUsername;
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences;
    public static final String ANONYMOUS = "anonymous";

    // Firebase-auth
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    //Logging
    private static final String TAG = "EventListActivity";

    FacebookPage selectedFacebookPage;

    // Counters for GraphRequestBatches
    int totalBatches = 0;
    int batchesProcessed = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String arg_facebook_id = getIntent().getStringExtra("FacebookID");
        System.out.println(arg_facebook_id);
        System.out.println("-------------------");

        selectedFacebookPage = GlobalVariables.getInstance().getFacebookPage(arg_facebook_id);

        System.out.println(selectedFacebookPage.getFacebookID());
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Action bar title
        setTitle(selectedFacebookPage.getName());
        getSupportActionBar().setSubtitle("Upcoming Events");

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Check if user is signed in
        if (mFirebaseUser == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            // Set up name and TODO profile picture
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

        View recyclerView = findViewById(R.id.event_list);
        assert recyclerView != null;

        if (findViewById(R.id.event_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        // Calendar - Not a singleton - an abstract class.
        // The getInstance method is a FACTORY METHOD that returns a concrete implementation of the Calendar class.
        final Calendar untilCalendar = Calendar.getInstance();
        final Calendar todayCalendar = Calendar.getInstance();

        untilCalendar.add(Calendar.WEEK_OF_YEAR, 3);
        final Date today = new Date();
        getEventFromFacebook(todayCalendar, untilCalendar);

        SimpleDateFormat displayFormat = new SimpleDateFormat("EEEE MMM d");
        String sinceAPIString = displayFormat.format(todayCalendar.getTime());
        String untilAPIString = displayFormat.format(untilCalendar.getTime());



    }

    // Event handler for back button
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Event handler for side menu opening
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.test_nav, menu);
        return true;
    }

    // Event handler for menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more pitch, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            //navigateUpTo(new Intent(this, FacebookPageListActivity.class));
            finish();
            return true;
        }

        // Back button go back to whatever screen called it
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // Overrided method - pass in a list of items
    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Event> ITEMS) {
        // Set the adaptor with the items

        recyclerView.swapAdapter(new SimpleItemRecyclerViewAdapter(ITEMS), false);
        System.out.println("Adapter view swapped!");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private final List<Event> mValues;

        public SimpleItemRecyclerViewAdapter(List<Event> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.event_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).host);

            Calendar startTimeCal = null;
            try {
                startTimeCal = ISO8601.toCalendar(holder.mItem.startTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE d MMMM");
            String startTimeString = timeFormat.format(startTimeCal.getTime());
            String dateString = dateFormat.format(startTimeCal.getTime());

            holder.mTitleView.setText(mValues.get(position).title);
            holder.mTimeView.setText(startTimeString);
            holder.mLocationView.setText(mValues.get(position).location);
            holder.mDateView.setVisibility(View.VISIBLE);
            holder.mDateView.setText(dateString);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, EventDetailActivity.class);
                    intent.putExtra(EventDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                    context.startActivity(intent);
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


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    public void getEventFromFacebook(Calendar dateCalendar) {
        getEventFromFacebook(dateCalendar, dateCalendar);
    }

    public void getEventFromFacebook(Calendar sinceCalendar, Calendar untilCalendar) {
        final View recyclerView = findViewById(R.id.event_list);
        final View emptyView = findViewById(R.id.empty_view);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
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
                            List<Event> ITEMS = EventService.getFromJSONArray(events, selectedFacebookPage.getName());
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


    public void setPicture() {

    }

}

