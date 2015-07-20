package com.example.jason.healthcaremobileappdemo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


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
