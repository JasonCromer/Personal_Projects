package com.dev.cromer.jason.whatsappening.Logic.MapSearchBar;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.dev.cromer.jason.whatsappening.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 8/26/15.
 */
public class GeoAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private static final int MAX_RESULTS = 10;
    private Context mContext;
    private List resultList = new ArrayList();


    //constructor
    public GeoAutoCompleteAdapter(Context context) {
        mContext = context;
    }


    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public GeoSearchResult getItem(int position) {
        return (GeoSearchResult) resultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.geo_search_result_item, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.geo_search_result_text)).setText(getItem(position).getAddress());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null) {
                    List locations = FindLocations(mContext, constraint.toString());

                    //Assign the data to the FilterResults
                    filterResults.values = locations;
                    filterResults.count = locations.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results != null && results.count > 0) {
                    resultList = (List) results.values;
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }



    private List<GeoSearchResult> FindLocations(Context context, String queryText) {
        List<GeoSearchResult> geoSearchResults = new ArrayList<>();

        Geocoder geocoder = new Geocoder(context, context.getResources().getConfiguration().locale);
        List<Address> addresses = null;

        try {
            //Getting a max of 10 addresses that matches the input text
            addresses = geocoder.getFromLocationName(queryText, MAX_RESULTS);

            for(int i = 0; i < addresses.size(); i++) {
                Address address = (Address) addresses.get(i);
                if(address.getMaxAddressLineIndex() != -1) {
                    geoSearchResults.add(new GeoSearchResult(address));
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return geoSearchResults;
    }
}
