package com.dev.cromer.jason.whatsappening.Activities;


import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dev.cromer.jason.whatsappening.Logic.DraggedMarker;
import com.dev.cromer.jason.whatsappening.Logic.LocalMarkers;
import com.dev.cromer.jason.whatsappening.Logic.PlaceArrayAdapter;
import com.dev.cromer.jason.whatsappening.Logic.PostRequestParams;
import com.dev.cromer.jason.whatsappening.Networking.HttpPostRequest;
import com.dev.cromer.jason.whatsappening.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.ExecutionException;


public class MapActivity extends FragmentActivity implements LocationListener,
                                                                GoogleApiClient.ConnectionCallbacks,
                                                                GoogleApiClient.OnConnectionFailedListener,
                                                                View.OnClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraChangeListener, AdapterView.OnItemClickListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;

    private ImageButton postNewPinButton;
    private EditText setPinTitleText;
    private Button backMeUpButton;
    private Marker mLastMarker;
    private CameraPosition lastCameraPosition = null;
    private Marker lastOpenedMarker = null;

    static DraggedMarker currentDraggedMarker = null;
    static int CAMERA_ZOOM = 15;
    boolean someBool = true;



    //Autofill search bar
    private static final String LOG_TAG = "MapActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    static AutoCompleteTextView mAutocompleteTextView;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        postNewPinButton = (ImageButton) findViewById(R.id.postNewPinButton);
        setPinTitleText = (EditText) findViewById(R.id.pinTitleEditText);
        backMeUpButton = (Button) findViewById(R.id.backMeUpButton);

        //AutoComplete
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        mAutocompleteTextView.setThreshold(3);


        backMeUpButton.setOnClickListener(this);
        postNewPinButton.setOnClickListener(this);
        mAutocompleteTextView.setOnItemClickListener(this);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

        setUpMapIfNeeded();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
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
        mMap.setOnCameraChangeListener(this);
        showLocation(mMap.getMyLocation());
    }


    protected void showLocation(Location mCurrentLocation) {

        if (mCurrentLocation != null) {
            getNearbyMarkers(mCurrentLocation);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())
                    ,CAMERA_ZOOM));
        }

        //show centered marker
        LatLng center = mMap.getCameraPosition().target;
        mLastMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(center.latitude, center.longitude))
                .draggable(true));

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
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mAutocompleteTextView.setVisibility(View.VISIBLE);

            }
            else if(setPinTitleText.getVisibility() == View.VISIBLE){
                setPinTitleText.setVisibility(View.GONE);
                backMeUpButton.setVisibility(View.GONE);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mAutocompleteTextView.setVisibility(View.GONE);
            }
        }
        if(v == backMeUpButton) {
            if(!setPinTitleText.getText().toString().isEmpty()){
                setMarker();
                setPinTitleText.setText("");
            }
        }
    }


    /* This function sets a marker at a location based on whether or not the user's
        maker has been dragged, or if they have a current location or not.
        Priority is given to the centered marker, then dragged marker, and
        lastly, the current location
     */
    protected void setMarker() {
        String markerLatitude = "";
        String markerLongitude = "";
        final String postRequestURL = "http://10.0.2.2:5000/api/add_marker";
        final String markerTitle = setPinTitleText.getText().toString();
        final Location currentLocation = mMap.getMyLocation();
        boolean hasLocation = false;

        //Give centered marker first priority
        if(mLastMarker != null && currentDraggedMarker == null){
            LatLng markerLocation = mLastMarker.getPosition();
            markerLatitude = String.valueOf(markerLocation.latitude);
            markerLongitude = String.valueOf(markerLocation.longitude);
            mLastMarker.remove();
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
            //mLastMarker.remove();
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
                Toast.makeText(getApplicationContext(), receivedData, Toast.LENGTH_SHORT).show();
            }
            catch (ExecutionException | InterruptedException | NullPointerException e) {
                e.printStackTrace();
            }

            Location myLocation = mMap.getMyLocation();
            onLocationChanged(myLocation);
        }
    }



    public void getNearbyMarkers(Location mCurrentLocation) {
        //Set up nearby markers
        LocalMarkers localMarkers = new LocalMarkers(mCurrentLocation, mMap);
        localMarkers.getLocalMarkers();                                         //Set local markers based on current position
        localMarkers.mapLocalMarkers();                                     // display local markers from other users
    }




    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            //mNameTextView.setText(Html.fromHtml(place.getName() + ""));
            //mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
            //mIdTextView.setText(Html.fromHtml(place.getId() + ""));
            //mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
            //mWebTextView.setText(place.getWebsiteUri() + "");
            //if (attributions != null) {
                //mAttTextView.setText(Html.fromHtml(attributions.toString()));
            //}
        }
    };



    /*
        Override Methods
     */


    @Override
    public void onLocationChanged(Location location) {
        showLocation(location);
        if(mLastMarker != null) {
            mLastMarker.remove();
        }
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
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        showLocation(mCurrentLocation);
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
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

            if(lastOpenedMarker.equals(marker)){
                lastOpenedMarker = null;
                return true;
            }
        }
        if(!marker.isDraggable()){
            marker.setAlpha(.4f);
        }

        marker.showInfoWindow();
        lastOpenedMarker = marker;

        return true;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        final Marker mCurrentMarker;
        final Location mCurrentMarkerLocation = new Location("CenteredMarkerLocation");


        //Get center of map
        LatLng center = mMap.getCameraPosition().target;

        //Get nearby posted markers
        mCurrentMarkerLocation.setLatitude(center.latitude);
        mCurrentMarkerLocation.setLongitude(center.longitude);

        final double camLat = center.latitude;
        final double camLng = center.longitude;
        if(lastCameraPosition != null) {
            if((lastCameraPosition.target.latitude-1 <= camLat && camLat <= lastCameraPosition.target.latitude+1) &&
                    (lastCameraPosition.target.longitude-1 <= camLng && camLng <= lastCameraPosition.target.longitude+1)){
                getNearbyMarkers(mCurrentMarkerLocation);
            }
            //If not in range of last centered marker(by 1 lat and lng), then get new markers and clear map
            else{
                mMap.clear();
                getNearbyMarkers(mCurrentMarkerLocation);
                someBool = false;
            }
        }
        lastCameraPosition = cameraPosition;

        //Place marker at center of map, each time the camera moves
        mCurrentMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(center.latitude, center.longitude))
                .draggable(true));

        //Remove previous marker to refrain from duplicate markers
        if(mLastMarker != null) {
            mLastMarker.remove();
        }
        mLastMarker = mCurrentMarker;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
        final String placeId = String.valueOf(item.placeId);
        Log.i(LOG_TAG, "Selected: " + item.description);
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
    }
}
