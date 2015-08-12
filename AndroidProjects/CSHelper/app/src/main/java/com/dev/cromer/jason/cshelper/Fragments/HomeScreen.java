package com.dev.cromer.jason.cshelper.Fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.dev.cromer.jason.cshelper.Logic.GridViewAdapter;
import com.dev.cromer.jason.cshelper.Logic.GridViewItem;
import com.dev.cromer.jason.cshelper.R;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends Fragment {

    static View homeScreenView;
    static GridView gridView;

    private String[] imageTitles = {"Ascii Chart"};
    private Integer[] imageIds = {R.drawable.ascii_chart_icon};
    private List<GridViewItem> gridViewItemList;

    private int NUMBER_OF_GRID_ITEMS = imageTitles.length;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeScreenView = inflater.inflate(R.layout.activity_home_screen, container, false);

        gridView = (GridView) homeScreenView.findViewById(R.id.gridView);
        initializeData();
        initializeGridAdapter();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });

        return homeScreenView;
    }


    public void initializeData() {
        gridViewItemList = new ArrayList<>();
        for(int i = 0; i < NUMBER_OF_GRID_ITEMS; i++) {
            gridViewItemList.add(new GridViewItem(imageTitles[i], imageIds[i]));
        }
    }

    public void initializeGridAdapter() {
        GridViewAdapter adapter = new GridViewAdapter(getActivity(), gridViewItemList);
        gridView.setAdapter(adapter);
    }
}
