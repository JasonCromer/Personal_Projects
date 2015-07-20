package com.example.jason.healthcaremobileappdemo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


public class ExerciseVideo extends Activity {

    private TextView exerciseTitleField;
    private VideoView videoView;
    private MediaController mediaController;
    private final String videoPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_video);

        Intent myIntent = getIntent();
        String exerciseName = myIntent.getStringExtra("Exercise Name");

        exerciseTitleField = (TextView) findViewById(R.id.exerciseTitleField);
        exerciseTitleField.setText(exerciseName);


        videoView = (VideoView) findViewById(R.id.videoView);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        //videoView.setVideoURI(Uri.parse(videoPath));
        //videoView.start();
    }

}
