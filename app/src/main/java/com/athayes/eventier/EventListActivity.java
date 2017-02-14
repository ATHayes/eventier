package com.athayes.eventier;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class EventListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
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

    // Firebase instance variables - Step 7 of Firebase codelab
//    private DatabaseReference mFirebaseDatabaseReference;
//    private FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>
//            mFirebaseAdapter;

    //List of events, used for temporary storage - async methods
    List<Event> allEvents = new ArrayList<>();

    // Counters for GraphRequestBatches
    int totalBatches = 0;
    int batchesProcessed = 0;


    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        //Load ads
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7581972583339154~7785733029");
        // Initialize and request AdMob ad.
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Action bar title
        setTitle("Home");

        // Drawer (side menu)
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,                               /* host Activity */
                drawer,                             /* DrawerLayout object */
                toolbar,                            /* nav drawer icon to replace 'Up' caret */
                R.string.navigation_drawer_open,    /* "open drawer" description */
                R.string.navigation_drawer_close);  /* "close drawer" description */

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userName = (TextView) headerView.findViewById(R.id.userName);

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
            userName.setText(mUsername);
        }

        //RecyclerView
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
        final Calendar selectCalendar = Calendar.getInstance();
        final Calendar todayCalendar = Calendar.getInstance();
        final Date today = new Date();

        //get our Facebook page pics



        //getEventsFromFacebook();
        getEventsFromFacebook(todayCalendar);

        getSupportActionBar().setSubtitle("Today's Events");

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
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    // Event handler for menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_button_menu) {

            final Calendar selectCalendar = Calendar.getInstance();
            final Calendar todayCalendar = Calendar.getInstance();

            // Date picker and Recycler View
            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    selectCalendar.set(Calendar.YEAR, year);
                    selectCalendar.set(Calendar.MONTH, monthOfYear);
                    selectCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    SimpleDateFormat displayFormat = new SimpleDateFormat("EEEE, MMM d, yyyy");
                    if (todayCalendar.get(Calendar.DATE) == selectCalendar.get(Calendar.DATE)) {
                        getSupportActionBar().setSubtitle("Today's Events");
                    } else {
                        getSupportActionBar().setSubtitle(displayFormat.format(selectCalendar.getTime()));
                    }
                    //get Facebook events for this day - the list will be updated automatically
                    getEventsFromFacebook(selectCalendar);
                }
            };

            new DatePickerDialog(EventListActivity.this, date, selectCalendar
                    .get(Calendar.YEAR), selectCalendar.get(Calendar.MONTH),
                    selectCalendar.get(Calendar.DAY_OF_MONTH)).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_date) {
            final Calendar todayCalendar = Calendar.getInstance();
            getEventsFromFacebook(todayCalendar);
            getSupportActionBar().setSubtitle("Today's Events");

        } else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareBody = getResources().getString(R.string.play_store_url);
            String shareSub = getResources().getString(R.string.share_message);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);

            try {
                startActivity(Intent.createChooser(shareIntent, "Share Using"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(EventListActivity.this, "Error Sharing Content", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_sign_out) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            mFirebaseAuth.signOut();
                            mUsername = ANONYMOUS;
                            startActivity(new Intent(EventListActivity.this, SignInActivity.class));

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to log out?").setPositiveButton("Log out", dialogClickListener)
                    .setNegativeButton("Stay here", dialogClickListener).show();

        } else if (id == R.id.nav_privacy_policy) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.privacy_policy_url)));
            startActivity(browserIntent);
        } else if (id == R.id.nav_rate) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.play_store_url)));
            startActivity(browserIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Overrided method - pass in a list of items
    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Event> ITEMS) {
        // Set the adaptor with the items
        for (Event e : ITEMS) {
            System.out.println("Event: " + e.title + "date " + e.date);
        }
        recyclerView.swapAdapter(new SimpleItemRecyclerViewAdapter(ITEMS), false);
        System.out.println("Adapter view swapped!");
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
            holder.mTitleView.setText(mValues.get(position).title);
            holder.mTimeView.setText(mValues.get(position).time);
            holder.mLocationView.setText(mValues.get(position).location);
            //holder.mDateView.setText(mValues.get(position).date);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(EventDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        EventDetailFragment fragment = new EventDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
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
            public final TextView mIdView, mTitleView, mTimeView, mLocationView;
            public Event mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mTitleView = (TextView) view.findViewById(R.id.titleValueLabel);
                mTimeView = (TextView) view.findViewById(R.id.timeValueLabel);
                mLocationView = (TextView) view.findViewById(R.id.locationValueLabel);

            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTitleView.getText() + "'";
            }
        }


    }

    // When the user is done with the subsequent activity and returns, the system calls the activity's onActivityResult() method.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == CREATE_EVENT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // TODO - consider passing the date to the activity and back again, rather than simply recreating the activity
                // The user created an event
                Intent intent = getIntent();
                finish();
                startActivity(intent);
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


    public void getEventsFromFacebook(Calendar dateCalendar) {
        getEventsFromFacebook(dateCalendar, dateCalendar);
    }

    public void getEventsFromFacebook(Calendar sinceCalendar, Calendar untilCalendar) {

        final View recyclerView = findViewById(R.id.event_list);
        final View emptyView = findViewById(R.id.empty_view);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        allEvents.clear();

        ArrayList<FacebookPage> facebookPages = GlobalVariables.getInstance().getFacebookPages();
        ArrayList<GraphRequestBatch> requestBatchList = new ArrayList<>();

        int numberOfPages = facebookPages.size();
        int pagesPerBatch = 50;

        // Round up (ceil)
        totalBatches = (numberOfPages / pagesPerBatch) + ((numberOfPages == 0) ? 0 : 1);

        // Reset counter
        batchesProcessed = 0;

        for (int i = 0; i <= (totalBatches - 1); i++) {
            int startIndex = 0 + (i * pagesPerBatch);
            int endIndex = 49 + (i * pagesPerBatch);

            // Avoid overflow error
            if (endIndex >= numberOfPages) {
                endIndex = numberOfPages - 1;
            }

            requestBatchList.add(facebookPageRequestBatch(
                    new ArrayList<FacebookPage>(facebookPages.subList(startIndex, endIndex)),
                    sinceCalendar,
                    untilCalendar));

            requestBatchList.get(i).addCallback(new GraphRequestBatch.Callback() {
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

    // For images
//    public GraphRequestBatch facebookPageRequestBatch(ArrayList<FacebookPage> facebookPages) {
//        GraphRequestBatch requestBatch = new GraphRequestBatch();
//        for (FacebookPage page : facebookPages) {
//            requestBatch.add(getPictures(page.getFacebookID(), page.getName()));
//        }
//        return requestBatch;
//    }

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
                            List<Event> ITEMS = EventService.getFromJSONArray(events, hostName);
                            addToList(ITEMS);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
        );
        return request;
    }

//    private void getPagePhotosFromFacebook(){
//        ArrayList<FacebookPage> facebookPages = GlobalVariables.getInstance().getFacebookPages();
//        ArrayList<GraphRequestBatch> requestBatchList = new ArrayList<>();
//
//        int numberOfPages = facebookPages.size();
//        int pagesPerBatch = 50;
//
//        // Round up (ceil)
//        totalBatches = (numberOfPages / pagesPerBatch) + ((numberOfPages == 0) ? 0 : 1);
//
//        // Reset counter
//        batchesProcessed = 0;
//
//        for (int i = 0; i <= (totalBatches - 1); i++) {
//            int startIndex = 0 + (i * pagesPerBatch);
//            int endIndex = 49 + (i * pagesPerBatch);
//
//            // Avoid overflow error
//            if (endIndex >= numberOfPages) {
//                endIndex = numberOfPages - 1;
//            }
//
//            requestBatchList.add(facebookPageRequestBatch(new ArrayList<FacebookPage>(facebookPages.subList(startIndex, endIndex));
//
//            requestBatchList.get(i).addCallback(new GraphRequestBatch.Callback() {
//                                                    @Override
//                                                    public void onBatchCompleted(GraphRequestBatch batch) {
//                                                        batchesProcessed += 1;
//                                                        if (batchesProcessed >= totalBatches) {
//
//                                                            // Reset counters
//                                                            totalBatches = 0;
//                                                            batchesProcessed = 0;
//
//                                                            } else {
//
//                                                            }
//                                                        }
//
//                                                }
//            );
//        }
//
//        for (GraphRequestBatch requestBatch : requestBatchList) {
//            requestBatch.executeAsync();
//        }
//    }


}
