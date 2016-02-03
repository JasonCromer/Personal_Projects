package com.dev.cromer.jason.whatshappening.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.dev.cromer.jason.whatshappening.R;
import com.dev.cromer.jason.whatshappening.logic.ShareMarkerHandler;
import com.dev.cromer.jason.whatshappening.networking.VolleyPostRequest;
import com.dev.cromer.jason.whatshappening.networking.VolleyQueueSingleton;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MarkerDescriptionActivity extends AppCompatActivity implements View.OnClickListener,
                                EditText.OnEditorActionListener, RequestQueue.RequestFinishedListener<Object> {

    private TextView markerDescriptionTextView;
    private TextView markerLikesTextView;
    private ImageButton likeButton;
    private String markerDescription = "";
    private String markerLikes = "";
    private ListView commentsListView;
    private String markerID;
    private LatLng markerPosition;
    private SharedPreferences preferences;
    private EditText userComment;
    private VolleyPostRequest volleyPostRequest;
    private RequestQueue queue;
    private boolean hasLiked = false;
    private boolean hasCommented = false;

    //constants
    private static final String DEFAULT_LIKES = "0";
    private static final String UPDATE_LIKES_TAG = "UPDATE_LIKES";
    private static final String UPDATE_COMMENTS_TAG = "UPDATE_COMMENTS";
    private static final String LIKED_STRING = "like";
    private static final String DISLIKED_STRING = "dislike";
    private static final String GET_COMMENTS_ENDPOINT = "http://whatsappeningapi.elasticbeanstalk.com/api/get_comments/";
    private static final String POST_COMMENT_ENDPOINT = "http://whatsappeningapi.elasticbeanstalk.com/api/post_comment/";
    private static final String GET_LIKES_ENDPOINT = "http://whatsappeningapi.elasticbeanstalk.com/api/get_marker_likes/";
    private static final String GET_DESCRIPTION_ENDPOINT = "http://whatsappeningapi.elasticbeanstalk.com/api/get_marker_description/";
    private static final String UPDATE_LIKES_ENDPOINT = "http://whatsappeningapi.elasticbeanstalk.com/api/update_marker_likes/";
    private static final boolean DEFAULT_HAS_LIKED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_description);

        //Instantiate our view objects
        instantiateViewObjects();

        //Instantiate our volley objects for http requests
        instantiateVolleyRequestObjects();

        //Get the ID and position from the marker thats been clicked on, on the map
        markerID = getIntent().getStringExtra("MARKER_ID");
        Bundle bundle = getIntent().getParcelableExtra("MARKER_LOCATION");
        markerPosition = bundle.getParcelable("LATLNG");

        //Get the shared preference to see if user has liked or disliked the post
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        checkUserLike();

        //getMarkerDescription and likes from database via passed in Marker ID
        getMarkerDescription();
        getMarkerLikes();

        //Display our comments
        getAndDisplayComments();
    }


    private void instantiateViewObjects(){
        markerLikesTextView = (TextView) findViewById(R.id.markerLikesTextView);
        markerDescriptionTextView = (TextView) findViewById(R.id.markerDescriptionTextView);
        likeButton = (ImageButton) findViewById(R.id.likeButton);
        commentsListView = (ListView) findViewById(R.id.commentsListView);
        userComment = (EditText) findViewById(R.id.userCommentEditText);

        //comment edit text at bottom of screen
        userComment.setOnEditorActionListener(this);

        //Like button (heart button)
        likeButton.setOnClickListener(this);
    }


    private void instantiateVolleyRequestObjects(){
        //Start our volley request queue to persists in application lifetime
        queue = VolleyQueueSingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();

        queue.addRequestFinishedListener(this);

        volleyPostRequest = new VolleyPostRequest(getApplicationContext());
    }


    private void getMarkerDescription(){
        final String url = GET_DESCRIPTION_ENDPOINT + markerID;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                markerDescription = response;
                markerDescription = markerDescription.replace("\"", "");

                //Display description in the textView
                displayMarkerDescription();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                displayErrorMessage(error);
            }
        });

        queue.add(request);
    }


    //Display the Marker Description we received. Handle null case
    private void displayMarkerDescription(){
        if(markerDescription != null){
            markerDescriptionTextView.setText(markerDescription);
        }
        else{
            markerDescriptionTextView.setText("");
        }
    }


    private void getMarkerLikes() {
        //This endpoint points to the markerLikes of the specific post
        final String url = GET_LIKES_ENDPOINT + markerID;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                markerLikes = response;

                //Display the likes in the textView
                displayMarkerLikes();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                displayErrorMessage(error);
            }
        });

        //Add our request to the queue
        queue.add(request);
    }


    private void displayErrorMessage(VolleyError error){
        //Convert error to NetworkResponse to get details
        NetworkResponse errorResponse = error.networkResponse;
        String errorResponseString = "Sorry, we ran into some network issues";

        if(errorResponse != null && errorResponse.data != null){
            Toast.makeText(getApplicationContext(), errorResponseString, Toast.LENGTH_LONG).show();
        }
    }


    private void displayMarkerLikes(){
        if(markerLikes != null){
            //Set our view
            markerLikesTextView.setText(markerLikes);
        }
        else{
            //Set our view
            markerLikesTextView.setText(DEFAULT_LIKES);
        }
    }


    private void updateMarkerLikes(String likeType){
        final String url = UPDATE_LIKES_ENDPOINT + markerID;

        //Create parameter HashMap to hold likeType key and the data (likes)
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("likeType", likeType);

        //Create Json request object using our params
        JsonObjectRequest request = volleyPostRequest.getRequestObject(url, paramsMap);

        //Set our tag to identify this Post request
        request.setTag(UPDATE_LIKES_TAG);

        //Add our request object to the Singleton Volley queue
        queue.add(request);
    }


    @Override
    public void onClick(View v) {
        if(v == likeButton && !hasLiked) {
            likeButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_heart_red));
            updateMarkerLikes(LIKED_STRING);
            saveUserLike();
            hasLiked = true;
        }
        else if(v == likeButton){
            likeButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_heart_outline_grey));
            updateMarkerLikes(DISLIKED_STRING);
            saveUserDislike();
            hasLiked = false;
        }
    }


    private void saveUserLike(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(markerID, true);
        editor.apply();
    }


    private void saveUserDislike(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(markerID, false);
        editor.apply();
    }


    private void checkUserLike(){
        final boolean userHasLiked = preferences.getBoolean(markerID, DEFAULT_HAS_LIKED);
        if(userHasLiked){

            //Change image to the filled-in heart image
            likeButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_heart_red));
            hasLiked = true;
        }
    }


    private void postNewComment(){
        final String url = POST_COMMENT_ENDPOINT + markerID;

        //Convert our EditText input to a String
        //Replace comma with tilde for GET response processing later
        final String comment = userComment.getText().toString().replaceAll(",","~");

        //Create comment HashMap to hold data
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("comment", comment);

        //Create a new request object using our hashmap and url
        JsonObjectRequest request = volleyPostRequest.getRequestObject(url, paramsMap);

        //Tag our request for post-processing
        request.setTag(UPDATE_COMMENTS_TAG);

        //Add our request object to the Singleton Volley queue
        queue.add(request);

        //Remove text from Edit Text
        userComment.setText("");
    }


    private void getAndDisplayComments(){

        //Retrieve comments from database and assign result to our ArrayList
        final String url = GET_COMMENTS_ENDPOINT + markerID;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Format our comment string into a list
                List<Spanned> commentsList = formatCommentList(response);

                //Create an adapter for our listView
                ArrayAdapter adapter = new ArrayAdapter<>(MarkerDescriptionActivity.this,
                        R.layout.comment_item, R.id.commentTextView, commentsList);

                //set our adapter with our list of comments to the listView
                commentsListView.setAdapter(adapter);

                if(hasCommented){
                    scrollToBottom();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                displayErrorMessage(error);
            }
        });

        queue.add(request);
    }


    private void scrollToBottom(){
        //smooth scroll to bottom of newly posted comment
        commentsListView.setSelection(commentsListView.getCount() - 1);
    }


    /*
        This method reverses manual encodings done to retain proper formatting
        when receiving information from the API. Tilde's are reversed back to
        comma's, and the "\n" newline from the Python API is replaced with
        two \n\n in Java Unicode.
     */
    private List<Spanned> formatCommentList(String input){
        List<String> commentsList;
        final String TILDE = "~";
        final String COMMA = ",";
        final String ITALICS_START = "<br><br/><i>";
        final String ITALICS_END = "</i>";
        final int SUBSTRING_START = 0;
        final int SUBSTRING_ITALICS_LENGTH = 28;

        if(input != null) {
            //We remove the brackets and quotations
            input = input.replace("[", "").replace("]", "").replace("\"", "");

            //Remove any additional white space with the regex \\s*
            commentsList = Arrays.asList(input.split("\\s*, \\s*"));


            //We replaced comma's with tilde's in the POST request, and now we reverse it
            for (int i = 0; i < commentsList.size(); i++) {
                if (commentsList.get(i).contains(TILDE)) {
                    String replacedString = commentsList.get(i).replace(TILDE, COMMA);
                    commentsList.set(i, replacedString);
                }

                //Replace "\n" string literal with two NewLine characters and italics
                if (commentsList.get(i).contains("\\n")) {
                    String replacedString = commentsList.get(i).replace("\\n", "");                 // replace("\\n", "\n\n");
                    replacedString = replacedString.substring(SUBSTRING_START, replacedString.length() - SUBSTRING_ITALICS_LENGTH)
                            + ITALICS_START
                            + replacedString.substring(replacedString.length() - SUBSTRING_ITALICS_LENGTH,
                            replacedString.length())
                            + ITALICS_END;
                    commentsList.set(i, replacedString);
                }
            }

            //Convert our list to a Spanned list and return
            return convertToSpannedList(commentsList);
        }

        else {
            //return empty list
            return new ArrayList<>();
        }
    }


    /*
        This method takes in a List of type String and converts it to a
        list of type Spanned
     */
    private List<Spanned> convertToSpannedList(List<String> stringList){

        //capacity of our list
        int listCapacity = stringList.size();

        //Spanned list with capacity
        List<Spanned> spannedList = Arrays.asList(new Spanned[listCapacity]);

        //Loop through and convert each String to a Spanned and add to new list
        for(int i = 0;i < listCapacity; i++){
            Spanned spannedString = Html.fromHtml(stringList.get(i));
            spannedList.set(i, spannedString);
        }

        return spannedList;

    }


    private void openShareService(){
        //Default text
        final String defaultMessage = "This is Whats Happening: ";

        //Default zoom level for map
        final int zoomLevel = 18;

        //Instantiate new object for creating the Share service
        ShareMarkerHandler shareMarkerHandler = new ShareMarkerHandler(this);

        //Pass in our parameters to share the marker of interest
        shareMarkerHandler.shareMarkerLocation(markerPosition, zoomLevel, defaultMessage +
                "\n" + markerDescription + "\n");
    }


    @Override
    protected void onStop() {
        super.onStop();

        //Destroy all items in our Volley request queue to prevent any crashes from View changes
        VolleyQueueSingleton.getInstance(this).destroyRequestQueue();
    }


    @Override
    public boolean onNavigateUp(){
        //Destroy our activity and return to top of the activity stack
        finish();
        return true;
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        //Booleans to determine whether the done button is pressed on keyboard, and comment is not empty
        final int isDonePressed = EditorInfo.IME_ACTION_DONE;
        final boolean isTextEmpty = userComment.getText().toString().isEmpty();

        if(actionId == isDonePressed && !isTextEmpty){

            //Post a new comment if our input isn't empty
            postNewComment();

            //Assert that we have commented, in which case, onResponse will scroll to bottom
            hasCommented = true;
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.socialMediaShare){
            //open the share service so user can share their marker of interest
            openShareService();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_marker_description, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRequestFinished(Request<Object> request) {
        if(request.getTag() == UPDATE_LIKES_TAG){

            //Update our likes here to ensure post has finished
            getMarkerLikes();
        }
        if(request.getTag() == UPDATE_COMMENTS_TAG){

            //Update our comments list to refresh newly added comments
            getAndDisplayComments();
        }
    }
}
