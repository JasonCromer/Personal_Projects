package com.dev.cromer.jason.whatshappening.activities;


import android.app.Activity;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dev.cromer.jason.whatshappening.logic.LocalMarkers;
import com.dev.cromer.jason.whatshappening.logic.NewMarkerPostRequestParams;
import com.dev.cromer.jason.whatshappening.networking.NewMarkerHttpPostRequest;
import com.dev.cromer.jason.whatshappening.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.SupportMapFragment;
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
                                                                OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener {

    // mMap might be null if Google Play services APK is not available.
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    //Map objects. Nullify objects that must not exist at activity creation
    private Marker temporaryPlacedMarker = null;
    private Marker temporarySearchedMarker = null;
    private CameraPosition lastCameraPosition = null;
    private Marker lastOpenedMarker = null;
    private boolean hasSearched = false;
    private boolean initialLocationShown = false;
    private LatLng searchedAddress;

    //constants
    private static final int CAMERA_ZOOM = 18;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final double LAT_LNG_OFFSET = 1.5;
    private static final int POST_NEW_MARKER_REQ_CODE = 1;
    private static final int SEARCH_PLACE_REQ_CODE = 2;
    private static final int WAIT_IN_MILLISECONDS = 500;

    //Hashmap for storing local, non-duplicate local markers
    private HashMap<MarkerOptions, Integer> postableMarkersHashMap = new HashMap<>();
    private HashMap<String, Integer> markerIDHashMap = new HashMap<>();

    //EditText search bar
    private static EditText searchBarEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.show();
        }

        searchBarEditText = (EditText) findViewById(R.id.searchBarEditText);
        searchBarEditText.setOnClickListener(this);


        //Setup the autocomplete feature and googleApiClient
        setUpGoogleApiClient();

        //Instantiate the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Context thisConx = this.getApplicationContext();
        //UiModeManager manager = (UiModeManager) thisConx.getSystemService(Context.UI_MODE_SERVICE);
    }



    //This should only be called once and when we are sure that mMap is not null.
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //For adding new markers
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
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
            if(!initialLocationShown){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())
                        , CAMERA_ZOOM));

                initialLocationShown = true;
            }

            //Update nearby markers
            getNearbyMarkers(mCurrentLocation);
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
            searchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(searchIntent, SEARCH_PLACE_REQ_CODE);
        }
    }


    /* This function sets a marker at a location where the user has pressed and held down
        on the map.
     */
    protected void setMarker(String markerTitle, String markerDescription) {
        String markerLatitude = "";
        String markerLongitude = "";
        final String postRequestURL = "http://whatsappeningapi.elasticbeanstalk.com/api/add_marker";
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
            NewMarkerPostRequestParams params = new NewMarkerPostRequestParams(postRequestURL, markerLatitude, markerLongitude, markerTitle.replace(","," "),
                    markerDescription.replace(",", " "));
            NewMarkerHttpPostRequest postRequest = new NewMarkerHttpPostRequest();

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
                Toast.makeText(getApplicationContext(), "Cannot contact our server! Check your connection.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            //After posting, refresh area that user posted in
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
                //Store markerID from the current marker in the HashList
                final Integer markerID = Integer.parseInt(String.valueOf(pair.getValue()));

                //If the postable Hashmap doesn't contain the ID in the current Hashmap, add it
                if(!postableMarkersHashMap.containsValue(markerID)){
                    postableMarkersHashMap.put(pair.getKey(), pair.getValue());

                    //Add marker to map (the Key of the Hashmap is the marker object)
                    Marker mappedMarker = mMap.addMarker(pair.getKey());

                    //Add google map's marker id with database ID to Hashmap
                    markerIDHashMap.put(mappedMarker.getId(), markerID);

                }
                iterator.remove();
            }
        }
        //If no markers in local area, clear the current list of postable markers
        else{
            postableMarkersHashMap.clear();
        }

    }



    //Delay the SetMarkerTitleActivity intent for a short time, declared by the WAIT_IN_MILLISECONDS
    //constant, so that user can see that they are putting down a marker before the intent initiates.
    private void delayTitleIntent(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent resultIntent = new Intent(getApplicationContext(), SetMarkerTitleActivity.class);
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        if(mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
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
        Toast.makeText(getApplicationContext(), "Oh no! Looks like we've got some network issues.", Toast.LENGTH_SHORT).show();
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
                Intent descriptionIntent = new Intent(getApplicationContext(), MarkerDescriptionActivity.class);
                descriptionIntent.putExtra("MARKER_ID", String.valueOf(markerIDHashMap.get(marker.getId())));
                descriptionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(descriptionIntent);

                //if so, nullify it
                lastOpenedMarker = null;

                //return true to close info window
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


    @Override
    public void onInfoWindowClick(Marker marker) {
        //Open marker description
        Intent descriptionIntent = new Intent(getApplicationContext(), MarkerDescriptionActivity.class);
        descriptionIntent.putExtra("MARKER_ID", String.valueOf(markerIDHashMap.get(marker.getId())));
        descriptionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(descriptionIntent);
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
                    final String newMarkerDescription = data.getStringExtra("MARKER_DESCRIPTION");
                    setMarker(newMarkerTitle, newMarkerDescription);
                }
                break;

            case(SEARCH_PLACE_REQ_CODE):
                if(resultCode == Activity.RESULT_OK) {
                    if(temporarySearchedMarker != null){
                        temporarySearchedMarker.remove();
                    }
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
