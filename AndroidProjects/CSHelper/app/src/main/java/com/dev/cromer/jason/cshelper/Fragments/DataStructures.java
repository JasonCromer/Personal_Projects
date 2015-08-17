package com.dev.cromer.jason.cshelper.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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
                "An Array is a data structure consisting of a collection of elements (values or variables), each identified by at least one array index " +
                        "or key. An array is stored so that the position of each element can be computed from its index tuple by a " +
                        "mathematical formula. The simplest type of data structure is a linear array, also called one-dimensional array.",
                "A Dynamic Array is a random access, variable-size list data structure that allows elements to be added or removed." +
                        "A dynamic array is not the " + "same thing as a dynamically allocated array, which is an array whose size is " +
                        "fixed when the array is " + "allocated, although a dynamic array may use such a fixed-size array as a back end.",
                "A lookup table is an array that replaces runtime computation with a simpler array indexing operation. The savings in terms of " +
                        "processing time can be significant, since retrieving a value from memory is often faster than undergoing an " +
                        "'expensive' computation or input/output operation.",
                "A Bitmap is a mapping from some domain (for example, a range of integers) to bits, that is, values which are zero or one. " +
                        "It is also called a bit array or bitmap index.",
                "A Linked List is a data structure consisting of a group of nodes which together represent a sequence. Under the simplest form," +
                        " each node is composed of data and a reference (in other words, a link) to the next node in the sequence; more complex " +
                        "variants add additional links. This structure allows for efficient insertion or removal of elements from any position " +
                        "in the sequence.",
                "A Doubly-Linked List is a linked data structure that consists of a set of sequentially linked records called nodes. Each node " +
                        "contains two fields, called links, that are references to the previous and to the next node in the sequence of nodes. " +
                        "The beginning and ending nodes' previous and next links, respectively, point to some kind of terminator, typically a " +
                        "sentinel node or null, to facilitate traversal of the list. If there is only one sentinel node, then the list is " +
                        "circularly linked via the sentinel node.",
                "A Binary Tree is a tree data structure in which each node has at most two children, which are referred to as the left child " +
                        "and the right child. ",
                "a B-Tree is a tree data structure that keeps data sorted and allows searches, sequential access, insertions, and deletions in " +
                        "logarithmic time.",
                "A Red–Black Tree is a binary search tree with an extra bit of data per node, its color, which can be either red or black. " +
                        "The extra bit of storage ensures an approximately balanced tree by constraining how nodes are colored from any path " +
                        "from the root to the leaf. Thus, it is a data structure which is a type of self-balancing binary search tree.",
                "An AVL tree is a self-balancing binary search tree. In an AVL tree, the heights of the two child " +
                        "subtrees of any node differ by at most one; if at any time they differ by more than one, rebalancing is done to " +
                        "restore this property. Lookup, insertion, and deletion all take O(log n) time in both the average and worst cases, " +
                        "where n is the number of nodes in the tree prior to the operation. Insertions and deletions may require the tree to be " +
                        "rebalanced by one or more tree rotations.",
                "A Bloom filter is a space-efficient probabilistic data structure that is used to test whether an element is a member of a set. " +
                        "False positive matches are possible, but false negatives are not, thus a Bloom filter has a 100% recall rate. In " +
                        "other words, a query returns either 'possibly in set' or 'definitely not in set'. Elements can be added to the set, " +
                        "but not removed. The more elements that are added to the set, the larger the probability of false positives.",
                "A Hash List is typically a list of hashes of the data blocks in a file or set of files. Lists of hashes " +
                        "are used for many different purposes, such as fast table lookup (hash tables) and distributed databases " +
                        "(distributed hash tables).",
                "A Hash Table (hash map) is a data structure used to implement an associative array, a structure that can map keys to values. " +
                        "A hash table uses a hash function to compute an index into an array of buckets or slots, from which the desired " +
                        "value can be found."
        };

        return dataStructureDescriptions;
    }

}
