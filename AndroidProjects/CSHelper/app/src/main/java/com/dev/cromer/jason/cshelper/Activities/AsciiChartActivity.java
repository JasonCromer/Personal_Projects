package com.dev.cromer.jason.cshelper.Activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.dev.cromer.jason.cshelper.R;


public class AsciiChartActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ascii);

        backButton = (ImageView) findViewById(R.id.asciiChartBackButton);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == backButton) {
            finish();
        }

    }
}
