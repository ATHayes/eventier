package com.athayes.eventier;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
    FacebookPageListAdapter facebookPageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebookpage_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View recyclerView = findViewById(R.id.facebookpage_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
        setTitle("Search...");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_facebook_page_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.search_view);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                facebookPageListAdapter.getFilter().filter(text);
                return true;
            }
        });
        return true;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        ArrayList<FacebookPage> pages = GlobalVariables.getInstance().getFacebookPages();
        Collections.sort(pages);
        facebookPageListAdapter = new FacebookPageListAdapter(pages, this);
        recyclerView.setAdapter(facebookPageListAdapter);
    }

    // Left transition when going back, finish activity

}
