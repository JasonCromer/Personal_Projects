package com.dev.cromer.jason.whatsappening.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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

        //Get last
        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        lastTitle = preferences.getString("lastTitle", "");
        setLastTitleTextView();

    }


    private void setLastTitleTextView() {
        lastTitleTextView.setText(lastTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_new_marker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE) {
            if(!markerTitleEditText.getText().toString().isEmpty()){
                //Set last title for the current activity
                lastTitle = markerTitleEditText.getText().toString();
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
        }
        return false;
    }
}
