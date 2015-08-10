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
    private String[] dataStructureNames = {"Array", "Dynamic Array", "Lookup Table", "Bitmap",
            "Linked List", "Doubly Linked List", "Binary Tree", "B-Tree", "Red-Black Tree",
            "AVL Tree", "Bloom Filter", "Hash List", "Hash Table"};

    private int NUMBER_OF_CARDS = dataStructureNames.length;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataStructuresView = inflater.inflate(R.layout.fragment_data_structures, container, false);

        //Add recycler view to the layout inflater
        recyclerView = (RecyclerView) dataStructuresView.findViewById(R.id.recyclerView);

        //set a fixed size so size of the view cannot be adjusted
        recyclerView.setHasFixedSize(true);

        //Create a Linear Layout manager to manage recycler view layout
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        initializeData();
        initializeAdapter();

        return dataStructuresView;
    }



    //This function creates a list of DataStructureItems
    private void initializeData() {
        dataStructuresList = new ArrayList<>();
        for(int i = 0; i < NUMBER_OF_CARDS; i++) {
            dataStructuresList.add(new DataStructureItem(dataStructureNames[i]));
        }
    }


    //This function passes the DataStructureItem List into the custom RVAdapter
    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(dataStructuresList);
        recyclerView.setAdapter(adapter);
    }

}
