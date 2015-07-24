package com.example.jason.healthcaremobileappdemo.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.example.jason.healthcaremobileappdemo.R;


public class ExerciseVideo extends Activity implements View.OnClickListener {

    private TextView exerciseTitleField;
    private VideoView videoView;
    private MediaController mediaController;
    private final String videoPath = "";
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_video);

        Intent myIntent = getIntent();
        String exerciseName = myIntent.getStringExtra("Exercise Name");

        exerciseTitleField = (TextView) findViewById(R.id.exerciseTitleField);
        exerciseTitleField.setText(exerciseName);

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
