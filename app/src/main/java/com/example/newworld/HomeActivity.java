package com.example.newworld;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView loginInfoTextView;
    private Button logoutButton;
    private Button loginButton;
    private ImageView imageView;
    private TextView welcomemessage;
    private GridLayout gridLayout;
    private CardView mapsButton, aboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loginInfoTextView = findViewById(R.id.login_info_text_view);
        mapsButton = findViewById(R.id.maps_button);
        aboutButton = findViewById(R.id.about_button);
        logoutButton = findViewById(R.id.logout_button);
        loginButton = findViewById(R.id.login_button);
        imageView = findViewById(R.id.profile_picture);
        welcomemessage = findViewById(R.id.textView2);
        gridLayout = findViewById(R.id.grid_layout);

        mapsButton.setOnClickListener(this);
        aboutButton.setOnClickListener(this);

        // Check if the user is logged in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is logged in, show the login information and the maps and about buttons and the logout button
            String email = user.getEmail();
            loginInfoTextView.setText("Logged in as: " + email);
            loginButton.setVisibility(View.GONE);
            gridLayout.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
            welcomemessage.setText("So you have logged in. You can click the maps button to see your current location and nearby hospital or click about us button to see information about us or click log out button to log out.");

        } else {
            // User is not signed in
            // Display the login button
            loginButton.setVisibility(View.VISIBLE);
            gridLayout.setVisibility(View.GONE);
            logoutButton.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
        }

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

        // Set up the logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.maps_button:
                startActivity(new Intent(HomeActivity.this, MapsActivity.class));
                break;
            case R.id.about_button:
                startActivity(new Intent(HomeActivity.this, AboutActivity.class));
                break;
        }
    }
}