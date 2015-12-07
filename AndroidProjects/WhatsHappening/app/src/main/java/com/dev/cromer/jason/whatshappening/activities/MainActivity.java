package com.dev.cromer.jason.whatshappening.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.dev.cromer.jason.whatshappening.R;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String[] requestPerms = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int LOCATION_REQUEST_CODE = 100;
    private static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == LOCATION_REQUEST_CODE){

            //If our asked permission is asking to access fine location, and it is granted, proceed
            if(permissions.length == 1 && permissions[0].equals(LOCATION_PERMISSION)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startIntent();
            }
            else{
                //if our permission was denied, ask again
                requestPermission();
            }
        }
        else{
            //If request was not made, close the application
            finish();
        }
    }


    private void requestPermission(){

        //If the user has already granted permission, start our app
        if(ContextCompat.checkSelfPermission(this, LOCATION_PERMISSION)
                == PackageManager.PERMISSION_GRANTED){
            startIntent();
        }
        else{
            ActivityCompat.requestPermissions(this, requestPerms, LOCATION_REQUEST_CODE);
        }
    }

    private void startIntent() {
        Intent mapIntent = new Intent(this, MapActivity.class);
        mapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mapIntent);
        finish();
    }
}
