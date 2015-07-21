package com.example.jason.healthcaremobileappdemo.Logic;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jason.healthcaremobileappdemo.Logic.Person;
import com.example.jason.healthcaremobileappdemo.Logic.RVAdapter;
import com.example.jason.healthcaremobileappdemo.R;

import java.util.ArrayList;
import java.util.List;


public class ExerciseList extends Fragment {

    View fragmentView;
    private List<Person> persons;
    private RecyclerView rv;


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
        persons = new ArrayList<>();
        persons.add(new Person("Deep Squat", "12x reps | 2x day", R.drawable.uxicon));
        persons.add(new Person("Alternating Deep Lunge", "8x reps | 1x day", R.drawable.uxicon));
        persons.add(new Person("Shallow Squat", "10x reps | 2x day", R.drawable.uxicon));
        persons.add(new Person("Deep Squat", "12x reps | 2x day", R.drawable.uxicon));
        persons.add(new Person("Alternating Deep Lunge", "8x reps | 1x day", R.drawable.uxicon));
        persons.add(new Person("Shallow Squat", "10x reps | 2x day", R.drawable.uxicon));
        persons.add(new Person("Deep Squat", "12x reps | 2x day", R.drawable.uxicon));
        persons.add(new Person("Alternating Deep Lunge", "8x reps | 1x day", R.drawable.uxicon));
        persons.add(new Person("Shallow Squat", "10x reps | 2x day", R.drawable.uxicon));
        persons.add(new Person("Alternating Deep Lunge", "8x reps | 1x day", R.drawable.uxicon));
        persons.add(new Person("Deep Squat", "12x reps | 2x day", R.drawable.uxicon));
        persons.add(new Person("Shallow Squat", "10x reps | 2x day", R.drawable.uxicon));
        persons.add(new Person("Alternating Deep Lunge", "8x reps | 1x day", R.drawable.uxicon));
        persons.add(new Person("Shallow Squat", "10x reps | 2x day", R.drawable.uxicon));
        persons.add(new Person("Alternating Deep Lunge", "8x reps | 1x day", R.drawable.uxicon));
        persons.add(new Person("Deep Squat", "12x reps | 2x day", R.drawable.uxicon));
        persons.add(new Person("Shallow Squat", "10x reps | 2x day", R.drawable.uxicon));
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(persons);
        rv.setAdapter(adapter);
    }

}
