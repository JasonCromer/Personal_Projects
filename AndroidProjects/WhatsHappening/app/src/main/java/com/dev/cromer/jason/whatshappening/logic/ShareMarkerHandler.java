package com.dev.cromer.jason.whatshappening.logic;


import android.content.Context;
import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;

public class ShareMarkerHandler {

    Context appContext;
    private static final String ACTION_INTENT_TEXT = "Share it!";
    private static final String SHARE_TEXT_TYPE = "text/plain";

    public ShareMarkerHandler(Context applicationContext){
        this.appContext = applicationContext;
    }

    public void shareMarkerLocation(LatLng latlng, int zoomLevel, String text){
        Intent shareIntent = new Intent();

        //Format our input to provide a url to the shared marker
        String locationURL = formatLocationURL(latlng, zoomLevel);

        //Set as ACTION_SEND to allow user to choose app
        shareIntent.setAction(Intent.ACTION_SEND);

        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        shareIntent.setType(SHARE_TEXT_TYPE);

        //Add our text and url
        shareIntent.putExtra(Intent.EXTRA_TEXT, text + locationURL);

        //Share our activity
        appContext.startActivity(Intent.createChooser(shareIntent, ACTION_INTENT_TEXT));
    }

    private String formatLocationURL(LatLng latlng, int zoomLevel){
        String lat = Double.toString(latlng.latitude);
        String lng = Double.toString(latlng.longitude);
        String urlStart = "http://maps.google.com/maps?q=" + lat + "," + lng;
        String urlEnd = "&z=" + String.valueOf(zoomLevel);

        return urlStart + urlEnd + "\n";
    }
}
