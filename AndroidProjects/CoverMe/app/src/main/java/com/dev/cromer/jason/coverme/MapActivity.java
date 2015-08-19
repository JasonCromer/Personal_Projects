package com.dev.cromer.jason.coverme;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements LocationListener, ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMyLocationChangeListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private static final String[] INITIAL_PERMS = {android.Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int INITIAL_REQUEST = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if(!canAccessLocation()) {
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
        }
        else {
            setUpMapIfNeeded();
            Log.d("TAG", "BLAH");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d("TAG", "finished request");
        //setUpMapIfNeeded();
    }


    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                //mMap.setMyLocationEnabled(true);
                //mMap.setOnMyLocationChangeListener(this);
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
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (myLocation != null) {
                Log.d("TAG", "NOT NULL");

                LatLng location = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                Log.d("VALUES", String.valueOf(location));
                mMap.addMarker(new MarkerOptions().position(location).title("ITS ME!"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 8.0f));
            }
            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1000, this);
                Log.d("TAG", "NULL");
            }
        }
        catch (SecurityException se) {
            Log.d("TAG", "SE CAUGHT");
            se.printStackTrace();
        }
    }


    private boolean canAccessLocation() {
        int res = getApplicationContext().checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        return (res == PackageManager.PERMISSION_GRANTED);
    }







    @Override
    public void onLocationChanged(Location mlocation) {
        Log.d("CHANGED", "LOCATION UPDATED");


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMyLocationChange(Location location) {
        Log.d("TAG", "LOCATION CHANGED");
    }
}
