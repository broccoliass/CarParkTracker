package com.example.newworld;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "parking.db";
    private static final int DATABASE_VERSION = 1;

    // Add a member variable to keep the reference to the writable database
    private SQLiteDatabase database;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the "parking" table
        String createTableQuery = "CREATE TABLE parking (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "latitude REAL," +
                "longitude REAL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement the upgrade logic if needed
    }

    // Override the getWritableDatabase method to store the reference to the writable database
    @Override
    public SQLiteDatabase getWritableDatabase() {
        database = super.getWritableDatabase();
        return database;
    }

    // Add a method to close the database
    public void closeDatabase() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}
