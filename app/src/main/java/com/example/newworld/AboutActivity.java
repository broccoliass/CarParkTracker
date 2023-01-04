package com.example.newworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Set the text and make the link clickable
        TextView aboutText = findViewById(R.id.about_text);
        aboutText.setText("Learn more about our organization at http://www.example.com");
        Linkify.addLinks(aboutText, Linkify.WEB_URLS);
    }
}
