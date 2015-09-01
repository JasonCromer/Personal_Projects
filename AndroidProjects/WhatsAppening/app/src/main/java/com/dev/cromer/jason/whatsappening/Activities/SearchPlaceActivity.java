package com.dev.cromer.jason.whatsappening.Activities;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.dev.cromer.jason.whatsappening.Logic.PlaceArrayAdapter;
import com.dev.cromer.jason.whatsappening.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.List;

public class SearchPlaceActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    static int CAMERA_ZOOM = 18;

    //Autocomplete search bar
    static GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    static AutoCompleteTextView autocompleteTextView;
    private PlaceArrayAdapter placeArrayAdapter;
    private static final LatLngBounds GLOBAL_BOUNDS = new LatLngBounds(new LatLng(-85.0, -180.0), new LatLng(85.0, 180.0));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_place);

        autocompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        setupGoogleApiClient();
        mGoogleApiClient.connect();
        setUpAutocompleteView();
    }


    private void setupGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .build();

    }


    private void setUpAutocompleteView() {
        autocompleteTextView.setThreshold(3);
        autocompleteTextView.setOnItemClickListener(this);

        placeArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                GLOBAL_BOUNDS, null);
        autocompleteTextView.setAdapter(placeArrayAdapter);
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
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(thisAddress, CAMERA_ZOOM));
                autocompleteTextView.setText("");
                //hideSoftKeyboard();
            }

            //mNameTextView.setText(Html.fromHtml(place.getName() + ""));
            //mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
            //mIdTextView.setText(Html.fromHtml(place.getId() + ""));
            //mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
            //mWebTextView.setText(place.getWebsiteUri() + "");
        }
    };



    private LatLng getLocationFromAddress(String strAddress){
        Geocoder geocoder = new Geocoder(this);
        final int MAX_LOCATION_RESULTS = 1;

        if(strAddress != null && !strAddress.isEmpty()){
            try{
                List<Address> addressList = geocoder.getFromLocationName(strAddress, MAX_LOCATION_RESULTS);
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



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final PlaceArrayAdapter.PlaceAutocomplete item = placeArrayAdapter.getItem(position);
        final String placeId = String.valueOf(item.placeId);
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {
        placeArrayAdapter.setGoogleApiClient(mGoogleApiClient);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
