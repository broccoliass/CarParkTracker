package com.example.newworld;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class RecentSessionsActivity extends AppCompatActivity {
    private ArrayList<Session> sessionList;
    private SessionAdapter sessionAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_sessions);

        sessionList = new ArrayList<>();
        sessionAdapter = new SessionAdapter(this, sessionList);
        listView = findViewById(R.id.listView);
        listView.setAdapter(sessionAdapter);

        viewRecentSession();
    }

    private void viewRecentSession() {
        // Create a request queue using Volley library
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Define the API endpoint URL on DigitalOcean droplet
        String url = "http://157.230.252.173:8080/parking_api.php";

        // Create a GET request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Process the JSON response containing the parking sessions
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject sessionObj = response.getJSONObject(i);
                                int sessionId = sessionObj.getInt("id");
                                double latitude = sessionObj.getDouble("latitude");
                                double longitude = sessionObj.getDouble("longitude");
                                String streetName = sessionObj.getString("street_name");
                                String createdAt = sessionObj.getString("created_at");

                                // Create a Session object and add it to the sessionList
                                Session session = new Session(sessionId, latitude, longitude, streetName, createdAt);
                                sessionList.add(session);
                            }

                            // Notify the adapter that the data has changed
                            sessionAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Log.e("Volley Error", "Error: " + error.getMessage());
                        Toast.makeText(RecentSessionsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        // Add the request to the request queue
        requestQueue.add(jsonArrayRequest);
    }
}
