package com.athayes.eventier;

// Imports

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class CreateEventActivity extends AppCompatActivity {

    EditText input_event_name;
    EditText input_event_description;
    EditText input_event_date;
    EditText input_event_time;
    EditText input_event_host;
    EditText input_event_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Set up action bar and toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Display back button at top right hand corner of screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set up controls
        input_event_name = (EditText) findViewById((R.id.input_event_name));
        input_event_description = (EditText) findViewById(R.id.input_event_description);
        input_event_date = (EditText) findViewById(R.id.input_event_date);
        input_event_time = (EditText) findViewById(R.id.input_event_time);
        input_event_host = (EditText) findViewById(R.id.input_event_host);
        input_event_location = (EditText) findViewById(R.id.input_event_location);

        Button button_create_event = (Button) findViewById(R.id.button_create_event);

        // Set action bar title
        setTitle("Create Event");

        // Set up calendar
        final Calendar myCalendar = Calendar.getInstance();

        // Set up date picker
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // date formatter.
                 String myFormat = getString(R.string.date_format); // from strings
                 SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                 input_event_date.setText(sdf.format(myCalendar.getTime()));

                // New Joda-time date formatter
                // DateTimeFormatter dtf = DateTimeFormat.forPattern("dd-MM-yyyy");
                // input_event_date.setText(dtf.parseDateTime(myCalendar.getTime());
            }
        };

        // Event handler for event date text field - opens up the calendar dialog
        input_event_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateEventActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        // Event handler for event time edit text - opens the time dialog
        input_event_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        input_event_time.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        // Event listener for create event button
        button_create_event.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                createEvent();
            }
        });
    }

    // Set up the back button at the top left of the screen
    // Code source:
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void createEvent() {

        //TODO Validate the form - use a library

        // Get values from each field
        String eventName = input_event_name.getText().toString();
        String eventDescription = input_event_description.getText().toString();
        String dateTime = input_event_time.getText().toString();
        String eventHost = input_event_host.getText().toString();
        String eventLocation = input_event_location.getText().toString();
        String startTime = "";
        String endTime = "";

//        try {
//            // Try to parse the date from the text input
//            String expectedDateFormat = getString(R.string.date_format); //In which you need put here
//            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
//            eventDate = sdf.parse(input_event_date.getText().toString());
//        } catch (Exception ex){
//            // Date is invalid
//            Context context = getApplicationContext();
//            CharSequence text = "Error in selecting date. Please try again.";
//            int duration = Toast.LENGTH_SHORT;
//            Toast toast = Toast.makeText(context, text, duration);
//            toast.show();
//        }


        // Get list of events from global variable singleton
        List<Event> ITEMS = GlobalVariables.getInstance().getITEMS();

        // Create new event from values in each field
        // Size of the array is 1 larger than it's index - index starts at 0 based while size starts at 1
        // Therefore the next index (id value in Event) is always equal to the current size
        Event event = new Event(Integer.toString(ITEMS.size()), eventName, eventDescription, eventHost, eventLocation, startTime, endTime);

        // Add the new event to the event list
        // Use the addToITEMS method to add to both the List and the Hash Map
        GlobalVariables.getInstance().addToITEMS(event);

        // Notify the user that the event has been created
        Context context = getApplicationContext();
        CharSequence text = "You created an event!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();


        // End this activity and return to the main activity, notifiying it that an event has been created
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }


}
