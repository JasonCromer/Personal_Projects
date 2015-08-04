package com.dev.cromer.jason.cshelper.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.cromer.jason.cshelper.Logic.DataStructureItem;
import com.dev.cromer.jason.cshelper.Logic.RVAdapter;
import com.dev.cromer.jason.cshelper.R;

import java.util.ArrayList;
import java.util.List;


public class DataStructures extends Fragment {

    private View dataStructuresView;
    private RecyclerView recyclerView;

    private List<DataStructureItem> dataStructuresList;
    private String[] dataStructureNames = {"Array", "Linked List", "Hash Table", "Binary Tree"};

    private int NUMBER_OF_CARDS = dataStructureNames.length;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataStructuresView = inflater.inflate(R.layout.fragment_data_structures, container, false);

        recyclerView = (RecyclerView) dataStructuresView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        initializeData();
        initializeAdapter();

        return dataStructuresView;
    }

    private void initializeData() {
        dataStructuresList = new ArrayList<>();
        for(int i = 0; i < NUMBER_OF_CARDS; i++) {
            dataStructuresList.add(new DataStructureItem(dataStructureNames[i]));
        }
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(dataStructuresList);
        recyclerView.setAdapter(adapter);

    }
}
