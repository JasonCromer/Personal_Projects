package com.dev.cromer.jason.whatsappening.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dev.cromer.jason.whatsappening.R;

public class PostNewMarkerActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText markerTitleEditText;
    private Button setMarkerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_marker);

        markerTitleEditText = (EditText) findViewById(R.id.markerTitleEditText);
        setMarkerButton = (Button) findViewById(R.id.setMarkerButton);

        setMarkerButton.setOnClickListener(this);

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
    public void onClick(View v) {
        if(v == setMarkerButton){
            Intent resultIntent = new Intent();
            resultIntent.putExtra("TITLE", markerTitleEditText.getText().toString());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();

        }
    }
}
