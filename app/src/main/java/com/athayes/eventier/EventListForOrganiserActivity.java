package com.athayes.eventier;

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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.athayes.eventier.fragments.EventDetailFragment;
import com.athayes.eventier.fragments.EventListForOrganiserFragment;
import com.athayes.eventier.models.FacebookPage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * An activity representing a list of Events. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link EventDetailActivity} representing
 * item pitch. On tablets, the activity presents the list of items and
 * item pitch side-by-side using two vertical panes.
 */
public class EventListForOrganiserActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener,
        EventDetailFragment.OnCoverRetrievedListener,
        EventListForOrganiserFragment.OnFragmentInteractionListener {

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

    public FacebookPage getSelectedFacebookPage() {
        return selectedFacebookPage;
    }

    FacebookPage selectedFacebookPage;

    // Counters for GraphRequestBatches
    int totalBatches = 0;
    int batchesProcessed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list_for_organiser);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String arg_facebook_id = getIntent().getStringExtra("FacebookID");

        selectedFacebookPage = GlobalVariables.getInstance().getFacebookPage(arg_facebook_id);
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Action bar title
        setTitle(selectedFacebookPage.getName());
        getSupportActionBar().setSubtitle("Upcoming Events");

        // Show the Up (back) button in the action bar.
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

        Bundle arguments = new Bundle();
        arguments.putString(EventListForOrganiserFragment.ARG_FACEBOOKPAGE_ID, selectedFacebookPage.getFacebookID());
        arguments.putString(EventListForOrganiserFragment.ARG_FACEBOOKPAGE_NAME, selectedFacebookPage.getName());
        EventListForOrganiserFragment fragment = new EventListForOrganiserFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.event_list_container, fragment)
                .commit();
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
            // activity, the Up button is shown.
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

    @Override
    public void onFragmentInteraction(Uri uri) {
        //required for ad fragment
    }

    @Override
    public void onCoverRetrieved(String uri) {
        // update with cover photo
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

}

