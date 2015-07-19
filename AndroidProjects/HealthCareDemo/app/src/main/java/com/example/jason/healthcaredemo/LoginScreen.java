package com.example.jason.healthcaredemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameField;
    private EditText passwordField;
    private Button loginButton;
    private TextView forgotPasswordLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        usernameField = (EditText) findViewById(R.id.usernameField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        loginButton = (Button) findViewById(R.id.loginButton);
        forgotPasswordLink = (TextView) findViewById(R.id.forgotPasswordLink);

        loginButton.setOnClickListener(this);
        forgotPasswordLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v == loginButton) {
            if(usernameField.getText().toString().isEmpty() || passwordField.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Username or password field is empty", Toast.LENGTH_SHORT).show();
            }
            else if(usernameField.getText().toString().isEmpty() && passwordField.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Username or password field is empty", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Logged in!", Toast.LENGTH_LONG).show();
                usernameField.setText("");
                passwordField.setText("");
                Intent loginIntent = new Intent(this, ExerciseList.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
            }
        }
        if(v == forgotPasswordLink) {
            Intent resetPasswordIntent = new Intent(this, ForgotPasswordResetScreen.class);
            startActivity(resetPasswordIntent);

        }

    }
}
