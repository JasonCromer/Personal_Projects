package com.dev.cromer.jason.cshelper.Logic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.cromer.jason.cshelper.R;

import java.util.List;


public class GridViewAdapter extends ArrayAdapter {

    private Context gridViewContext;
    private List<GridViewItem> gridViewItemList;

    static TextView itemText;
    static ImageView itemImage;

    /*
        Constructor: Pass in application context (Activity), and the gridViewItemList
        that holds a GridViewItem with two lists: one of string titles and one of int
        Image Id's
     */
    public GridViewAdapter(Context gridViewContext, List<GridViewItem> gridViewItemList) {
        super(gridViewContext, R.layout.grid_view_item, gridViewItemList);
        this.gridViewContext = gridViewContext;
        this.gridViewItemList = gridViewItemList;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) gridViewContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //inflate the view
            convertView = inflater.inflate(R.layout.grid_view_item, parent, false);
        }

        //initiate textview and imageview
        itemText = (TextView) convertView.findViewById(R.id.gridViewText);
        itemImage = (ImageView) convertView.findViewById(R.id.gridViewImage);

        //set text and image resources from the passed in list
        itemText.setText(gridViewItemList.get(position).title);
        itemImage.setImageResource(gridViewItemList.get(position).imageId);

        return convertView;
    }
}
