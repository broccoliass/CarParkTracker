package com.example.newworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ParkingDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_details);

        // Retrieve the parking session object from the intent
        ParkingSession parkingSession = (ParkingSession) getIntent().getSerializableExtra("parkingSession");

        // Display the details in the activity layout
        TextView latitudeTextView = findViewById(R.id.latitudeTextView);
        TextView longitudeTextView = findViewById(R.id.longitudeTextView);
        TextView createdAtTextView = findViewById(R.id.createdAtTextView);

        latitudeTextView.setText(String.valueOf(parkingSession.getLatitude()));
        longitudeTextView.setText(String.valueOf(parkingSession.getLongitude()));
        createdAtTextView.setText(parkingSession.getCreatedAt());
    }
}

