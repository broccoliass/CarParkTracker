package com.example.newworld;

public class Session {
    private int id;
    private double latitude;
    private double longitude;
    private String streetName;
    private String createdAt;

    public Session(int id, double latitude, double longitude, String streetName, String createdAt) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.streetName = streetName;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
