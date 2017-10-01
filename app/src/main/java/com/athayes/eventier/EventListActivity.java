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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.athayes.eventier.fragments.EventDetailFragment;
import com.athayes.eventier.fragments.EventListFragment;
import com.athayes.eventier.models.Event;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener,
        EventDetailFragment.OnCoverRetrievedListener,
        EventListFragment.OnFragmentInteractionListener {

    // Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
    private boolean mTwoPane;

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

    //List of events, used for temporary storage - async methods
    List<Event> allEvents = new ArrayList<>();

    // Counters for GraphRequestBatches
    int totalBatches = 0;
    int batchesProcessed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Update location selection from sharedPreference
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        GlobalVariables.getInstance().setPageCollection(sharedPref.getString((getString(R.string.preference_file_key)), ""));

        setContentView(R.layout.activity_event_list);
        //Fragment onCreateView starts now

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        //TODO - set to the value in the sharedPreference on onResume or something...
        if (getTitle() == "") {
            setTitle("Home");
        }
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

        // Check if user is signed in
        if (mFirebaseUser == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

        if (findViewById(R.id.event_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

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
                    if (mTwoPane) {
                        // If a detail fragment is present
                        if (getSupportFragmentManager().findFragmentById(R.id.event_detail_container) != null) {
                            getSupportFragmentManager()
                                    .beginTransaction().
                                    remove(getSupportFragmentManager().findFragmentById(R.id.event_detail_container)).commit();
                        }
                    }
                    EventListFragment eventListFragment = (EventListFragment) getSupportFragmentManager().findFragmentById(R.id.eventListFragment);
                    if (todayCalendar.get(Calendar.DATE) == selectCalendar.get(Calendar.DATE)) {
                        getSupportActionBar().setSubtitle("Today's Events");
                        eventListFragment.getEventsFromFacebook(todayCalendar);
                    } else {
                        SimpleDateFormat displayFormat = new SimpleDateFormat("EEEE, MMM d, yyyy");
                        getSupportActionBar().setSubtitle(displayFormat.format(selectCalendar.getTime()));
                        eventListFragment.getEventsFromFacebook(selectCalendar);
                    }
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
            //Todo fix
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
                            // Sign out of Facebook
                            LoginManager.getInstance().logOut();
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
        } else if (id == R.id.nav_search_host) {
            Intent intent = new Intent(this, FacebookPageListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_set_location) {
            Intent intent = new Intent(this, SelectLocationActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //required for fragments
    }

    @Override
    public void onCoverRetrieved(String uri) {
        // Required - do nothing here
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume(){
        super.onResume();
        // Update location and title bar from sharedPreference
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        setTitle(sharedPref.getString(getString(R.string.preference_file_key), ""));
        GlobalVariables.getInstance().setPageCollection(sharedPref.getString((getString(R.string.preference_file_key)), ""));
    }

}
