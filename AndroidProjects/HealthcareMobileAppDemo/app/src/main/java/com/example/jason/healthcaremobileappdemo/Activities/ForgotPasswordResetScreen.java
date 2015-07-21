package com.example.jason.healthcaremobileappdemo.Activities;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jason.healthcaremobileappdemo.R;


public class ForgotPasswordResetScreen extends Activity implements View.OnClickListener {

    private EditText resetEmailField;
    private Button sendResetEmailButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_reset_screen);

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

}