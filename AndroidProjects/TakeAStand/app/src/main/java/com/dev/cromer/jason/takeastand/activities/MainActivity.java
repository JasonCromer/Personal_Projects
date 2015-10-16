package com.dev.cromer.jason.takeastand.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dev.cromer.jason.takeastand.R;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String[] requestPerms = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int LOCATION_REQUEST_CODE = 1440;
    private static final String SHARED_PREFS_TITLE = "FIRST_TIME_USER";
    private static final boolean SHARED_PREFS_DEFAULT = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Request Location if app hasn't done so already
        requestPermission();
    }


    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            startIntent();
        }
        else{
            //if API is 23 or greater, use default requestPermissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(requestPerms, LOCATION_REQUEST_CODE);
            }
            else{
                //Otherwise, use the backwards compatible ActivityCompat requestPermissions
                ActivityCompat.requestPermissions(this, requestPerms, LOCATION_REQUEST_CODE);
            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_REQUEST_CODE) {
            if(permissions.length == 1 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startIntent();
            }
            else {
                requestPermission();
            }
        }
        else{
            finish();
        }
    }


    private void startIntent(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final Boolean isFirstTimeUser = preferences.getBoolean(SHARED_PREFS_TITLE, SHARED_PREFS_DEFAULT);

        if(isFirstTimeUser){
            startScreenIntent();
        }
        else {
            startMapIntent();
        }
    }


    private void startScreenIntent(){
        Intent intent = new Intent(getApplicationContext(), StartScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void startMapIntent(){
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
