package com.athayes.eventier;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SelectLocationActivity extends AppCompatActivity {

    String[] arraySpinner;
    Spinner locationSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        this.arraySpinner = new String[] {
                "Cork (UCC)", "Toronto"
        };
        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        locationSpinner.setAdapter(adapter);
        Button btnLocation = (Button) findViewById(R.id.btnLocation);

        btnLocation.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getBaseContext().getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.preference_file_key), "Toronto");
                editor.commit();
                System.out.println("-------------" + sharedPref.getString(getString(R.string.preference_file_key), ""));

                Intent intent = new Intent(getBaseContext(), EventListActivity.class);
                startActivity(intent);
            }
        });
    }
}
