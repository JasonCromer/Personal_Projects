package com.dev.cromer.jason.coverme;


import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dev.cromer.jason.coverme.Networking.HttpGetRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MapActivity extends FragmentActivity implements com.google.android.gms.location.LocationListener,
                                                                GoogleApiClient.ConnectionCallbacks,
                                                                GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private Marker mCurrentMarker;
    private Marker mLastMarker;
    private List<String> latLngLocalPositions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
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


    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);

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
    protected void showLocation(Location mCurrentLocation) {
        //clear previous markers
        mMap.clear();

        if (mCurrentLocation != null) {

            //grab local marker locations
            latLngLocalPositions = getLocalMarkers(mCurrentLocation);
            if(latLngLocalPositions.size() > 1) {
                Log.d("TAG", String.valueOf(latLngLocalPositions));
                mapLocalMarkers(latLngLocalPositions);
            }
            else {
                Toast.makeText(getApplicationContext(), "No data available: Server is down", Toast.LENGTH_LONG).show();
            }


            Log.i("Where am I?", "Latitude: " + mCurrentLocation.getLatitude() + ", Longitude:" + mCurrentLocation.getLongitude());
            mCurrentMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                    .title("ITS ME!").draggable(true));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 7));

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
    public void onLocationChanged(Location location) {
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
            setUpMapIfNeeded();    // <-from previous tutorial
            startLocationUpdates();
        }
    }




    private List<String> getLocalMarkers(Location mCurrentLocation) {
        List<String> markerItems = Arrays.asList("");   //Initialize as to return null if try doesn't work
        final String url = "http://10.0.2.2:5000/api/get_markers/"+String.valueOf(mCurrentLocation.getLatitude())+
                String.valueOf("/"+mCurrentLocation.getLongitude());


        try{
            HttpGetRequest getRequest = new HttpGetRequest();
            String receivedData = getRequest.execute(url).get();
            receivedData = receivedData.replace("[", "").replace("]", "").replace("\"", "");  //replace brackets
            markerItems = Arrays.asList(receivedData.split("\\s*,\\s*"));   //filter out whitespace

            return markerItems;
        }
        catch (ExecutionException | InterruptedException | NullPointerException e) {
            e.printStackTrace();
        }

        return markerItems;
    }


    private void mapLocalMarkers(List<String> localCoordinates) {
        /*
            List includes a pattern of: [latitude, longitude, latitude, long...]
            so we must assign values based on chunks of two, then iterate by 2.
         */
        for(int i = 0; i < localCoordinates.size(); i+=3) {
            final String thisLatitude = localCoordinates.get(i);
            final String thisLongitude = localCoordinates.get(i+1);
            final String thisTitle = localCoordinates.get(i+2);

            mMap.addMarker(new MarkerOptions().position(new LatLng(Float.valueOf(thisLatitude), Float.valueOf(thisLongitude)))
                    .title(thisTitle).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        }
    }
}
