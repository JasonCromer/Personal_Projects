package com.dev.cromer.jason.coverme.Activities;


import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dev.cromer.jason.coverme.Logic.DraggedMarker;
import com.dev.cromer.jason.coverme.Logic.LocalMarkers;
import com.dev.cromer.jason.coverme.Logic.PostRequestParams;
import com.dev.cromer.jason.coverme.Networking.HttpPostRequest;
import com.dev.cromer.jason.coverme.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.ExecutionException;


public class MapActivity extends FragmentActivity implements LocationListener,
                                                                GoogleApiClient.ConnectionCallbacks,
                                                                GoogleApiClient.OnConnectionFailedListener,
                                                                View.OnClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private Marker mLastMarker;

    private ImageButton postNewPinButton;
    private EditText setPinTitleText;
    private Button backMeUpButton;

    static DraggedMarker currentDraggedMarker = null;
    static int CAMERA_ZOOM = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        postNewPinButton = (ImageButton) findViewById(R.id.postNewPinButton);
        setPinTitleText = (EditText) findViewById(R.id.pinTitleEditText);
        backMeUpButton = (Button) findViewById(R.id.backMeUpButton);

        backMeUpButton.setOnClickListener(this);
        postNewPinButton.setOnClickListener(this);

        setUpMapIfNeeded();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    //This should only be called once and when we are sure that mMap is not null.
    private void setUpMap() {
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);
        showLocation(mMap.getMyLocation());
    }


    protected void showLocation(Location mCurrentLocation) {
        final Marker mCurrentMarker;

        //clear previous markers
        mMap.clear();

        if (mCurrentLocation != null) {

            //Set up nearby markers
            LocalMarkers localMarkers = new LocalMarkers(mCurrentLocation, mMap);
            localMarkers.setLocalMarkers();                                         //Set local markers based on current position
            localMarkers.mapLocalMarkers();                                     // display local markers from other users


            //Map our current position
            mCurrentMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                    .title("ITS ME!").draggable(true));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())
                    , CAMERA_ZOOM));

            if(mLastMarker != null) {
                mLastMarker.setTitle("Expired!");
            }
            mLastMarker = mCurrentMarker;
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
        if (v == postNewPinButton) {
            if (setPinTitleText.getVisibility() == View.GONE) {
                setPinTitleText.setVisibility(View.VISIBLE);
                backMeUpButton.setVisibility(View.VISIBLE);

            }
            else if(setPinTitleText.getVisibility() == View.VISIBLE){
                setPinTitleText.setVisibility(View.GONE);
                backMeUpButton.setVisibility(View.GONE);
            }
        }
        if(v == backMeUpButton) {
            if(!setPinTitleText.getText().toString().isEmpty()){
                setMarker();
                setPinTitleText.setText("");
            }
        }
    }


    protected void setMarker() {
        String markerLatitude = "";
        String markerLongitude = "";
        final String postRequestURL = "http://10.0.2.2:5000/api/add_marker";
        final String markerTitle = setPinTitleText.getText().toString();
        final Location loc = mMap.getMyLocation();
        boolean hasLocation = false;

        //Give draggedMarker priority, but if null, use current location
        if(currentDraggedMarker == null && loc != null) {
            markerLatitude = String.valueOf(loc.getLatitude());
            markerLongitude = String.valueOf(loc.getLongitude());
            hasLocation = true;
        }
        else if(currentDraggedMarker != null){
            Log.d("TAG", "DRAGGED");
            markerLatitude = currentDraggedMarker.getDraggedLatitude();
            markerLongitude = currentDraggedMarker.getDraggedLongitude();
            hasLocation = true;
        }

        if(hasLocation) {
            //Create params object holding all the data and a new postRequest object
            PostRequestParams params = new PostRequestParams(postRequestURL, markerLatitude, markerLongitude, markerTitle);
            HttpPostRequest postRequest = new HttpPostRequest();

            //Post the marker and Toast the user a confirmation
            try{
                String receivedData = postRequest.execute(params).get();
                Toast.makeText(getApplicationContext(), receivedData, Toast.LENGTH_SHORT).show();
            }
            catch (ExecutionException | InterruptedException | NullPointerException e) {
                e.printStackTrace();
            }

            Location myLocation = mMap.getMyLocation();
            onLocationChanged(myLocation);
        }
    }



    /*
        Override Methods
     */


    @Override
    public void onLocationChanged(Location location) {
        mMap.clear();
        showLocation(location);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stop location updates
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            setUpMapIfNeeded();
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
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        showLocation(mCurrentLocation);
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
        if(!marker.isDraggable()){                      //Only our icon is draggable, we don't want it faded
            marker.setAlpha(.4f);
        }

        return false;
    }
}
