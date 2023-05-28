package com.example.newworld;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import androidx.appcompat.app.AppCompatActivity;

public class ParkingDetailsActivity extends AppCompatActivity {

    private TextView textViewTime;
    private TextView textViewStreetName;
    private TextView textViewDateTime;
    private Button buttonDirections;
    private Button buttonShowMap;

    private ParkingSession parkingSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_details);

        textViewTime = findViewById(R.id.textViewTime);
        textViewStreetName = findViewById(R.id.textViewStreetName);
        buttonDirections = findViewById(R.id.buttonDirections);
        buttonShowMap = findViewById(R.id.buttonShowMap);
        textViewDateTime = findViewById(R.id.textViewDateTime);

        // Retrieve the ParkingSession object from the intent
        Intent intent = getIntent();
        if (intent.hasExtra("parkingSession")) {
            parkingSession = (ParkingSession) intent.getSerializableExtra("parkingSession");
        }

        if (parkingSession != null) {
            // Set the saved time
            String timeAgo = getTimeAgo(parkingSession.getCreatedAt());
            textViewTime.setText("Saved " + timeAgo);

            // Set the street name
            textViewStreetName.setText("Near " + parkingSession.getStreetName());

            // Set the date and time
            String formattedDateTime = getFormattedDateTime(parkingSession.getCreatedAt());
            textViewDateTime.setText(formattedDateTime);
        }


        buttonDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDirections();
            }
        });

        buttonShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapActivity();
            }
        });
    }

    private String getTimeAgo(String createdAt) {
        long timeInMillis = convertDateStringToMillis(createdAt);
        long currentTimeInMillis = System.currentTimeMillis();
        return DateUtils.getRelativeTimeSpanString(timeInMillis, currentTimeInMillis, DateUtils.MINUTE_IN_MILLIS).toString();
    }

    private long convertDateStringToMillis(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date date = format.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String getFormattedDateTime(String createdAt) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a, d MMM yyyy", Locale.getDefault());
        try {
            Date date = inputFormat.parse(createdAt);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void openDirections() {
        if (parkingSession != null) {
            double latitude = parkingSession.getLatitude();
            double longitude = parkingSession.getLongitude();
            String uri = "google.navigation:q=" + latitude + "," + longitude;
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
    }

    private void openMapActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        finish();
    }


}
