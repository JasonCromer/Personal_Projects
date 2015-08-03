package com.dev.cromer.jason.cshelper.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.cromer.jason.cshelper.R;

import java.util.List;


public class DataStructures extends Fragment {

    private View dataStructuresView;
    private List<String> DataStructures;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataStructuresView = inflater.inflate(R.layout.fragment_data_structures, container, false);

        return dataStructuresView;
    }

}
