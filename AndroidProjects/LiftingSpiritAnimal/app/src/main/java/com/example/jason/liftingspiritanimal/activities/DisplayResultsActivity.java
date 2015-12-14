package com.example.jason.liftingspiritanimal.activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jason.liftingspiritanimal.R;
import com.example.jason.liftingspiritanimal.networking.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class DisplayResultsActivity extends AppCompatActivity {

    //UI components and other persistant variables
    private RequestQueue queue;
    private ImageView resultImage;
    private TextView resultTextView;
    private String animal;

    //constants
    private static final String API_KEY = "0eb90221661628f6b49c4b38e26009eb";
    private static final String IMG_ENDPOINT_START = "https://api.flickr.com/services/rest/?" +
            "format=json&nojsoncallback=1&sort=random&method=flickr.photos.search&tags=";
    private static final String IMG_ENDPOINT_END = "&tag_mode=all&api_key=";
    private static final String GET_REQUEST_TAG = "get_request";
    private static final String ANIMAL_STRING = "animal_string";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);

        //Add up navigation
        enableUpNavigation();

        //Create our Volley Queue object
        instantiateVolleyRequestQueue();

        //Reference UI components
        resultImage = (ImageView) findViewById(R.id.imageView);
        resultTextView = (TextView) findViewById(R.id.animalNameTextView);

        //Retrieve our passed in animal string
        animal = getAnimalStringFromIntent();

        //Get and show our image
        showImage(animal);
    }


    @Override
    protected void onStop() {
        super.onStop();

        //Cancel Request to prevent crash
        if(queue != null){
            queue.cancelAll(GET_REQUEST_TAG);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_display_results, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Get a new random image from Flickr
        if(item.getItemId() == R.id.newImageMenuButton && animal != null){
            showImage(animal);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void enableUpNavigation(){
        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    private String getAnimalStringFromIntent(){
        return this.getIntent().getStringExtra(ANIMAL_STRING);
    }


    private void instantiateVolleyRequestQueue(){
        //Start our queue and persist it through app lifetime
        queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
    }


    /*
        This method calls upon getRandomImage(), and handles some special cases of user input.
        This method serves primarily as a helper function and serves as a direct point to make
        http requests and UI handling.
     */
    private void showImage(String animal){
        //Create a string of tags to search Flickr with
        String searchTags;

        switch (animal) {
            case "Nothing":
                searchTags = "Nothing,lame";
                getRandomImage(searchTags);
                resultTextView.setText(R.string.nothing_string);
                break;
            case "999":
                resultImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                resultImage.setImageResource(R.drawable.nein_nein_nein);
                resultTextView.setText(R.string.nein_string);
                break;
            default:
                searchTags = animal.replace(" ","") + ",animal,mammal";
                getRandomImage(searchTags);

                //Set the textview with the corresponding animal
                resultTextView.setText(animal);
                break;
        }
    }


    /*
        This method retrieves a random image from Flickr's API associated with a String Tag(s).
        The tag(s) are then used to retrive an array of image data associated with it. We then
        use a randomly generated number to access an index of the array, choosing a random
        image data. We then use the image data to make a URL that points the the actual picture
        associated with that data. Afterwards, loadImage() is called, with the said URL passed
        as a parameter.
     */
    private void getRandomImage(String tag){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                IMG_ENDPOINT_START + tag + IMG_ENDPOINT_END + API_KEY, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try{
                    //Retrieve JSON array of all the image data queried
                    JSONArray images = response.getJSONObject("photos").getJSONArray("photo");

                    //Select a random image data item from the array
                    int index = new Random().nextInt(images.length());

                    //Create the image data as a JSON object
                    JSONObject imageItem = images.getJSONObject(index);

                    //Use the info from our image data item to fetch the actual image URL
                    String imageURL = "http://farm" + imageItem.getString("farm") +
                            ".static.flickr.com/" + imageItem.getString("server") +
                            "/" + imageItem.getString("id") + "_" +
                            imageItem.getString("secret") + "_" + "c.jpg";

                    //Load our image
                    loadImage(imageURL);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                displayError();
            }
        });

        request.setTag(GET_REQUEST_TAG);
        queue.add(request);
    }


    /*
        This method uses an image URl (sent to Flickr's API) to retrieve an image,
        then converts it to a bitmap, with the settings declared as final below.
        The request is made using volley, and the image is set to the UI upon
        successful http response. Otherwise, an error message is displayed.
     */
    private void loadImage(String imageURL){
        final int maxWidth = 0;
        final int maxHeight = 0;
        final ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;
        final Bitmap.Config config = Bitmap.Config.ARGB_8888;

        //Retrieves an image via the URL and displays it to the UI
        ImageRequest request = new ImageRequest(imageURL, new Response.Listener<Bitmap>() {

            @Override
            public void onResponse(Bitmap response) {

                //Set our image
                resultImage.setImageBitmap(response);
            }
        }, maxWidth, maxHeight, scaleType, config,

            new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                displayError();
            }
        });

        request.setTag(GET_REQUEST_TAG);
        queue.add(request);
    }


    private void displayError(){
        final String errorMessage = "Sorry, something went wrong";
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

}
