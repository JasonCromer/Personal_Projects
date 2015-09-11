package com.dev.cromer.jason.whatsappening.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.dev.cromer.jason.whatsappening.R;

public class SetMarkerTitleActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    private EditText markerTitleEditText;
    private TextView lastTitleTextView;
    private String lastTitle = "";
    private static final int MARKER_DESCRIPTION_REQ_CODE = 3;
    private String markerDescription = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_marker);


        markerTitleEditText = (EditText) findViewById(R.id.markerTitleEditText);
        lastTitleTextView = (TextView) findViewById(R.id.lastTitleTextView);

        markerTitleEditText.setOnEditorActionListener(this);

        //Get last title posted by the user
        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        lastTitle = preferences.getString("lastTitle", "");
        setLastTitleTextView();
        showKeyboard();
    }


    private void setLastTitleTextView() {
        lastTitleTextView.setText(lastTitle);
    }


    private void showKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_NEXT) {
            if(!markerTitleEditText.getText().toString().isEmpty()){

                //Set title for the current posted marker
                lastTitle = markerTitleEditText.getText().toString();
                //Save the title for future use
                SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("lastTitle", lastTitle);
                editor.apply();

                //Open intent to create description
                startDescriptionIntent();

                //close keyboard and finish onEditorAction call
                return true;
            }
            else{
                //close intent if user presses done on empty input
                finish();
                return false;
            }
        }
        return false;
    }


    private void startDescriptionIntent(){
        Intent descriptionIntent = new Intent(getApplicationContext(), SetMarkerDescriptionActivity.class);
        startActivityForResult(descriptionIntent, MARKER_DESCRIPTION_REQ_CODE);
    }


    private void startResultIntent(){
        //Create new intent to pass title to Map Activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("TITLE", markerTitleEditText.getText().toString());
        resultIntent.putExtra("MARKER_DESCRIPTION", markerDescription);
        setResult(Activity.RESULT_OK, resultIntent);

        //close this activity
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case(MARKER_DESCRIPTION_REQ_CODE):
                if(resultCode == Activity.RESULT_OK){
                    //retrieve description from previous intent and set it to class-local string
                    markerDescription = data.getStringExtra("MARKER_DESCRIPTION");

                    //Start intent to take us back to MapActivity
                    startResultIntent();
                }
                break;
        }
    }
}
