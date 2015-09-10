package com.dev.cromer.jason.whatsappening.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.dev.cromer.jason.whatsappening.R;
import com.dev.cromer.jason.whatsappening.networking.HttpGetRequest;

import java.util.concurrent.ExecutionException;

public class MarkerDescriptionActivity extends AppCompatActivity {

    private TextView markerDescription;
    private String markerID;
    private String description = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_description);

        markerDescription = (TextView) findViewById(R.id.markerDescriptionTextView);

        Intent thisIntent = getIntent();
        markerID = thisIntent.getStringExtra("MARKER_ID");

        //getMarkerDescription from database via passed in Marker ID
        getMarkerDescription(markerID);

        //Set marker description in the textview
        displayMarkerDescription();
    }


    private void getMarkerDescription(String markerIDString){
        final String url = "http://whatsappeningapi.elasticbeanstalk.com/api/get_marker_description/" + markerIDString;
        HttpGetRequest httpGetRequest = new HttpGetRequest();

        try{
            description = httpGetRequest.execute(url).get();
            description = description.replace("\"", "");
        }
        catch(ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
    }


    private void displayMarkerDescription(){
        if(description != null){
            markerDescription.setText(description);
        }
        else{
            markerDescription.setText("");
        }
    }

}
