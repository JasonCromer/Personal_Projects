package com.dev.cromer.jason.takeastand.Logic;


import android.content.Context;
import android.content.Intent;

public class TwitterPostHandler {

    Context appContext;
    private static final String ACTION_INTENT_TEXT = "Tweet it!";
    private static final String TWEET_TEXT_TYPE = "text/plain";

    public TwitterPostHandler(Context applicationContext){
        this.appContext = applicationContext;
    }

    public void postTweet(String tweetText){
        Intent tweetIntent = new Intent();

        //Set as ACTION_SEND to allow user to choose app
        tweetIntent.setAction(Intent.ACTION_SEND);

        tweetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        tweetIntent.setType(TWEET_TEXT_TYPE);

        //Add our passed in tweet string
        tweetIntent.putExtra(Intent.EXTRA_TEXT, tweetText);

        //Start our activity
        appContext.startActivity(Intent.createChooser(tweetIntent, ACTION_INTENT_TEXT));

    }
}
