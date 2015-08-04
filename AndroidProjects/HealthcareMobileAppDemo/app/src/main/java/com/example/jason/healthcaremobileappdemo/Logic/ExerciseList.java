package com.example.jason.healthcaremobileappdemo.Logic;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.jason.healthcaremobileappdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ExerciseList extends Fragment {

    private View fragmentView;
    private List<Exercise> exercises;
    private RecyclerView rv;

    private String[] exerciseNames = {"Deep Squat","Alternating Deep Lunge","Shallow Squat"};
    private String[] timesPerDay = {"12x reps | 2x day","8x reps | 1x day","10x reps | 2x day"};
    private Random RAND = new Random();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.activity_exercise_list, container, false);

        rv = (RecyclerView) fragmentView.findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        initializeData();
        initializeAdapter();

        return fragmentView;
    }


    private void initializeData(){
        exercises = new ArrayList<>();
        //15 is number of cards
        for(int i = 0; i < 15; i++) {
            exercises.add(new Exercise(exerciseNames[RAND.nextInt(2)], timesPerDay[RAND.nextInt(2)], R.drawable.uxicon));
        }
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(exercises);
        rv.setAdapter(adapter);
    }
}
