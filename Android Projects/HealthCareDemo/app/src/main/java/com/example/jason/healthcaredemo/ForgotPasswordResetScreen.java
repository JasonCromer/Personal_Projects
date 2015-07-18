package com.example.jason.healthcaredemo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ForgotPasswordResetScreen extends ActionBarActivity implements View.OnClickListener {

    private EditText resetEmailField;
    private Button sendResetEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_reset_screen);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        resetEmailField = (EditText) findViewById(R.id.resetEmailField);
        sendResetEmailButton = (Button) findViewById(R.id.resetEmailButton);

        sendResetEmailButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v == sendResetEmailButton) {
            //Need to check for email validity here
            resetEmailField.setText("");
            Toast.makeText(getBaseContext(), "Reset email sent!", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
