package com.example.newworld;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import com.example.newworld.R;

public class SessionAdapter extends ArrayAdapter<Session> {
    private ArrayList<Session> sessionList;
    private LayoutInflater inflater;

    public SessionAdapter(Context context, ArrayList<Session> sessionList) {
        super(context, 0, sessionList);
        this.sessionList = sessionList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = inflater.inflate(R.layout.session_item, parent, false);
        }

        Session currentSession = sessionList.get(position);

        //TextView idTextView = itemView.findViewById(R.id.session_id_textview);
        TextView locationTextView = itemView.findViewById(R.id.location_textview);
        TextView createdAtTextView = itemView.findViewById(R.id.created_at_textview);

        //idTextView.setText(String.valueOf(currentSession.getId()));
        locationTextView.setText(currentSession.getStreetName());
        createdAtTextView.setText(currentSession.getCreatedAt());

        return itemView;
    }
}
