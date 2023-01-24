package com.example.newworld;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private List<Hospital> hospitals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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
            // Permission has already been granted, get the last known location
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Add a marker at the current location
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
                                // Set the map's camera position to the current location
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                                // Add markers for all hospitals
                                List<Hospital> hospitals = new ArrayList<>();
                                hospitals.add(new Hospital("SALAM Shah Alam Specialist Hospital", 3.0492016615573934, 101.53527684342718));
                                hospitals.add(new Hospital("KPJ Selangor Specialist Hospital", 3.0572073636360484, 101.54155398688032));
                                hospitals.add(new Hospital("Avisena Specialist Hospital", 3.071730873001622, 101.52379800593825));
                                hospitals.add(new Hospital("IJN Selgate Hospital", 3.072049865706741, 101.52274331870775));
                                hospitals.add(new Hospital("Shah Alam Hospital", 3.0712086366663938, 101.49010267514724));
                                hospitals.add(new Hospital("Columbia Asia Extended Care Hospital", 3.04732010815699, 101.5050694270794));
                                hospitals.add(new Hospital("Hospital Umra", 3.082885370653288, 101.53994568168024));
                                hospitals.add(new Hospital("MSU Medical Centre", 3.0767693928998194, 101.55276231016727));
                                hospitals.add(new Hospital("Subang Jaya Medical Centre (SJMC)", 3.079869683027289, 101.59387133435948));
                                hospitals.add(new Hospital("Sunway Medical Centre", 3.0660111943579618, 101.60826685019812));
                                hospitals.add(new Hospital("Assunta Hospital PJ", 3.0934261775948544, 101.64584510747876));
                                hospitals.add(new Hospital("Tengku Ampuan Rahimah Hospital, Klang", 3.020067261297631, 101.44008647610751));
                                hospitals.add(new Hospital("KPJ Klang Specialist Hospital", 3.0622598646169727, 101.46327274490585));
                                hospitals.add(new Hospital("Sentosa Specialist Hospital Klang", 3.0057591943679234, 101.48292616212605));
                                hospitals.add(new Hospital("Bukit Tinggi Medical Centre", 3.010055079177547, 101.43184866399302));
                                // and so on ...
                                for (Hospital hospital : hospitals) {
                                    LatLng hospitalLatLng = new LatLng(hospital.getLatitude(), hospital.getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(hospitalLatLng).title(hospital.getName()));
                                }
                            }
                        }
                    })
            ;}
    }



}