package com.dev.cromer.jason.whatshappening.logic;


import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class PlaceArrayAdapter extends ArrayAdapter<PlaceArrayAdapter.PlaceAutocomplete> implements Filterable {

    private GoogleApiClient mGoogleApiClient;
    private AutocompleteFilter mPlaceFilter;
    private LatLngBounds mBounds;
    private ArrayList<PlaceAutocomplete> mResultList;

    /**
     * Constructor
     *
     * @param context  Context
     * @param resource Layout resource
     * @param bounds   Used to specify the search bounds
     * @param filter   Used to specify place types
     */
    public PlaceArrayAdapter(Context context, int resource, LatLngBounds bounds, AutocompleteFilter filter) {
        super(context, resource);
        mBounds = bounds;
        mPlaceFilter = filter;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        if (googleApiClient == null || !googleApiClient.isConnected()) {
            mGoogleApiClient = null;
        }
        else {
            mGoogleApiClient = googleApiClient;
        }
    }

    @Override
    public int getCount() {
        return mResultList.size();
    }

    @Override
    public PlaceAutocomplete getItem(int position) {
        return mResultList.get(position);
    }


    private ArrayList<PlaceAutocomplete> getPredictions(CharSequence constraint) {

        if (mGoogleApiClient != null) {

            //Query predictions with our constraint input
            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, constraint.toString(),
                                    mBounds, mPlaceFilter);

            // Wait for predictions, set the timeout.
            AutocompletePredictionBuffer autocompletePredictions = results
                    .await(60, TimeUnit.SECONDS);

            final Status status = autocompletePredictions.getStatus();

            //Return null if our results failed
            if (!status.isSuccess()) {
                autocompletePredictions.release();
                return null;
            }

            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList<PlaceAutocomplete> resultList = new ArrayList<>(autocompletePredictions.getCount());

            //Add our results to an ArrayList
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                resultList.add(new PlaceAutocomplete(prediction.getPlaceId(),
                        prediction.getDescription()));
            }

            // Buffer release
            autocompletePredictions.release();

            return resultList;
        }

        //Return null if our Google API client isn't connected
        return null;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new Filter.FilterResults();

                if (constraint != null) {

                    // Query the autocomplete API for the entered constraint
                    mResultList = getPredictions(constraint);

                    if (mResultList != null) {

                        // Results
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (results != null && results.count > 0) {

                    // The API returned at least one result, update the data.
                    notifyDataSetChanged();
                }
                else {

                    // The API did not return any results, invalidate the data set.
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    public class PlaceAutocomplete {

        public CharSequence placeId;
        public CharSequence description;

        PlaceAutocomplete(CharSequence placeId, CharSequence description) {
            this.placeId = placeId;
            this.description = description;
        }

        public String toString(){
            return description.toString();
        }
    }
}
