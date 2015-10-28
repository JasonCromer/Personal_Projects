package com.dev.cromer.jason.takeastand.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dev.cromer.jason.takeastand.Logic.BounceMarkerHandler;
import com.dev.cromer.jason.takeastand.Logic.PostUserMarkerHandler;
import com.dev.cromer.jason.takeastand.Logic.RetrieveAllMarkersHandler;
import com.dev.cromer.jason.takeastand.Logic.SharePostHandler;
import com.dev.cromer.jason.takeastand.Logic.UpdateNumUsersHandler;
import com.dev.cromer.jason.takeastand.R;
import com.dev.cromer.jason.takeastand.networking.GenericHttpGetRequest;
import com.dev.cromer.jason.takeastand.networking.HttpMarkerPostRequest;
import com.dev.cromer.jason.takeastand.objects.MarkerPostRequestParams;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
                                                                GoogleApiClient.OnConnectionFailedListener, LocationListener,
                                                                GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    protected Intent mapsIntent;
    protected HashMap<MarkerOptions, Integer> markersHashMap;
    private Marker lastOpenedMarker;
    private boolean initialLocationShown = false;


    //Constants
    private static final String UPDATE_NUM_USERS_URL = "http://takeastandapi.elasticbeanstalk.com/update_num_users";
    private static final String POST_MARKER_URL = "http://takeastandapi.elasticbeanstalk.com/add_marker";
    private static final String GET_MARKERS_URL = "http://takeastandapi.elasticbeanstalk.com/get_markers";
    private static final String USER_RELIGION_CHOICE_EXTRA = "USER_CHOICE_EXTRA";
    private static final String DEFAULT_TWEET_TEXT = "I just voiced my beliefs on the Take A Stand app! " +
            "Download the app and stand strong with others across the globe.";
    private static final String SHARED_PREFS_TITLE = "FIRST_TIME_USER";
    private static final boolean SHARED_PREFS_DEFAULT = true;
    private static final boolean SHARED_PREFS_FIRST_TIME_BOOLEAN = false;
    private static final int LOCATION_REQUEST_INTERVAL_MILLISECONDS = 10000;
    private static final int FASTEST_LOCATION_REQUEST_INTERVAL_MILLISECONDS = 5000;
    private static final float CAMERA_ZOOM = 3;
    private static final int RETURN_FAILED = -1;
    private static final int RETURN_SUCCESS = 0;
    private static final int INT_AGNOSTIC = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setUpGoogleApiClient();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    private void setUpGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false); //disable UI features
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMarkerClickListener(this);
    }



    private void startLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_REQUEST_INTERVAL_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_LOCATION_REQUEST_INTERVAL_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    //This method shows our current location only once each time the user opens the app
    private void showLocation(Location myLocation){

        if(myLocation != null && mMap != null){

            //Check if our user is a first time user
            postMarkerIfFirstTimeUser(myLocation);

            if(!initialLocationShown){
                initialLocationShown = true;
            }

            //Small focus zoom on user's location
            LatLng latlngLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlngLocation, CAMERA_ZOOM));

            //Clear any previously mapped markers
            mMap.clear();

            //Map all markers on Map
            retrieveAndMapMarkers();
        }
        else{
            Toast.makeText(this, "Looks like we've ran into some network issues!", Toast.LENGTH_SHORT).show();
        }
    }

    private void postMarkerIfFirstTimeUser(Location myLocation){
        final int result;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final Boolean isFirstTimeUser = preferences.getBoolean(SHARED_PREFS_TITLE, SHARED_PREFS_DEFAULT);

        if(isFirstTimeUser){
            //post user's marker to database
            result = postUserMarker(myLocation);

            //Handle result in case marker could not be posted
            processPostRequestResult(result);

            //Save in storage that the user is no longer a first time user
            saveFirstTimeUserInfo();

            //Update global user count in database
            updateNumberOfUsers();
        }
    }

    private void saveFirstTimeUserInfo(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.edit().putBoolean(SHARED_PREFS_TITLE, SHARED_PREFS_FIRST_TIME_BOOLEAN).apply();
    }

    private void updateNumberOfUsers(){
        //Create request and handler objects
        final GenericHttpGetRequest httpGetRequest = new GenericHttpGetRequest();
        final UpdateNumUsersHandler numUsersHandler = new UpdateNumUsersHandler(UPDATE_NUM_USERS_URL, httpGetRequest);

        //Update the number of users in the database
        numUsersHandler.updateNumberOfUsers();
    }


    private int getUserReligionID(){
            /*
        This method retrieves the integer associated with the user's religion.
        The integers correspond to the position on the spinner that the user chooses from, i.e.:
        1 = Christian
        2 = Islam
        3 = Catholic
        ..and so on.
        The default is set to Agnostic in the event that the extra did not go through,
        as Agnostic is a more neutral viewpoint.
     */
        int userReligionID;

        mapsIntent = getIntent();
        userReligionID = mapsIntent.getIntExtra(USER_RELIGION_CHOICE_EXTRA, INT_AGNOSTIC);

        return userReligionID;
    }

    private int postUserMarker(Location myLocation){
        final int result;

        if(myLocation != null){
            Log.d("TAG............", "POSTED MARKER");
            //Populate the params object
            MarkerPostRequestParams markerParams = new MarkerPostRequestParams(POST_MARKER_URL,
                    (float)myLocation.getLatitude(), (float)myLocation.getLongitude(), getUserReligionID());

            //Create a new instance of the http post request class
            HttpMarkerPostRequest markerPostRequest = new HttpMarkerPostRequest();

            //Perform the post request using our parameter and request object
            PostUserMarkerHandler userMarkerHandler = new PostUserMarkerHandler(markerPostRequest, markerParams);
            result = userMarkerHandler.makeHttpMarkerPostRequest();

            return result;
        }
        return RETURN_FAILED;
    }

    private void processPostRequestResult(int result){
        //Perform any post processing if needed
        if(result == RETURN_SUCCESS){
            //Success
            Log.d("TAG", "SUCCESS");
        }
        else if(result == RETURN_FAILED){
            //failed
            Log.d("TAG", "FAILED");
        }
    }

    private HashMap<MarkerOptions, Integer> retrieveAllMarkers(){
        final String stringResult;
        final String[] parsedStringResult;
        HashMap<MarkerOptions, Integer> hashMap;


        //Create new instance of a GET request
        GenericHttpGetRequest getRequest = new GenericHttpGetRequest();

        //Pass our request object and URL into the handler class
        RetrieveAllMarkersHandler markersHandler = new RetrieveAllMarkersHandler(GET_MARKERS_URL, getRequest);

        //Retrieve the string result and parse it
        stringResult = markersHandler.fetchMarkers();
        parsedStringResult = markersHandler.parseMarkerResult(stringResult);

        //Use the parsed result to get a Hashmap of the markers and their religion type
        hashMap = markersHandler.mapMarkerItems(parsedStringResult);

        return hashMap;
    }


    private void mapMarkers(HashMap<MarkerOptions, Integer> hashMap){

        //Iterate through non-empty hashmap and add marker to the map
        if(!hashMap.isEmpty() && mMap != null){
            for(HashMap.Entry<MarkerOptions, Integer> entry : hashMap.entrySet()){
                if(entry.getKey() != null){
                    mMap.addMarker(entry.getKey());
                }
            }
        }
    }


    private void retrieveAndMapMarkers(){
        //Get all the markers and map them
        markersHashMap = retrieveAllMarkers();
        mapMarkers(markersHashMap);
    }

    private void bounceMarker(Marker mMarker){
        final int offsetX = 0;
        final int offsetY = -100;
        BounceMarkerHandler bounceMarkerHandler = new BounceMarkerHandler(mMarker);

        bounceMarkerHandler.setStartTime();

        //Set projection of map
        bounceMarkerHandler.setMapProjection(mMap);

        //Set the initial latlng of our marker
        bounceMarkerHandler.setMarkerInitialLatLng(mMarker);

        //Set start point of our marker via the projection of our map
        bounceMarkerHandler.setStartPoint(mMarker);

        //Set our offset of the startPoint (distance change in x, distance change in y)
        bounceMarkerHandler.setStartPointOffset(offsetX, offsetY);

        //Set our start latlng of our bouncing animation
        bounceMarkerHandler.setStartBounceLatLng();

        //Bounce the marker!
        bounceMarkerHandler.bounce();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Resume location updates
        if(mGoogleApiClient.isConnected()){
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stop location updates to conserve batttery
        if(mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Oh no! Looks like we've got some network issues.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(!initialLocationShown){
            showLocation(location);
        }
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(lastOpenedMarker != null){

            //Check if marker title is already open
            if(lastOpenedMarker.equals(marker)){
                lastOpenedMarker = null;

                //close the title window
                return true;
            }
        }

        bounceMarker(marker);

        marker.showInfoWindow();
        lastOpenedMarker = marker;
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_menu_items, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.globalStats) {
            Intent intent = new Intent(this, GlobalStatsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.twitterShare){
            SharePostHandler sharePostHandler = new SharePostHandler(this);
            sharePostHandler.postTweet(DEFAULT_TWEET_TEXT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        final Location myLocation = mMap.getMyLocation();
        if(myLocation != null){
            showLocation(myLocation);
        }

        return true;
    }

}
