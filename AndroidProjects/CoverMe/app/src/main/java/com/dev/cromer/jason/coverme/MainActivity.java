package com.dev.cromer.jason.coverme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent mapIntent = new Intent(this, MapActivity.class);
        mapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mapIntent);
    }

}
