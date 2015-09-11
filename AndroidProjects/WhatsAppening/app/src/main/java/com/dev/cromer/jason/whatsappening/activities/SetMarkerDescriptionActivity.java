package com.dev.cromer.jason.whatsappening.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.dev.cromer.jason.whatsappening.R;

public class SetMarkerDescriptionActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    private EditText markerDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_marker_description);

        markerDescription = (EditText) findViewById(R.id.markerDescriptionEditText);
        markerDescription.setOnEditorActionListener(this);

        showKeyboard();
    }


    private void showKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE){
            Intent resultIntent = new Intent();
            resultIntent.putExtra("MARKER_DESCRIPTION", markerDescription.getText().toString());
            setResult(Activity.RESULT_OK, resultIntent);

            //close activity
            finish();
            return true;
        }
        return false;
    }
}
