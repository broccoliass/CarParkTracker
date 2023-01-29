package com.example.newworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setupHyperlink();

        // Copyright
        TextView copyrighttext = findViewById(R.id.copyright_text);
        copyrighttext.setText("Copyright © HospitalFinder Inc. All rights reserved.");
        Linkify.addLinks(copyrighttext, Linkify.WEB_URLS);
        copyrighttext.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
        copyrighttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Copyright © HospitalFinder Inc. All rights reserved.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setupHyperlink() {
        TextView linkTextView = findViewById(R.id.activity_main_link);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }


}