package com.dev.cromer.jason.takeastand.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.dev.cromer.jason.takeastand.Logic.UpdateNumUsersHandler;
import com.dev.cromer.jason.takeastand.networking.GenericHttpGetRequest;
import com.dev.cromer.jason.takeastand.R;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.concurrent.ExecutionException;

public class StartScreenActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    static ArrayAdapter<String> spinnerArrayAdapter;
    static String recievedNumberOfUsers;
    private String response;
    private int userSelectedChoice;
    private boolean itemSelected;
    private MenuItem sendButton;
    private Spinner religionSpinner;
    private TextView numOfUsersTextView;
    private String[] spinnerItems = {"Choose Your Religion", "Christian", "Islam", "Catholic", "Hindu", "Buddhist", "Agnostic", "Athiest"};

    //constants
    private static final String GET_NUM_USERS_URL = "http://takeastandapi.elasticbeanstalk.com/get_users";
    private static final String UPDATE_NUM_USERS_URL = "http://takeastandapi.elasticbeanstalk.com/update_num_users";
    private static final String USER_RELIGION_CHOICE_EXTRA = "USER_CHOICE_EXTRA";
    private static final int DEFAULT_SPINNER_ITEM_POSITION = 0;
    private static final String DEFAULT_TEXT_VIEW_NUM_VALUE = "many";
    private static final String HTML_COLOR_WRAP_START = "<font color='#FDD835'>";
    private static final String HTML_COLOR_WRAP_END = "</font>";
    private static final String TEXT_VIEW_START = "Pick your religion and join ";
    private static final String TEXT_VIEW_END = " other users around the world to take a stand";
    private static final boolean SHARED_PREFS_BOOLEAN = false;
    private static final String SHARED_PREFS_TITLE = "FIRST_TIME_USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        int googleServiceStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this,googleServiceStatus, 10);
        if(dialog!=null){
            dialog.show();
        }

        //set itemSelected to false as no items have been selected in onCreate
        itemSelected = false;

        numOfUsersTextView = (TextView) findViewById(R.id.numOfUsersTextView);
        religionSpinner = (Spinner) findViewById(R.id.spinner);
        setUpSpinnerAdapter();

        //set up the textview to show number of users
        setNumberOfUsersTextView();
    }

    private void setUpSpinnerAdapter() {
        //Create new instance of the spinner item
        spinnerArrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.custom_spinner_item,
                spinnerItems);

        //Use a custom dropdown with the spinner
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
        religionSpinner.setAdapter(spinnerArrayAdapter);
        religionSpinner.setOnItemSelectedListener(this);
    }

    //This method hides the "send" button on the menu
    private void hideMenuSendButton(){
        sendButton.setVisible(false);
        invalidateOptionsMenu();
    }

    //This method shows the "send" button on the menu
    private void showMenuSendButton() {
        sendButton.setVisible(true);
        invalidateOptionsMenu();
    }

    //This method opens the map intent to display the Google Map
    private void startMapIntent(){
        Intent mapIntent = new Intent(this, MapsActivity.class);

        //add the users choice as an extra to pass into the map activity
        mapIntent.putExtra(USER_RELIGION_CHOICE_EXTRA, getUserSelectedChoice());

        startActivity(mapIntent);
        finish();
    }


    /*
        This method creates a generic httpRequest to a constant URL that
        retrieves the number of users in the server database.
        @return String (If response is not null)
        @return null (if http connection failed)
     */
    private String getNumberOfUsers(){
        GenericHttpGetRequest httpGetRequest = new GenericHttpGetRequest();

        //Connect and attempt to get users from url
        try{
            //We are recieving a string, but need to convert it to an integer
            response = httpGetRequest.execute(GET_NUM_USERS_URL).get();
        }
        catch(ExecutionException | InterruptedException e){
            e.printStackTrace();
        }

        if(response != null){
            return response;
        }
        else{
            return null;
        }
    }

    /*
        This method sets the number of users based on the getNumberOfUsers() function.
        If the returned String is not null, we add the number to our textview.
        If the returned String is null, we set the text view to a default value
     */
    private void setNumberOfUsersTextView(){
        recievedNumberOfUsers = getNumberOfUsers();

        //Check for bad return
        if(recievedNumberOfUsers != null){
            final String formattedString = TEXT_VIEW_START + HTML_COLOR_WRAP_START + recievedNumberOfUsers
                                                + HTML_COLOR_WRAP_END + TEXT_VIEW_END;
            numOfUsersTextView.setText(Html.fromHtml(formattedString));
        }
        else{
            final String formattedString = TEXT_VIEW_START + DEFAULT_TEXT_VIEW_NUM_VALUE + TEXT_VIEW_END;
            numOfUsersTextView.setText(formattedString);
        }
    }


    private void updateNumberOfUsers(){
        //Create request and handler objects
        final GenericHttpGetRequest httpGetRequest = new GenericHttpGetRequest();
        final UpdateNumUsersHandler numUsersHandler = new UpdateNumUsersHandler(UPDATE_NUM_USERS_URL, httpGetRequest);

        //Update the number of users in the database
        numUsersHandler.updateNumberOfUsers();
    }


    private void saveFirstTimeUserInfo(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.edit().putBoolean(SHARED_PREFS_TITLE, SHARED_PREFS_BOOLEAN).apply();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_screen, menu);

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

        if (id == R.id.action_send) {
            updateNumberOfUsers();
            saveFirstTimeUserInfo();
            startMapIntent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //if we choose any options other than the default, show the send button in the menu
        if(position > DEFAULT_SPINNER_ITEM_POSITION){
            itemSelected = true;

            //Set the user selected choice for the class instance
            setUserSelectedChoice(position);
            showMenuSendButton();
        }
        //Hide button if default item is selected
        else if(position == DEFAULT_SPINNER_ITEM_POSITION){
            itemSelected = false;
            hideMenuSendButton();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    public void setUserSelectedChoice(int selectedChoice){
        this.userSelectedChoice = selectedChoice;
    }

    public int getUserSelectedChoice(){
        return this.userSelectedChoice;
    }


}
