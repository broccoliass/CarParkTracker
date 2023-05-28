package com.example.newworld;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private DatabaseHelper databaseHelper;
    private Button markParkingButton;
    private Button viewRecentSessionButton;
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
        viewRecentSessionButton = findViewById(R.id.button2);
        viewRecentSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewRecentSession();
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
                viewCurrentSession();
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
                            // Perform reverse geocoding to get the address
                            Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                            List<Address> addresses;
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                if (!addresses.isEmpty()) {
                                    Address address = addresses.get(0);
                                    String streetName = address.getAddressLine(0); // Get the street name

                                    // Store the parking location in the local SQLite database
                                    SQLiteDatabase db = databaseHelper.getWritableDatabase();
                                    ContentValues values = new ContentValues();
                                    values.put("latitude", location.getLatitude());
                                    values.put("longitude", location.getLongitude());
                                    values.put("street_name", streetName); // Store the street name
                                    values.put("created_at", getCurrentTimestamp());
                                    long newRowId = db.insert("parking", null, values);
                                    db.close();

                                    if (newRowId != -1) {
                                        // Successful insertion
                                        Toast.makeText(MapsActivity.this, "Parking location marked!", Toast.LENGTH_SHORT).show();
                                        updateButtonsForSession();

                                        // Create a custom marker icon with a different color
                                        BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);

                                        // Add a marker for the parking location with the custom icon
                                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                        parkingLocationMarker = mMap.addMarker(new MarkerOptions()
                                                .position(latLng)
                                                .title("You parked here")
                                                .snippet(streetName) // Set the street name as the marker snippet
                                                .icon(icon));

                                        parkingLocationMarked = true; // Update the flag to indicate parking location is marked

                                        // Send the parking location data to DigitalOcean droplet
                                        sendParkingLocationToDroplet(location.getLatitude(), location.getLongitude(), streetName);
                                    } else {
                                        // Failed to insert
                                        Toast.makeText(MapsActivity.this, "Failed to mark parking location.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // No address found
                                    Toast.makeText(MapsActivity.this, "Unable to retrieve address for the location.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(MapsActivity.this, "Failed to perform reverse geocoding.", Toast.LENGTH_SHORT).show();
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

    // Helper method to get the current timestamp
    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void sendParkingLocationToDroplet(double latitude, double longitude, String streetName) {
        // Create a JSON object to hold the latitude, longitude, and street name values
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("latitude", latitude);
            jsonParams.put("longitude", longitude);
            jsonParams.put("street_name", streetName);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        // Create a request queue using Volley library
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Define the API endpoint URL
        String url = "http://157.230.252.173/parking_api.php";

        // Create a POST request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response from the API
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");

                            // Display a toast message based on the response
                            Toast.makeText(MapsActivity.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Toast.makeText(MapsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the request queue
        requestQueue.add(jsonObjectRequest);
    }

    private void viewCurrentSession() {
        // Get the details of the current session from the database
        ParkingSession currentSession = getCurrentParkingSession();

        // Check if there is a current session
        if (currentSession != null) {
            Intent intent = new Intent(this, ParkingDetailsActivity.class);
            intent.putExtra("parkingSession", currentSession);
            startActivity(intent);
        } else {
            Toast.makeText(this, "No current session found.", Toast.LENGTH_SHORT).show();
        }
    }

    private ParkingSession getCurrentParkingSession() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM parking ORDER BY created_at DESC LIMIT 1", null);

        ParkingSession currentSession = null;
        if (cursor.moveToFirst()) {
            double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
            double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
            String createdAt = cursor.getString(cursor.getColumnIndex("created_at"));

            currentSession = new ParkingSession(latitude, longitude, createdAt);
        }

        cursor.close();
        db.close();

        return currentSession;
    }

    private void viewRecentSession() {
        Intent intent = new Intent(MapsActivity.this, RecentSessionsActivity.class);
        startActivity(intent);
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
        viewRecentSessionButton.setVisibility(View.GONE);
        endSessionButton.setVisibility(View.VISIBLE);
        viewParkingLocationButton.setVisibility(View.VISIBLE);
    }

    private void updateButtonsForNoSession() {
        markParkingButton.setVisibility(View.VISIBLE);
        viewRecentSessionButton.setVisibility(View.VISIBLE);
        endSessionButton.setVisibility(View.GONE);
        viewParkingLocationButton.setVisibility(View.GONE);
    }
}
