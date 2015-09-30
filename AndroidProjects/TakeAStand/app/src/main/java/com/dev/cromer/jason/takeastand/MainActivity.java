package com.dev.cromer.jason.takeastand;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String[] requestPerms = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int LOCATION_REQUEST_CODE = 1440;

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
            startMapActivity();
        }
        else{
            requestPermissions(requestPerms, LOCATION_REQUEST_CODE);
        }
    }


    private void startMapActivity(){
        Intent mapIntent = new Intent(getApplicationContext(), MapsActivity.class);
        mapIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mapIntent);
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_REQUEST_CODE) {
            if(permissions.length == 1 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startMapActivity();
            }
            else {
                requestPermission();
            }
        }
        else{
            finish();
        }
    }

}
