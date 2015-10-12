package com.dev.cromer.jason.takeastand.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.dev.cromer.jason.takeastand.R;

public class StartScreenActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    static ArrayAdapter<String> spinnerArrayAdapter;
    private String userSelectedChoice;
    private boolean itemSelected;
    private MenuItem sendButton;
    private Spinner religionSpinner;
    private TextView numOfUsersTextView;
    private String[] spinnerItems = {"Choose Your Religion", "Christian", "Islam", "Catholic", "Hindu", "Buddhist", "Agnostic", "Athiest"};

    //constants
    private static final String USER_RELIGION_CHOICE_EXTRA = "USER_CHOICE_EXTRA";
    private static final int DEFAULT_ITEM_POSITION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_creation);

        //set itemSelected to false as no items have been selected in onCreate
        itemSelected = false;

        numOfUsersTextView = (TextView) findViewById(R.id.numOfUsersTextView);
        religionSpinner = (Spinner) findViewById(R.id.spinner);
        setUpSpinnerAdapter();
    }


    private void setUpSpinnerAdapter() {
        spinnerArrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.custom_spinner_item,
                spinnerItems);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
        religionSpinner.setAdapter(spinnerArrayAdapter);
        religionSpinner.setOnItemSelectedListener(this);
    }

    private void hideMenuSendButton(){
        sendButton.setVisible(false);
        invalidateOptionsMenu();
    }

    private void showMenuSendButton() {
        sendButton.setVisible(true);
        invalidateOptionsMenu();
    }

    private void startMapIntent(String chosenReligion){
        Intent mapIntent = new Intent(this, MapsActivity.class);

        //add the users choice as an extra to pass into the map activity
        mapIntent.putExtra(USER_RELIGION_CHOICE_EXTRA, chosenReligion);

        startActivity(mapIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_marker_creation, menu);

        //Instantiate send button and assign to object via id
        sendButton = menu.findItem(R.id.action_send);

        //We set to false instead of calling hideMenuSendButton to prevent invalidation() call
        if(!itemSelected){
            sendButton.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send) {
            startMapIntent(userSelectedChoice);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position > DEFAULT_ITEM_POSITION){
            itemSelected = true;
            userSelectedChoice = parent.getItemAtPosition(position).toString();
            showMenuSendButton();
        }
        //Hide button if default item is selected
        else if(position == DEFAULT_ITEM_POSITION){
            itemSelected = false;
            hideMenuSendButton();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
