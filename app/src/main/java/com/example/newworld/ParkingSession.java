package com.example.newworld;

import java.io.Serializable;

public class ParkingSession implements Serializable {
    private double latitude;
    private double longitude;
    private String createdAt;
    private String streetName;

    public ParkingSession(double latitude, double longitude, String createdAt, String streetName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = createdAt;
        this.streetName = streetName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getStreetName() {
        return streetName;
    }
}
