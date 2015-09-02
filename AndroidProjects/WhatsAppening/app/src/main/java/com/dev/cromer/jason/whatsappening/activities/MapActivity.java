package com.dev.cromer.jason.whatsappening.activities;


import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dev.cromer.jason.whatsappening.logic.DraggedMarker;
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
                                                                View.OnClickListener, GoogleMap.OnMarkerDragListener,
                                                                 GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraChangeListener,
                                                                OnMapReadyCallback {

    // mMap might be null if Google Play services APK is not available.
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    //Map objects. Nullify objects that must not exist at activity creation
    private ImageButton postNewPinButton;
    private Marker mLastMarker = null;
    private CameraPosition lastCameraPosition = null;
    private Marker lastOpenedMarker = null;
    private DraggedMarker currentDraggedMarker = null;

    //constants
    private static final int CAMERA_ZOOM = 18;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final double LAT_LNG_OFFSET = 1.5;
    private static final int POST_NEW_MARKER_REQ_CODE = 0;
    private static final int SEARCH_PLACE_REQ_CODE = 1;

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
        postNewPinButton = (ImageButton) findViewById(R.id.postNewPinButton);

        postNewPinButton.setOnClickListener(this);
        searchBarEditText.setOnClickListener(this);


        //Setup the autocomplete feature and googleApiClient
        setUpGoogleApiClient();

        //Instantiate the map
        ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }



    //This should only be called once and when we are sure that mMap is not null.
    private void setUpMap() {
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraChangeListener(this);
        mMap.setPadding(0, 120, 0, 0);
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
                    ,CAMERA_ZOOM));
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
        if (v == postNewPinButton){
            //Start intent for user to add marker title
            Intent resultIntent = new Intent(this, PostNewMarkerActivity.class);
            startActivityForResult(resultIntent, POST_NEW_MARKER_REQ_CODE);
        }
        if(v == searchBarEditText){
            //Start intent for user to search a place
            Intent searchIntent = new Intent(this, SearchPlaceActivity.class);
            startActivityForResult(searchIntent, SEARCH_PLACE_REQ_CODE);
        }
    }


    /* This function sets a marker at a location based on whether or not the user's
        maker has been dragged, or if they have a current location or not.
        Priority is given to the centered marker, then dragged marker, and
        lastly, the current location
     */
    protected void setMarker(String markerTitle) {
        String markerLatitude = "";
        String markerLongitude = "";
        final String postRequestURL = "http://10.0.2.2:5000/api/add_marker";
        final Location currentLocation = mMap.getMyLocation();
        boolean hasLocation = false;

        //Give centered marker first priority
        if(mLastMarker != null && currentDraggedMarker == null){
            LatLng markerLocation = mLastMarker.getPosition();
            markerLatitude = String.valueOf(markerLocation.latitude);
            markerLongitude = String.valueOf(markerLocation.longitude);
            if(mLastMarker != null){
                mLastMarker.remove();
            }
            currentDraggedMarker = null;
            hasLocation = true;
        }
        //If dragged marker is null, and current location isn't, use current location
        else if(currentDraggedMarker == null && currentLocation != null) {
            markerLatitude = String.valueOf(currentLocation.getLatitude());
            markerLongitude = String.valueOf(currentLocation.getLongitude());
            hasLocation = true;
        }
        //If dragged marker isn't null, use dragged marker location
        else if(currentDraggedMarker != null){
            markerLatitude = currentDraggedMarker.getDraggedLatitude();
            markerLongitude = currentDraggedMarker.getDraggedLongitude();
            if(mLastMarker != null){
                mLastMarker.remove();
            }
            //Set null to remove the dragged marker
            currentDraggedMarker = null;
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
        if (mLastMarker != null) {
            mLastMarker.remove();
        }
        showLocation(location);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLastMarker != null){
            mLastMarker.remove();
        }
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
        if(lastCameraPosition != null){
            onCameraChange(lastCameraPosition);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        //Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }




    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Get dragged marker Position
        LatLng markerPos = marker.getPosition();
        currentDraggedMarker = new DraggedMarker(String.valueOf(markerPos.latitude),
                String.valueOf(markerPos.longitude));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(lastOpenedMarker != null){
            lastOpenedMarker.hideInfoWindow();

            //is the marker already open
            if(lastOpenedMarker.equals(marker)){
                //if so, nullify it
                lastOpenedMarker = null;
                //return true so it doesn't open again
                return true;
            }
        }
        if(!marker.isDraggable()){
            marker.setAlpha(.4f);
        }

        marker.showInfoWindow();
        lastOpenedMarker = marker;

        return false;
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        final Marker mCurrentMarker;
        final Location mCurrentMarkerLocation = new Location("CenteredMarkerLocation");


        //Get center of map
        LatLng center = mMap.getCameraPosition().target;

        //Get current centered location
        mCurrentMarkerLocation.setLatitude(center.latitude);
        mCurrentMarkerLocation.setLongitude(center.longitude);


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
                getNearbyMarkers(mCurrentMarkerLocation);
            }
            //If camera moves out of bounds then get new markers and clear map and postable markers list
            else{
                mMap.clear();
                postableMarkersHashMap.clear();
                getNearbyMarkers(mCurrentMarkerLocation);
            }
        }
        lastCameraPosition = cameraPosition;

        //Remove previous marker to refrain from duplicate markers
        if(mLastMarker != null) {
            mLastMarker.remove();
        }

        //Nullify currentDraggedMarker to avoid marker posting its locationSo
        currentDraggedMarker = null;

        //Place marker at center of map, each time the camera moves
        mCurrentMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(center.latitude, center.longitude))
                .draggable(true).anchor(.5f, .5f));

        mLastMarker = mCurrentMarker;
    }


    //This returns data from a previously opened activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case(POST_NEW_MARKER_REQ_CODE):
                if(resultCode == Activity.RESULT_OK){
                    //Get title inputted by user in previous activity
                    final String newMarkerTitle = data.getStringExtra("TITLE");
                    setMarker(newMarkerTitle);
                }
                break;
            case(SEARCH_PLACE_REQ_CODE):
                if(resultCode == Activity.RESULT_OK) {
                    final LatLng searchedAddress = data.getParcelableExtra("SEARCHED_LOCATION");
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(searchedAddress, CAMERA_ZOOM));
                    //call Camera change on last location so centered marker appears
                    onCameraChange(lastCameraPosition);
                }
                break;
        }
    }
}
