package com.dev.cromer.jason.whatshappening.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.cromer.jason.whatshappening.R;
import com.dev.cromer.jason.whatshappening.logic.DailyVoteHandler;
import com.dev.cromer.jason.whatshappening.logic.MarkerLikesPostRequestParams;
import com.dev.cromer.jason.whatshappening.networking.HttpGetRequest;
import com.dev.cromer.jason.whatshappening.networking.UpdateMarkerLikesHttpPostRequest;

import java.util.concurrent.ExecutionException;

public class MarkerDescriptionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView markerDescriptionTextView;
    private TextView markerLikesTextView;
    private ImageButton upvoteButton;
    private ImageButton downvoteButton;
    private String markerDescription = "";
    private String markerLikes = "";
    private static String markerID;
    private static SharedPreferences preferences;

    //constants
    private static final int DEFAULT_LIKES = 0;
    private static final String NUM_VOTES_PREFERENCE = "NUM_VOTES";
    private static final String OLD_DATE_PREFERENCE = "OLD_DATE";
    private static final String NO_OLD_DATE_AVAILABLE = "NONE";
    private static final String UPVOTE_STRING = "upVote";
    private static final String DOWNVOTE_STRING = "downVote";
    private static final String GET_LIKES_ENDPOINT = "http://whatsappeningapi.elasticbeanstalk.com/api/get_marker_likes/";
    private static final String GET_DESCRIPTION_ENDPOINT = "http://whatsappeningapi.elasticbeanstalk.com/api/get_marker_description/";
    private static final String UPDATE_LIKES_ENDPOINT = "http://whatsappeningapi.elasticbeanstalk.com/api/update_marker_likes/";

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

        //Get the shared preference for amount of votes made by the user
        preferences = this.getPreferences(Context.MODE_PRIVATE);


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
        final String url = GET_DESCRIPTION_ENDPOINT + markerID;
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
        final String url = GET_LIKES_ENDPOINT + markerID;
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
            markerLikesTextView.setText(markerLikes + " likes");
        }
        else{
            markerLikesTextView.setText("0 likes");
        }
    }


    private void updateMarkerLikes(String voteType){
        final String url = UPDATE_LIKES_ENDPOINT + markerID;

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

        //Update likes after submitting like/dislike
        getMarkerLikes();
        displayMarkerLikes();
    }


    @Override
    public void onClick(View v) {
        //Create daily vote handler
        DailyVoteHandler dailyVoteHandler = new DailyVoteHandler(preferences);

        //Check if it is a new day, if so, then NUM_VOTES is reset to 0
        final String oldDateString = preferences.getString(OLD_DATE_PREFERENCE, NO_OLD_DATE_AVAILABLE);
        dailyVoteHandler.checkIfNewDay(oldDateString);

        //Get number of votes. Default likes is set to zero for first time vote case
        final int NUM_VOTES = preferences.getInt(NUM_VOTES_PREFERENCE, DEFAULT_LIKES);

        if(v == upvoteButton && NUM_VOTES < 5){
            incrementNumberOfVotes(NUM_VOTES);
            updateMarkerLikes(UPVOTE_STRING);
        }
        if(v == downvoteButton && NUM_VOTES < 5){
            incrementNumberOfVotes(NUM_VOTES);
            updateMarkerLikes(DOWNVOTE_STRING);
        }

        //Create date stamp to compare for future date checks
        if(NUM_VOTES >= 5){
            Toast.makeText(this.getApplicationContext(), "Sorry, you've already voted 5 times today", Toast.LENGTH_SHORT).show();
            dailyVoteHandler.storeOldDateInPreferences();
        }
    }


    private void incrementNumberOfVotes(int NUM_VOTES){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(NUM_VOTES_PREFERENCE, NUM_VOTES + 1);
        editor.apply();
    }



    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
