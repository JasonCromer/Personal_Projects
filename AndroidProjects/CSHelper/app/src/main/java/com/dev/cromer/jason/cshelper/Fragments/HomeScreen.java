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

import com.dev.cromer.jason.cshelper.Logic.ImageAdapter;
import com.dev.cromer.jason.cshelper.R;

public class HomeScreen extends Fragment {

    static View homeScreenView;
    static GridView gridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeScreenView = inflater.inflate(R.layout.fragment_ascii, container, false);

        gridView = (GridView) homeScreenView.findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(getActivity()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });

        return homeScreenView;
    }
}
