package com.example.jason.healthcaremobileappdemo.Logic;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jason.healthcaremobileappdemo.Activities.ExerciseVideo;
import com.example.jason.healthcaremobileappdemo.R;

import java.util.List;

/**
 * Created by jason on 7/19/15.
 * This class provides a custom adapter for populating the recycler view layout via a card item.
 * A static class is created to hold the data of the card item, namely ExerciseViewHolder.
 * The RVAdapater class then inflates the card item via the ExerciseViewHolder data,
 * instead of searching for each item, on each card, each time the class is inflated.
 * This is done via a ViewHolder (ExerciseViewHolder) to improve performance and is required
 * by the RecyclerView Class.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ExerciseViewHolder> {

    //Create a list of data that the exercises object will contain
    private List<Exercise> exercises;


    //This class creates the data necessary for the RVAdapter to use
    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {

        private CardView cv;
        private TextView exerciseName;
        private TextView exerciseRepsSetsAndTimesPerDay;
        private ImageView exerciseImage;

        //Constructor for ExerciseViewHolder
        ExerciseViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            exerciseName = (TextView)itemView.findViewById(R.id.exerciseName);
            exerciseRepsSetsAndTimesPerDay = (TextView)itemView.findViewById(R.id.exerciseRepsSetsAndTimesPerDay);
            exerciseImage = (ImageView)itemView.findViewById(R.id.exerciseImage);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent exerciseVideoIntent = new Intent(itemView.getContext(), ExerciseVideo.class);
                    exerciseVideoIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //Add exercise name to the ExerciseVideo title as an intent parameter
                    exerciseVideoIntent.putExtra("EXERCISE_NAME", exerciseName.getText().toString());
                    exerciseVideoIntent.putExtra("REPS_SETS_TIMES_PER_DAY", exerciseRepsSetsAndTimesPerDay.getText().toString());
                    itemView.getContext().startActivity(exerciseVideoIntent);
                }
            });
        }
    }


    //Constructor for RVAdapter instantiates the exercises object
    RVAdapter(List<Exercise> exercises){
        this.exercises = exercises;
    }


    //Inflate the layout by grabbing the card_view_item layout, and instantiating a new viewholder(ExerciseViewHolder)
    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_item, viewGroup, false);
        ExerciseViewHolder pvh = new ExerciseViewHolder(v);

        return pvh;
    }


    //Use the PersonViewHolder to populate the card via the Recycler View
    @Override
    public void onBindViewHolder(ExerciseViewHolder personViewHolder, int i) {
        personViewHolder.exerciseName.setText(exercises.get(i).name);
        personViewHolder.exerciseRepsSetsAndTimesPerDay.setText(exercises.get(i).repsSetsAndTimesPerDay);
        personViewHolder.exerciseImage.setImageResource(exercises.get(i).photoId);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    @Override
    public void onViewAttachedToWindow(ExerciseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }
}
