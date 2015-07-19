package com.example.jason.healthcaredemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent thisIntent = new Intent(this, LoginScreen.class);
        thisIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        thisIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(thisIntent);
    }

}
