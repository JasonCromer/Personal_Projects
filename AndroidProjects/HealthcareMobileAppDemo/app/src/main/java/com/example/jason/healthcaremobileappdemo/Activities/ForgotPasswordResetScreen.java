package com.example.jason.healthcaremobileappdemo.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.jason.healthcaremobileappdemo.R;


public class ForgotPasswordResetScreen extends Activity implements View.OnClickListener {

    private EditText resetEmailField;
    private Button sendResetEmailButton;
    private ImageButton backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_reset_screen);

        resetEmailField = (EditText) findViewById(R.id.resetEmailField);

        sendResetEmailButton = (Button) findViewById(R.id.resetEmailButton);
        sendResetEmailButton.setOnClickListener(this);

        backButton = (ImageButton) findViewById(R.id.backToLoginScreenButton);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v == sendResetEmailButton) {
            //Need to check for email validity here
            resetEmailField.setText("");
            Toast.makeText(getBaseContext(), "Reset email sent!", Toast.LENGTH_SHORT).show();
        }
        if(v == backButton) {
            Intent backToHomeIntent = new Intent(this, LoginScreen.class);
            backToHomeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(backToHomeIntent);
        }

    }

}