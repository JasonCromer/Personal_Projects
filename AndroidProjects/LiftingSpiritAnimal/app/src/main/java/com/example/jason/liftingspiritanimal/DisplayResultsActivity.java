package com.example.jason.liftingspiritanimal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DisplayResultsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);

        //Add up navigation
        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }



}
