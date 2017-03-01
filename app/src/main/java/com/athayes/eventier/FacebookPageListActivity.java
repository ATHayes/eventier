package com.athayes.eventier;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.athayes.eventier.adapters.FacebookPageListAdapter;
import com.athayes.eventier.models.FacebookPage;

import java.util.ArrayList;
import java.util.Collections;


public class FacebookPageListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebookpage_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        View recyclerView = findViewById(R.id.facebookpage_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.facebookpage_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        setTitle("Choose an Organiser");
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        ArrayList<FacebookPage> pages = GlobalVariables.getInstance().getFacebookPages();
        Collections.sort(pages);
        recyclerView.setAdapter(new FacebookPageListAdapter(pages));
    }
}
