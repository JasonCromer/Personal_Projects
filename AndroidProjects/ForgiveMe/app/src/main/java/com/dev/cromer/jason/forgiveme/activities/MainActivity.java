package com.dev.cromer.jason.forgiveme.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dev.cromer.jason.forgiveme.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent homePageIntent = new Intent(this, HomePageActivity.class);
        homePageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homePageIntent);
    }
}
