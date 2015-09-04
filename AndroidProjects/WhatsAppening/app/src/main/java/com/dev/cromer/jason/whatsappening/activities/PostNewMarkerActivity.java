package com.dev.cromer.jason.whatsappening.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.dev.cromer.jason.whatsappening.R;

public class PostNewMarkerActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    private EditText markerTitleEditText;
    private TextView lastTitleTextView;
    private String lastTitle = "";

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
        if(actionId == EditorInfo.IME_ACTION_DONE) {
            if(!markerTitleEditText.getText().toString().isEmpty()){

                //Set title for the current posted marker
                lastTitle = markerTitleEditText.getText().toString();
                //Save the title for future use
                SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("lastTitle", lastTitle);
                editor.apply();

                //Create new intent to pass title to Map Activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("TITLE", markerTitleEditText.getText().toString());
                setResult(Activity.RESULT_OK, resultIntent);

                //close this activity
                finish();
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
}
