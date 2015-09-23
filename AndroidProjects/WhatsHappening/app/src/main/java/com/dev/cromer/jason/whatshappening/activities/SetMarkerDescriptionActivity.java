package com.dev.cromer.jason.whatshappening.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.dev.cromer.jason.whatshappening.R;


public class SetMarkerDescriptionActivity extends AppCompatActivity implements TextView.OnEditorActionListener{

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
            setResultOK();

            //close activity
            finish();
            return true;
        }
        return false;
    }


    private void setResultOK(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("MARKER_DESCRIPTION", markerDescription.getText().toString());
        setResult(Activity.RESULT_OK, resultIntent);
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_set_marker_description, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_send:
                setResultOK();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
