package com.example.newworld;

import java.io.Serializable;

public class ParkingSession implements Serializable {
    private double latitude;
    private double longitude;
    private String createdAt;

    public ParkingSession(double latitude, double longitude, String createdAt) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = createdAt;
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
}
