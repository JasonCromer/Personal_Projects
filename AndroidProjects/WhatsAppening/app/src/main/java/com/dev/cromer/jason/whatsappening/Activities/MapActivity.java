package com.dev.cromer.jason.whatsappening.Activities;


import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MapActivity extends FragmentActivity implements LocationListener,
                                                                GoogleApiClient.ConnectionCallbacks,
                                                                GoogleApiClient.OnConnectionFailedListener,
                                                                View.OnClickListener, GoogleMap.OnMarkerDragListener,
                                                                 GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraChangeListener, OnMapReadyCallback, AdapterView.OnItemClickListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;

    private ImageButton postNewPinButton;
    private EditText pinTitleEditText;
    private Button setPinButton;
    private Marker mLastMarker;
    private CameraPosition lastCameraPosition = null;
    private Marker lastOpenedMarker = null;

    static DraggedMarker currentDraggedMarker = null;
    static int CAMERA_ZOOM = 15;

    //Hashmap for storing local, non-duplicate local markers
    private HashMap<MarkerOptions, Integer> postableMarkersHashMap = new HashMap<>();



    //Autocomplete search bar
    private static final int GOOGLE_API_CLIENT_ID = 0;
    static AutoCompleteTextView autocompleteTextView;
    private PlaceArrayAdapter placeArrayAdapter;
    private static final LatLngBounds GLOBAL_BOUNDS = new LatLngBounds(new LatLng(-85.0, -180.0), new LatLng(85.0, 180.0));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        autocompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        postNewPinButton = (ImageButton) findViewById(R.id.postNewPinButton);
        pinTitleEditText = (EditText) findViewById(R.id.pinTitleEditText);
        setPinButton = (Button) findViewById(R.id.backMeUpButton);

        setPinButton.setOnClickListener(this);
        postNewPinButton.setOnClickListener(this);
        autocompleteTextView.setOnItemClickListener(this);


        //Setup the autocomplete feature and googleApiClient
        setUpGoogeApiClient();
        setUpAutocompleteView();

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
        showLocation(mMap.getMyLocation());
    }


    private void setUpAutocompleteView() {
        autocompleteTextView.setThreshold(3);

        placeArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                GLOBAL_BOUNDS, null);
        autocompleteTextView.setAdapter(placeArrayAdapter);
    }

    private void setUpGoogeApiClient() {
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
        if (v == postNewPinButton){
            if (pinTitleEditText.getVisibility() == View.GONE) {
                pinTitleEditText.setVisibility(View.VISIBLE);
                setPinButton.setVisibility(View.VISIBLE);
                autocompleteTextView.setVisibility(View.VISIBLE);

            }
            else if(pinTitleEditText.getVisibility() == View.VISIBLE){
                pinTitleEditText.setVisibility(View.GONE);
                setPinButton.setVisibility(View.GONE);
                autocompleteTextView.setVisibility(View.GONE);
                autocompleteTextView.setText("");
                pinTitleEditText.setText("");
                hideSoftKeyboard();
            }
        }
        if(v == setPinButton){
            if(!pinTitleEditText.getText().toString().isEmpty()){
                setMarker();
                pinTitleEditText.setText("");
                autocompleteTextView.setText("");
                hideSoftKeyboard();
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
        final String markerTitle = pinTitleEditText.getText().toString();
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
            if(mLastMarker != null){
                mLastMarker.remove();
            }
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



    private LatLng getLocationFromAddress(String strAddress){
        Geocoder geocoder = new Geocoder(this);
        final int MAX_LOCATION_RESULTS = 1;

        if(strAddress != null && !strAddress.isEmpty()){
            try{
                List<android.location.Address> addressList = geocoder.getFromLocationName(strAddress, MAX_LOCATION_RESULTS);
                if(addressList != null && addressList.size() > 0) {
                    double lat = addressList.get(0).getLatitude();
                    double lng = addressList.get(0).getLongitude();

                    return new LatLng(lat,lng);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e("TAG", "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);

            //Get address and return a latlng location
            final String address = String.valueOf(place.getAddress());
            LatLng thisAddress = getLocationFromAddress(address);

            if(thisAddress != null) {
                //move camera to latlng location and clear search box
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(thisAddress, CAMERA_ZOOM));
                autocompleteTextView.setText("");
                hideSoftKeyboard();
            }

            //mNameTextView.setText(Html.fromHtml(place.getName() + ""));
            //mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
            //mIdTextView.setText(Html.fromHtml(place.getId() + ""));
            //mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
            //mWebTextView.setText(place.getWebsiteUri() + "");
        }
    };




    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(autocompleteTextView.getWindowToken(), 0);
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
        if(mLastMarker != null) {
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        placeArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        showLocation(mCurrentLocation);
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        placeArrayAdapter.setGoogleApiClient(null);
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

        //Get current centered location
        mCurrentMarkerLocation.setLatitude(center.latitude);
        mCurrentMarkerLocation.setLongitude(center.longitude);


        //Compare previous center marker with current one
        final double camLat = center.latitude;
        final double camLng = center.longitude;

        if(lastCameraPosition != null) {
            final double latUpperBound = lastCameraPosition.target.latitude+1.5;
            final double latLowerBound = lastCameraPosition.target.latitude-1.5;
            final double lngUpperBound = lastCameraPosition.target.longitude+1.5;
            final double lngLowerBound = lastCameraPosition.target.longitude-1.5;

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
        final PlaceArrayAdapter.PlaceAutocomplete item = placeArrayAdapter.getItem(position);
        final String placeId = String.valueOf(item.placeId);
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
    }
}
