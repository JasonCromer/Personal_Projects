package com.dev.cromer.jason.whatshappening.logic;


import android.location.Location;

import com.dev.cromer.jason.whatshappening.networking.HttpGetRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LocalMarkersHandler {

    Location markerLatLngLocation;
    GoogleMap mMap;
    List<String> markerItemsList = Collections.emptyList();
    HashMap<MarkerOptions, Integer> markers = new HashMap<>();
    private static final String snippetText = "Click for info";
    private static final String GET_MARKERS_ENDPOINT = "http://whatsappeningapi.elasticbeanstalk.com/api/get_markers/";

    public LocalMarkersHandler(Location markerLatLngLocation, GoogleMap mMap) {
        this.markerLatLngLocation = markerLatLngLocation;
        this.mMap = mMap;
    }


    public void retrieveLocalMarkers() {

        //url to endpoint containing user's local latitude and longitude
        final String url = GET_MARKERS_ENDPOINT + String.valueOf(markerLatLngLocation.getLatitude())+
                String.valueOf("/"+markerLatLngLocation.getLongitude());


        try{
            HttpGetRequest getRequest = new HttpGetRequest();

            //Returned data from API as String-list, i.e. [[item1, item2, item3, item4]]
            String receivedData = getRequest.execute(url).get();
            //replace brackets and quotations
            if(receivedData != null) {
                receivedData = receivedData.replace("[", "");
                receivedData = receivedData.replace("]", "");
                receivedData = receivedData.replace("\"", "");
                //filter out whitespace and turn into List
                markerItemsList = Arrays.asList(receivedData.split("\\s*,\\s*"));
            }

        }
        catch (ExecutionException | InterruptedException | NullPointerException e) {
            e.printStackTrace();
        }
    }


    public HashMap<MarkerOptions, Integer> getLocalMarkersList() {
        /*
            List includes a pattern of: [latitude, longitude, Title, ID, latitude, long...]
            so we must assign values based on chunks of three, then iterate by 3.
         */

        //If size is not greater than 3, its not a valid list
        if(markerItemsList.size() > 3) {
            for(int i = 0; i < markerItemsList.size(); i+=4) {
                final String thisLatitude = markerItemsList.get(i);
                final String thisLongitude = markerItemsList.get(i + 1);
                final String thisTitle = markerItemsList.get(i + 2);
                final String thisId = markerItemsList.get(i + 3);

                final MarkerOptions currentMarker = new MarkerOptions().position(new LatLng(Float.valueOf(thisLatitude),
                        Float.valueOf(thisLongitude))).snippet(snippetText).title(thisTitle).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

                //Add marker to HashMap
                markers.put(currentMarker, Integer.parseInt(thisId));
            }

            return markers;
        }

        else{
            //return empty HashMap
            return markers;
        }
    }



}
