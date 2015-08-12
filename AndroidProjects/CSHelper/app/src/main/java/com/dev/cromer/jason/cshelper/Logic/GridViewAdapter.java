package com.dev.cromer.jason.cshelper.Logic;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.cromer.jason.cshelper.R;

import java.util.ArrayList;
import java.util.List;


public class GridViewAdapter extends ArrayAdapter {

    private Context gridViewContext;
    private List<GridViewItem> gridViewItemList;

    private TextView itemText;
    private ImageView itemImage;

    //constructor
    public GridViewAdapter(Context gridViewContext, List<GridViewItem> gridViewItemList) {
        super(gridViewContext, R.layout.grid_view_item, gridViewItemList);
        this.gridViewContext = gridViewContext;
        this.gridViewItemList = gridViewItemList;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //inflate the view
            convertView = (View) inflater.inflate(R.layout.grid_view_item, parent, false);
        }

        //initiate textview and imageview
        itemText = (TextView) convertView.findViewById(R.id.gridViewText);
        itemImage = (ImageView) convertView.findViewById(R.id.gridViewImage);

        //set text and image resources
        itemText.setText(gridViewItemList.get(position).title);
        itemImage.setImageResource(gridViewItemList.get(position).imageId);

        return convertView;
    }
}
