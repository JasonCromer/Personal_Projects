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

import com.dev.cromer.jason.cshelper.Activities.AsciiChartActivity;
import com.dev.cromer.jason.cshelper.Activities.DataContainerActivity;
import com.dev.cromer.jason.cshelper.Activities.ProgrammingLanguagesActivity;
import com.dev.cromer.jason.cshelper.Activities.TimeComplexityActivity;
import com.dev.cromer.jason.cshelper.Logic.GridViewAdapter;
import com.dev.cromer.jason.cshelper.Logic.GridViewItem;
import com.dev.cromer.jason.cshelper.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends Fragment implements AdapterView.OnItemClickListener {

    static View homeScreenView;
    static GridView gridView;

    //Create items for the GridView
    private String[] imageTitles = {"Ascii Chart", "Programming Languages", "Time Complexity",
                                "Unix Cheat Sheet", "Data Containers", "SQL Cheat Sheet"};
    private Integer[] imageIds = {R.drawable.ascii_table_icon, R.drawable.programming_languages_icon, R.drawable.time_complexity_icon,
                                R.drawable.unix_sheet_icon, R.drawable.data_container_icon, R.drawable.sql_sheet_icon};
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

        gridView.setOnItemClickListener(this);

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


    //Handle intent based on which gridview item is clicked via position
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Ascii Chart Activity
        if(position == 0) {
            Intent asciiChartIntent = new Intent(getActivity().getApplicationContext(), AsciiChartActivity.class);

            //add flag to clear stack and put pass position parameter to determine how to populate the activity
            asciiChartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(asciiChartIntent);
        }

        //Programming Languages Activity
        if(position == 1) {
            Intent languagesIntent = new Intent(getActivity().getApplicationContext(), ProgrammingLanguagesActivity.class);
            languagesIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(languagesIntent);
        }

        //Time Complexity Activity
        if(position == 2) {
            Intent timeComplexityIntent = new Intent(getActivity().getApplicationContext(), TimeComplexityActivity.class);
            timeComplexityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(timeComplexityIntent);
        }

        //Unix Cheat Sheet Activity
        if(position == 3) {
        }

        //Data Containers Activity
        if(position == 4) {
            Intent dataContainersIntent = new Intent(getActivity().getApplicationContext(), DataContainerActivity.class);
            dataContainersIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(dataContainersIntent);
        }

        //SQL Cheat Sheet
        if(position == 5) {
        }

    }
}
