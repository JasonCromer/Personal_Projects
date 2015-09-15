package com.dev.cromer.jason.whatsappening.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.cromer.jason.whatsappening.R;
import com.dev.cromer.jason.whatsappening.logic.MarkerLikesPostRequestParams;
import com.dev.cromer.jason.whatsappening.networking.HttpGetRequest;
import com.dev.cromer.jason.whatsappening.networking.UpdateMarkerLikesHttpPostRequest;

import java.util.concurrent.ExecutionException;

public class MarkerDescriptionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView markerDescriptionTextView;
    private TextView markerLikesTextView;
    private ImageButton upvoteButton;
    private ImageButton downvoteButton;
    private String markerDescription = "";
    private String markerLikes = "";
    static String markerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_description);

        markerLikesTextView = (TextView) findViewById(R.id.markerLikesTextView);
        markerDescriptionTextView = (TextView) findViewById(R.id.markerDescriptionTextView);
        upvoteButton = (ImageButton) findViewById(R.id.upvoteButton);
        downvoteButton = (ImageButton) findViewById(R.id.downvoteButton);

        upvoteButton.setOnClickListener(this);
        downvoteButton.setOnClickListener(this);


        //Get the ID from the marker thats been clicked on, on the map
        Intent thisIntent = getIntent();
        markerID = thisIntent.getStringExtra("MARKER_ID");

        //getMarkerDescription and likes from database via passed in Marker ID
        getMarkerDescription();
        getMarkerLikes();

        //Set marker markerDescription and likes in their textviews
        displayMarkerDescription();
        displayMarkerLikes();
    }


    private void getMarkerDescription(){
        final String url = "http://whatsappeningapi.elasticbeanstalk.com/api/get_marker_description/" + markerID;
        HttpGetRequest httpGetRequest = new HttpGetRequest();

        try{
            markerDescription = httpGetRequest.execute(url).get();

            //Remove quotations from markerDescription
            markerDescription = markerDescription.replace("\"", "");
        }
        catch(ExecutionException | InterruptedException | NullPointerException e){
            e.printStackTrace();
        }
    }


    private void displayMarkerDescription(){
        if(markerDescription != null){
            markerDescriptionTextView.setText(markerDescription);
        }
        else{
            markerDescriptionTextView.setText("");
        }
    }


    private void getMarkerLikes(){
        final String url = "http://whatsappeningapi.elasticbeanstalk.com/api/get_marker_likes/" + markerID;
        HttpGetRequest httpGetRequest = new HttpGetRequest();

        try{
            markerLikes = httpGetRequest.execute(url).get();
        }
        catch(ExecutionException | InterruptedException | NullPointerException e){
            e.printStackTrace();
        }
    }

    private void displayMarkerLikes(){
        if(markerLikes != null){
            markerLikesTextView.setText(markerLikes);
        }
        else{
            markerLikesTextView.setText("0");
        }
    }


    private void updateMarkerLikes(String voteType){
        final String url = "http://whatsappeningapi.elasticbeanstalk.com/api/update_marker_likes/" + markerID;

        //Create an object containing necessary parameters for our post request
        MarkerLikesPostRequestParams params = new MarkerLikesPostRequestParams(url, voteType);

        UpdateMarkerLikesHttpPostRequest postRequest = new UpdateMarkerLikesHttpPostRequest();

        try{
            postRequest.execute(params).get();
        }
        catch (ExecutionException | InterruptedException | NullPointerException e){
            Toast.makeText(getApplicationContext(), "Cannot contact our server! Check your connection.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        if(v == upvoteButton){
            final String upVoteString = "upVote";
            updateMarkerLikes(upVoteString);
        }
        if(v == downvoteButton){
            final String downVoteString = "downVote";
            updateMarkerLikes(downVoteString);
        }

    }
}
