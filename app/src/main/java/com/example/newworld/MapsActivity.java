package com.example.newworld;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private DatabaseHelper databaseHelper;
    private Button markParkingButton;
    private Button goToAnotherActivityButton;
    private Button endSessionButton;
    private Button viewParkingLocationButton;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Marker currentLocationMarker; // To store the current location marker
    private Marker parkingLocationMarker; // To store the parking location marker
    private boolean parkingLocationMarked = false; // Flag to indicate if parking location is marked





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Create an instance of DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Access the database and perform operations
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        // Perform database operations here

        // Close the database when you're done
        databaseHelper.closeDatabase();

// Button 1: Mark Parking
        markParkingButton = findViewById(R.id.button1);
        markParkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markParkingLocation();
            }
        });

// Button 2: View Recent Parking Location
        goToAnotherActivityButton = findViewById(R.id.button2);
        goToAnotherActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAnotherActivity();
            }
        });

// Button 3: End Session
        endSessionButton = findViewById(R.id.button3);
        endSessionButton.setVisibility(View.GONE); // Initially hidden
        endSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endSession();
            }
        });

// Button 4: View Parking Location
        viewParkingLocationButton = findViewById(R.id.button4);
        viewParkingLocationButton.setVisibility(View.GONE); // Initially hidden
        viewParkingLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement the logic to view the current parking location
                // For example, you can retrieve the location from the database and show it on the map
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check for location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission has already been granted
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations, this can be null.
                    if (location != null) {
                        // Set the map's camera position to the current location
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

                        // Add a marker at the current location if parking location is not marked
                        if (!parkingLocationMarked) {
                            currentLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
                        }
                    }
                }
            });
        }
    }

    private void markParkingLocation() {
        // Display a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to mark the parking location?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the current location
                fusedLocationClient.getLastLocation().addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Check if location is available
                        if (location != null) {
                            // Store the parking location in the offline database
                            SQLiteDatabase db = databaseHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("latitude", location.getLatitude());
                            values.put("longitude", location.getLongitude());
                            long newRowId = db.insert("parking", null, values);
                            db.close();

                            if (newRowId != -1) {
                                // Successful insertion
                                Toast.makeText(MapsActivity.this, "Parking location marked!", Toast.LENGTH_SHORT).show();
                                updateButtonsForSession();

                                // Remove the current location marker
                                if (currentLocationMarker != null) {
                                    currentLocationMarker.setVisible(false);
                                }

                                // Create a custom marker icon with a different color
                                BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);

                                // Add a marker for the parking location with the custom icon
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                parkingLocationMarker = mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title("You parked here")
                                        .icon(icon));

                                parkingLocationMarked = true; // Update the flag to indicate parking location is marked
                            } else {
                                // Failed to insert
                                Toast.makeText(MapsActivity.this, "Failed to mark parking location.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Location is null, unable to mark parking location
                            Toast.makeText(MapsActivity.this, "Unable to mark parking location. Location is null.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }



    private void goToAnotherActivity() {
        // Implement the logic to navigate to another activity here
        // For example, you can use an Intent to start the desired activity

        Toast.makeText(this, "Going to another activity...", Toast.LENGTH_SHORT).show();
    }

    private void endSession() {
        // Display a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to end the parking session?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateButtonsForNoSession();

                // Remove the parking location marker
                if (parkingLocationMarker != null) {
                    parkingLocationMarker.remove();
                    parkingLocationMarker = null;
                }

                // Make the current location marker visible again
                if (currentLocationMarker != null) {
                    currentLocationMarker.setVisible(true);
                }

                parkingLocationMarked = false; // Update the flag to indicate parking location is no longer marked
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }


    private void updateButtonsForSession() {
        markParkingButton.setVisibility(View.GONE);
        goToAnotherActivityButton.setVisibility(View.GONE);
        endSessionButton.setVisibility(View.VISIBLE);
        viewParkingLocationButton.setVisibility(View.VISIBLE);
    }

    private void updateButtonsForNoSession() {
        markParkingButton.setVisibility(View.VISIBLE);
        goToAnotherActivityButton.setVisibility(View.VISIBLE);
        endSessionButton.setVisibility(View.GONE);
        viewParkingLocationButton.setVisibility(View.GONE);
    }
}
