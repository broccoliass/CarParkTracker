package com.example.newworld;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private TextView loginInfoTextView;
    private Button mapsButton;
    private Button aboutButton;
    private Button logoutButton;
    private Button loginButton;
    private LinearLayout buttonsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loginInfoTextView = findViewById(R.id.login_info_text_view);
        mapsButton = findViewById(R.id.maps_button);
        aboutButton = findViewById(R.id.about_button);
        logoutButton = findViewById(R.id.logout_button);
        loginButton = findViewById(R.id.login_button);
        buttonsLayout = findViewById(R.id.buttons_layout);

        // Check if the user is logged in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is logged in, show the login information and the maps and about buttons and the logout button
            String email = user.getEmail();
            loginInfoTextView.setText("Logged in as: " + email);
            loginButton.setVisibility(View.GONE);
            buttonsLayout.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
        } else {
            // User is not signed in
            // Display the login button
            loginInfoTextView.setText("Not logged in.");
            loginButton.setVisibility(View.VISIBLE);
            buttonsLayout.setVisibility(View.GONE);
            logoutButton.setVisibility(View.GONE);
        }

        ImageView imageView = findViewById(R.id.profile_image);

        // Decode the image file into a Bitmap
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile_picture);

        // Resize the bitmap
        int newWidth = 300;
        int newHeight = 300;
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);

        // Set the ImageView to display the resized bitmap
        imageView.setImageBitmap(resizedBitmap);


        // Prevent the user from going back to the login screen by pressing the back button
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        // Set up the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });

        // Set up the maps button
        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MapsActivity.class));
            }
        });

        // Set up the about button
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AboutActivity.class));
            }
        });

        // Set up the logout
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                finish();
            }
        });
    }
}