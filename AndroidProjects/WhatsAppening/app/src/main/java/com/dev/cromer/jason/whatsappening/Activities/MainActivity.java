package com.dev.cromer.jason.whatsappening.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker.PermissionResult;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dev.cromer.jason.whatsappening.R;

import java.lang.annotation.Annotation;

public class MainActivity extends AppCompatActivity implements PermissionResult {

    private static final String[] PERMS_ARRAY = {android.Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int INITIAL_REQUEST = 1337;
    private static final int PERMISSION_GRANTED_INT = 0;
    static boolean hasAcceptedMapAgreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        hasAcceptedMapAgreement = preferences.getBoolean("hasAccepted", false);

        if(hasAcceptedMapAgreement) {
            startIntent();
        }
        else {
            requestPermissions(PERMS_ARRAY, INITIAL_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == INITIAL_REQUEST && grantResults[0] == PERMISSION_GRANTED_INT) {

            //if user grants app request to use location, set hasAccepted to true
            SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("hasAccepted", true);
            editor.apply();
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
