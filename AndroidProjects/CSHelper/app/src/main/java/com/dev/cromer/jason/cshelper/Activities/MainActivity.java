package com.dev.cromer.jason.cshelper.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dev.cromer.jason.cshelper.Logic.CustomFragmentManager;
import com.dev.cromer.jason.cshelper.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Start the fragment manager class that manages all three tabs
        Intent startAsciiFragment = new Intent(this, CustomFragmentManager.class);
        startAsciiFragment.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(startAsciiFragment);
    }


}
