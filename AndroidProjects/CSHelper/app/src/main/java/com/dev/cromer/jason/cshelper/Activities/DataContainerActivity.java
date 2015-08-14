package com.dev.cromer.jason.cshelper.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dev.cromer.jason.cshelper.R;

public class DataContainerActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView thisBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_container);

        thisBackButton = (ImageView) findViewById(R.id.dataContainerBackButton);
        thisBackButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == thisBackButton) {
            finish();
        }
    }
}
