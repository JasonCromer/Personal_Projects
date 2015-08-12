package com.dev.cromer.jason.cshelper.Fragments;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.dev.cromer.jason.cshelper.Activities.AsciiChart;
import com.dev.cromer.jason.cshelper.Logic.GridViewAdapter;
import com.dev.cromer.jason.cshelper.Logic.GridViewItem;
import com.dev.cromer.jason.cshelper.R;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends Fragment {

    static View homeScreenView;
    static GridView gridView;

    //Create items for the GridView
    private String[] imageTitles = {"Ascii Chart", "Something else"};
    private Integer[] imageIds = {R.drawable.ascii_chart_icon, R.drawable.ascii_chart_icon};
    private List<GridViewItem> gridViewItemList;

    private int NUMBER_OF_GRID_ITEMS = imageTitles.length;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeScreenView = inflater.inflate(R.layout.fragment_home_screen, container, false);

        //Create GridView data and adapter
        gridView = (GridView) homeScreenView.findViewById(R.id.gridView);
        initializeData();
        initializeGridAdapter();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_SHORT).show();

                if(position == 0) {
                    Intent gridItemActivityIntent = new Intent(getActivity().getApplicationContext(), AsciiChart.class);
                    //add flag to clear stack and put pass position parameter to determine how to populate the activity
                    gridItemActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    gridItemActivityIntent.putExtra("GridViewPosition", position);

                    //start activity
                    startActivity(gridItemActivityIntent);
                }
            }
        });

        return homeScreenView;
    }


    /*
        This function creates a list that adds a new GridViewItem. The GridViewItem is a class
        structure that holds a String title and int Image Id.
        Here add a GridViewItem for each pair of title and Image Id to the gridViewItemList.
     */
    public void initializeData() {
        gridViewItemList = new ArrayList<>();
        for(int i = 0; i < NUMBER_OF_GRID_ITEMS; i++) {
            gridViewItemList.add(new GridViewItem(imageTitles[i], imageIds[i]));
        }
    }


    //This function sets the adapter to the gridView, passing the activity as the context
    //and the list of items for the adapter to use
    public void initializeGridAdapter() {
        GridViewAdapter adapter = new GridViewAdapter(getActivity(), gridViewItemList);
        gridView.setAdapter(adapter);
    }
}
