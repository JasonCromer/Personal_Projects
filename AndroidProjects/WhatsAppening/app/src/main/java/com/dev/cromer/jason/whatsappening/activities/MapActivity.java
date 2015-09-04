package com.dev.cromer.jason.whatsappening.activities;


import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dev.cromer.jason.whatsappening.logic.LocalMarkers;
import com.dev.cromer.jason.whatsappening.logic.PostRequestParams;
import com.dev.cromer.jason.whatsappening.networking.HttpPostRequest;
import com.dev.cromer.jason.whatsappening.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;


public class MapActivity extends AppCompatActivity implements LocationListener,
                                                                GoogleApiClient.ConnectionCallbacks,
                                                                GoogleApiClient.OnConnectionFailedListener,
                                                                View.OnClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraChangeListener,
                                                                OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener {

    // mMap might be null if Google Play services APK is not available.
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    //Map objects. Nullify objects that must not exist at activity creation
    private Marker temporaryPlacedMarker = null;
    private Marker temporarySearchedMarker = null;
    private CameraPosition lastCameraPosition = null;
    private Marker lastOpenedMarker = null;
    private boolean hasSearched = false;
    private LatLng searchedAddress;

    //constants
    private static final int CAMERA_ZOOM = 18;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final double LAT_LNG_OFFSET = 1.5;
    private static final int POST_NEW_MARKER_REQ_CODE = 0;
    private static final int SEARCH_PLACE_REQ_CODE = 1;
    private static final int WAIT_IN_MILLISECONDS = 500;

    //Hashmap for storing local, non-duplicate local markers
    private HashMap<MarkerOptions, Integer> postableMarkersHashMap = new HashMap<>();

    //EditText search bar
    private static EditText searchBarEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Set up the action bar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.show();
        }

        searchBarEditText = (EditText) findViewById(R.id.searchBarEditText);
        searchBarEditText.setOnClickListener(this);


        //Setup the autocomplete feature and googleApiClient
        setUpGoogleApiClient();

        //Instantiate the map
        ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }



    //This should only be called once and when we are sure that mMap is not null.
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.isBuildingsEnabled();

        //For adding new markers
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);

        mMap.setOnMapLongClickListener(this);
        mMap.setOnCameraChangeListener(this);
    }


    private void setUpGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .build();
    }


    protected void showLocation(Location mCurrentLocation) {
        if (mCurrentLocation != null) {
            getNearbyMarkers(mCurrentLocation);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())
                    , CAMERA_ZOOM));
        }
    }

    
    protected void startLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }


    @Override
    public void onClick(View v) {
        if(v == searchBarEditText){
            //Start intent for user to search a place
            Intent searchIntent = new Intent(this, SearchPlaceActivity.class);
            startActivityForResult(searchIntent, SEARCH_PLACE_REQ_CODE);
        }
    }


    /* This function sets a marker at a location where the user has pressed and held down
        on the map.
     */
    protected void setMarker(String markerTitle) {
        String markerLatitude = "";
        String markerLongitude = "";
        final String postRequestURL = "http://10.0.2.2:5000/api/add_marker";
        boolean hasLocation = false;

        //Give centered marker first priority
        if(temporaryPlacedMarker != null){
            LatLng markerLocation = temporaryPlacedMarker.getPosition();
            markerLatitude = String.valueOf(markerLocation.latitude);
            markerLongitude = String.valueOf(markerLocation.longitude);
            hasLocation = true;
        }
        else{
            Toast.makeText(getApplicationContext(), "Sorry, we can't get your location", Toast.LENGTH_SHORT).show();
        }

        if(hasLocation) {
            //Create params object holding all the data and a new postRequest object
            PostRequestParams params = new PostRequestParams(postRequestURL, markerLatitude, markerLongitude, markerTitle.replace(","," "));
            HttpPostRequest postRequest = new HttpPostRequest();

            //Post the marker and Toast the user a confirmation
            try{
                String receivedData = postRequest.execute(params).get();
                if(receivedData.equals("Marker Posted!")){
                    Toast.makeText(getApplicationContext(), receivedData, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Sorry, the database is sleeping!", Toast.LENGTH_SHORT).show();
                }

            }
            catch (ExecutionException | InterruptedException | NullPointerException e) {
                e.printStackTrace();
            }

            //After posting, refresh area that user posted in and delete the temporary marker
            final Location postMarkerLocation = new Location("Post-Marker location");
            postMarkerLocation.setLatitude(Double.valueOf(markerLatitude));
            postMarkerLocation.setLongitude(Double.valueOf(markerLongitude));
            getNearbyMarkers(postMarkerLocation);
        }
    }



    public void getNearbyMarkers(Location mCurrentLocation) {
        HashMap<MarkerOptions, Integer> currentLocalMarkersHashMap;

        //Set up nearby markers
        LocalMarkers localMarkers = new LocalMarkers(mCurrentLocation, mMap);
        //Retrieve local markers from database
        localMarkers.retrieveLocalMarkers();

        // display local markers from other users
        currentLocalMarkersHashMap = localMarkers.getLocalMarkersList();
        compareAndMapLocalMarkers(currentLocalMarkersHashMap);
    }


    public void compareAndMapLocalMarkers(HashMap<MarkerOptions, Integer> currentLocalHashMap) {
        if(!currentLocalHashMap.isEmpty()){
            Iterator iterator = currentLocalHashMap.entrySet().iterator();
            while(iterator.hasNext()){

                @SuppressWarnings("unchecked")
                HashMap.Entry<MarkerOptions, Integer> pair = (HashMap.Entry<MarkerOptions, Integer>)iterator.next();
                final Integer currentVal = Integer.parseInt(String.valueOf(pair.getValue()));

                //if GET markers don't show up in our postable hashmap, add them
                if(!postableMarkersHashMap.containsValue(currentVal)){
                    postableMarkersHashMap.put(pair.getKey(), pair.getValue());
                    mMap.addMarker(pair.getKey());
                }
                //Otherwise, delete them from the map
                else{
                    mMap.addMarker(pair.getKey()).remove();
                }
                iterator.remove();
            }
        }
        //If no markers in local area, clear the current list of postable markers
        else{
            postableMarkersHashMap.clear();
        }

    }


    private void delayTitleIntent(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent resultIntent = new Intent(getApplicationContext(), PostNewMarkerActivity.class);
                startActivityForResult(resultIntent, POST_NEW_MARKER_REQ_CODE);

            }
        }, WAIT_IN_MILLISECONDS);
    }


    /*
        Override Methods
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(true);
        setUpMap();
    }


    @Override
    public void onLocationChanged(Location location) {
        showLocation(location);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stop location updates to conserve battery
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Resume location updates
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }



    @Override
    public void onMapLongClick(LatLng latLng) {
        //Add temporary marker so user knows they're posting one in long-clicked location
        temporaryPlacedMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        //Give a slight delay between intent and posted marker so user can see where they're posting
        delayTitleIntent();
    }


    @Override
    public void onMapClick(LatLng latLng) {
        //Remove searched Marker
        if(temporarySearchedMarker != null){
            temporarySearchedMarker.remove();
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        if(lastOpenedMarker != null) {

            //is the marker already open
            if (lastOpenedMarker.equals(marker)) {
                //if so, nullify it
                lastOpenedMarker = null;
                //return true so it doesn't open again
                return true;
            }
        }
        if(!marker.isDraggable() && !marker.isFlat()){
            marker.setAlpha(.4f);
        }

        marker.showInfoWindow();
        lastOpenedMarker = marker;

        return false;
    }


    //This function refreshes the locally posted markers based on camera coordinates
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        final Location currentCenteredScreenLocation = new Location("CenteredScreenLocation");

        //Get center of map
        LatLng center = mMap.getCameraPosition().target;

        //Get current centered location
        currentCenteredScreenLocation.setLatitude(center.latitude);
        currentCenteredScreenLocation.setLongitude(center.longitude);


        //Compare previous center marker with current one
        final double camLat = center.latitude;
        final double camLng = center.longitude;

        if(lastCameraPosition != null) {
            final double latUpperBound = lastCameraPosition.target.latitude+ LAT_LNG_OFFSET;
            final double latLowerBound = lastCameraPosition.target.latitude- LAT_LNG_OFFSET;
            final double lngUpperBound = lastCameraPosition.target.longitude+ LAT_LNG_OFFSET;
            final double lngLowerBound = lastCameraPosition.target.longitude- LAT_LNG_OFFSET;

            //If camera moves within bounds, attempt to retrieve and update new markers in local area
            if((latLowerBound <= camLat && camLat <= latUpperBound) && (lngLowerBound <= camLng && camLng <= lngUpperBound)){
                getNearbyMarkers(currentCenteredScreenLocation);
            }
            //If camera moves out of bounds then get new markers and clear map and postable markers list
            else{
                mMap.clear();
                postableMarkersHashMap.clear();
                getNearbyMarkers(currentCenteredScreenLocation);
            }
        }

        //If location has been searched, drop a pin at that location
        if(hasSearched){
            dropSearchedLocationPin();
        }
        lastCameraPosition = cameraPosition;
    }


    //This returns data from a previously opened activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case(POST_NEW_MARKER_REQ_CODE):
                //remove temporary marker because a new one will be placed with the title
                temporaryPlacedMarker.remove();

                if(resultCode == Activity.RESULT_OK){
                    //Get title inputted by user in previous activity
                    final String newMarkerTitle = data.getStringExtra("TITLE");
                    setMarker(newMarkerTitle);
                }
                break;
            case(SEARCH_PLACE_REQ_CODE):
                if(resultCode == Activity.RESULT_OK) {
                    searchedAddress = data.getParcelableExtra("SEARCHED_LOCATION");
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(searchedAddress, CAMERA_ZOOM));
                    hasSearched = true;
                }
                break;
        }
    }

    private void dropSearchedLocationPin(){
        temporarySearchedMarker = mMap.addMarker(new MarkerOptions().position(searchedAddress)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).flat(true));
        hasSearched = false;
    }
}
