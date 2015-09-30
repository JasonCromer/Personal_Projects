package com.dev.cromer.jason.takeastand;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
                                                                GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    private static final int LOCATION_REQUEST_INTERVAL_MILLISECONDS = 10000;
    private static final int FASTEST_LOCATION_REQUEST_INTERVAL_MILLISECONDS = 5000;



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
    }



    private void startLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_REQUEST_INTERVAL_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_LOCATION_REQUEST_INTERVAL_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
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
    }
}
