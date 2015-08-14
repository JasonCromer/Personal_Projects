package com.example.jason.healthcaremobileappdemo.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import com.example.jason.healthcaremobileappdemo.R;


public class ExerciseVideo extends Activity implements View.OnClickListener {

    static TextView exerciseTitleField;
    static TextView exerciseRepsSetsTimesPerDayTitleField;
    static VideoView videoView;
    static MediaController mediaController;
    //private final String videoPath = "";
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_video);

        Intent myIntent = getIntent();

        //Grab the exercise name passed by the RVAdapter sub-class PersonViewHolder's OnclickListener
        String exerciseName = myIntent.getStringExtra("EXERCISE_NAME");
        String repsSetsAndTimePerDay = myIntent.getStringExtra("REPS_SETS_TIMES_PER_DAY");

        exerciseTitleField = (TextView) findViewById(R.id.exerciseTitleField);
        exerciseTitleField.setText(exerciseName);

        exerciseRepsSetsTimesPerDayTitleField = (TextView) findViewById(R.id.repsSetsTimesPerDayTitleField);
        exerciseRepsSetsTimesPerDayTitleField.setText(repsSetsAndTimePerDay);


        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);


        videoView = (VideoView) findViewById(R.id.videoView);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        //videoView.setVideoURI(Uri.parse(videoPath));
        //videoView.start();
    }

    @Override
    public void onClick(View v) {
        if(v == backButton) {
            finish();
        }

    }
}
