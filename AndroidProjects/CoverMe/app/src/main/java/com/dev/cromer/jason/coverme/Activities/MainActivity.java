package com.dev.cromer.jason.coverme.Activities;

import android.content.Intent;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dev.cromer.jason.coverme.R;

import java.lang.annotation.Annotation;

public class MainActivity extends AppCompatActivity implements PermissionChecker.PermissionResult {

    private static final String[] PERMS_ARRAY = {android.Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int INITIAL_REQUEST = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions(PERMS_ARRAY, INITIAL_REQUEST);
        //Intent mapIntent = new Intent(this, MapActivity.class);
        //mapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //startActivity(mapIntent);
        //this.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == INITIAL_REQUEST) {
            Intent mapIntent = new Intent(this, MapActivity.class);
            mapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mapIntent);
            this.finish();

        }
        else {
            Log.d("TAG", "PERMISSION NOT GRANTED");
        }
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
