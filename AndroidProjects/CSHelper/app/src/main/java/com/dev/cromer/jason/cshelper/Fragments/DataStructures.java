package com.dev.cromer.jason.cshelper.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.cromer.jason.cshelper.Logic.DataStructureItem;
import com.dev.cromer.jason.cshelper.Logic.RecyclerViewAdapter;
import com.dev.cromer.jason.cshelper.R;

import java.util.ArrayList;
import java.util.List;


public class DataStructures extends Fragment {

    static View dataStructuresView;
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
        String[] dataStructureDescriptions = getDataStructureDescriptions();
        dataStructuresList = new ArrayList<>();
        for(int i = 0; i < NUMBER_OF_CARDS; i++) {
            dataStructuresList.add(new DataStructureItem(dataStructureNames[i], dataStructureDescriptions[i]));
        }
    }


    //This function passes the DataStructureItem List into the custom RecyclerViewAdapter
    private void initializeAdapter() {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(dataStructuresList);
        recyclerView.setAdapter(adapter);
    }


    private String[] getDataStructureDescriptions() {
        String[] dataStructureDescriptions = {
                "An array is a data structure consisting of a collection of elements (values or variables), each identified by at least one array index " +
                        "or key. An array is stored so that the position of each element can be computed from its index tuple by a " +
                        "mathematical formula.[1][2][3] The simplest type of data structure is a linear array, also called one-dimensional array.",
                "A Dynamic Array is a random access, variable-size list data structure that allows elements to be added or removed. It " +
                        "is supplied with standard libraries in many modern mainstream programming languages.\n" + "A dynamic array is not the " +
                        "same thing as a dynamically allocated array, which is an array whose size is fixed when the array is " +
                        "allocated, although a dynamic array may use such a fixed-size array as a back end.",
                "A lookup table is an array that replaces runtime computation with a simpler array indexing operation. The savings in terms of " +
                        "processing time can be significant, since retrieving a value from memory is often faster than undergoing an " +
                        "'expensive' computation or input/output operation.",
                "A bitmap is a mapping from some domain (for example, a range of integers) to bits, that is, values which are zero or one. " +
                        "It is also called a bit array or bitmap index.",
                "A linked list is a data structure consisting of a group of nodes which together represent a sequence. Under the simplest form," +
                        " each node is composed of data and a reference (in other words, a link) to the next node in the sequence; more complex " +
                        "variants add additional links. This structure allows for efficient insertion or removal of elements from any position " +
                        "in the sequence.",
                "A doubly-linked list is a linked data structure that consists of a set of sequentially linked records called nodes. Each node " +
                        "contains two fields, called links, that are references to the previous and to the next node in the sequence of nodes. " +
                        "The beginning and ending nodes' previous and next links, respectively, point to some kind of terminator, typically a " +
                        "sentinel node or null, to facilitate traversal of the list. If there is only one sentinel node, then the list is " +
                        "circularly linked via the sentinel node. It can be conceptualized as two singly linked lists formed from the same " +
                        "data items, but in opposite sequential orders.",


        };

        return dataStructureDescriptions;
    }

}
