package com.dev.cromer.jason.coverme.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dev.cromer.jason.coverme.R;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements PermissionChecker.PermissionResult {

    private static final String[] PERMS_ARRAY = {android.Manifest.permission.ACCESS_FINE_LOCATION};
    private static final String permission = "android.Manifest.permission.ACCESS_FINE_LOCATION";
    private static final int INITIAL_REQUEST = 1337;
    private static final int PERMISSION_GRANTED_INT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getApplicationContext().checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            startIntent();
        }
        else {
            requestPermissions(PERMS_ARRAY, INITIAL_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == INITIAL_REQUEST && grantResults[0] == 0) {
            startIntent();
        }
        else {
            finish();
        }
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }


    private void startIntent() {
        Intent mapIntent = new Intent(this, MapActivity.class);
        mapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mapIntent);
        this.finish();
    }
}
