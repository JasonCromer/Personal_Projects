package com.dev.cromer.jason.whatshappening.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.cromer.jason.whatshappening.R;
import com.dev.cromer.jason.whatshappening.logic.MarkerLikesPostRequestParams;
import com.dev.cromer.jason.whatshappening.networking.HttpGetRequest;
import com.dev.cromer.jason.whatshappening.networking.UpdateMarkerLikesHttpPostRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
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
            markerLikesTextView.setText(markerLikes + " likes");
        }
        else{
            markerLikesTextView.setText("0 likes");
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

        //Update likes after submitting like/dislike
        getMarkerLikes();
        displayMarkerLikes();
    }


    @Override
    public void onClick(View v) {
        //Default likes is zero for first time vote case
        final int NUM_VOTES = preferences.getInt("NUM_VOTES", 0);
        Log.d("TAG", String.valueOf(NUM_VOTES));
        //Create sharedpreference editor
        SharedPreferences.Editor editor = preferences.edit();

        if(v == upvoteButton){
            if(NUM_VOTES < 5){
                final String upVoteString = "upVote";
                updateMarkerLikes(upVoteString);
            }
        }
        if(v == downvoteButton){
            if(NUM_VOTES < 5) {
                final String downVoteString = "downVote";
                updateMarkerLikes(downVoteString);
            }
        }

        //Create date stamp and compare for next day
        if(NUM_VOTES >= 5){
            Toast.makeText(this.getApplicationContext(), "Sorry, you've already voted 5 times today", Toast.LENGTH_SHORT).show();
            compareDates(editor);
        }

        //Only increment to 6 (any more is unnecessary as we catch any above 5
        if(NUM_VOTES < 5){
            //add vote to editor
            editor.putInt("NUM_VOTES", NUM_VOTES + 1);
            editor.apply();
        }
    }


    private void compareDates(SharedPreferences.Editor editor){
        //Get old date from storage, and new date
        final String oldDateString = preferences.getString("OLD_DATE", "NONE");
        final String currentDateString = getCurrentDateString();
        final Date oldDate;
        final Date currentDate;

        //Check if old date exists
        if(!oldDateString.equals("NONE")){
            //Convert date Strings to Date objects
            oldDate = parseStringToDate(oldDateString);
            currentDate = parseStringToDate(currentDateString);

            //Compare dates to check if current date is after the old date
            if(currentDate.after(oldDate)){
                //Reset vote count since a day has passed
                editor.putInt("NUM_VOTES", 0);
                editor.apply();
            }
        }
        else{
            //Store current date as old date if first time voting
            editor.putString("OLD_DATE", currentDateString);
            editor.apply();
        }

    }


    private Date parseStringToDate(String dateToParse){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date parsedDate = new Date();
        try{
            parsedDate = dateFormatter.parse(dateToParse);
            return parsedDate;
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        return parsedDate;
    }

    private String getCurrentDateString(){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        return dateFormatter.format(new Date());
    }


}
