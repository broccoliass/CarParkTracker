package com.example.newworld;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "parking.db";
    private static final int DATABASE_VERSION = 2; // Update the database version

    // Table name and column names
    private static final String TABLE_PARKING = "parking";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_STREET_NAME = "street_name";
    private static final String COLUMN_CREATED_AT = "created_at";

    // Add a member variable to keep the reference to the writable database
    private SQLiteDatabase database;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the "parking" table with the new column
        String createTableQuery = "CREATE TABLE " + TABLE_PARKING + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_LATITUDE + " REAL," +
                COLUMN_LONGITUDE + " REAL," +
                COLUMN_STREET_NAME + " TEXT," + // Add the new column
                COLUMN_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
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
