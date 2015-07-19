package com.example.jason.healthcaredemo;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by jason on 7/18/15.
 */
public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemName;
    private final Integer[] imageId;

    public CustomListAdapter(Activity context, String[] itemName, Integer[] imageId) {
        super(context, R.layout.exercise_list_item, itemName);

        this.context = context;
        this.itemName = itemName;
        this.imageId = imageId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        if(rowView == null) {
            Log.d("VIEW: ", "INFLATED");
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.exercise_list_item, null, true);
        }

        String item = itemName[position];
        if(item != null) {
            Log.d("LOADED ITEM", itemName[position]);
            TextView exerciseTitle = (TextView) rowView.findViewById(R.id.exerciseItemTitle);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.exerciseItemImage);
            TextView exerciseDescription = (TextView) rowView.findViewById(R.id.exerciseItemDescription);

            exerciseTitle.setText(itemName[position]);
            imageView.setImageResource(imageId[position]);
            exerciseDescription.setText("Description " + itemName[position]);

        }
        return rowView;
    }
}
