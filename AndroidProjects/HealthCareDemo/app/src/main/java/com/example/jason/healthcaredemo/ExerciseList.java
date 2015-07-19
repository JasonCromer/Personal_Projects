package com.example.jason.healthcaredemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class ExerciseList extends Fragment {

    ListView listView;
    View fragmentView;

    private String[] itemName = {
            "Safari",
            "Camera",
            "Global",
            "FireFox",
            "UC Browser",
            "Android Folder",
            "VLC Player",
            "Cold War",
            "Safari",
            "Camera",
            "Global",
            "FireFox",

    };

    private Integer[] imageId = {
            R.drawable.pic1,
            R.drawable.pic2,
            R.drawable.pic3,
            R.drawable.pic4,
            R.drawable.pic5,
            R.drawable.pic6,
            R.drawable.pic7,
            R.drawable.pic8,
            R.drawable.pic9,
            R.drawable.pic10,
            R.drawable.pic11,
            R.drawable.pic12,

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentView = inflater.inflate(R.layout.activity_exercise_list, container, false);

        return fragmentView;

    }

    public void createListView() {

        CustomListAdapter adapter = new CustomListAdapter(getActivity(), itemName, imageId);
        listView = (ListView) fragmentView.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String selectedItem = itemName[position];
                Intent exerciseVideoIntent = new Intent(getActivity(), ExerciseVideo.class);
                exerciseVideoIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(exerciseVideoIntent);
            }
        });
    }

}
