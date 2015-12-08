package com.example.jason.liftingspiritanimal;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jason.liftingspiritanimal.networking.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class DisplayResultsActivity extends AppCompatActivity{

    private RequestQueue queue;
    private ImageView resultImage;
    private TextView resultTextView;

    //constants
    private static final String API_KEY = "0eb90221661628f6b49c4b38e26009eb";
    private static final String IMG_ENDPOINT_START = "https://api.flickr.com/services/rest/?" +
            "format=json&nojsoncallback=1&sort=random&method=flickr.photos.search&tags=";
    private static final String IMG_ENDPOINT_END = "&tag_mode=all&api_key=";
    private static final String GET_REQUEST_TAG = "get_request";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);

        //Add up navigation
        enableUpNavigation();

        instantiateVolleyRequestQueue();

        resultImage = (ImageView) findViewById(R.id.imageView);
        resultTextView = (TextView) findViewById(R.id.animalNameTextView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        getRandomImage("Koala,animal,mammal");
    }

    private void enableUpNavigation(){
        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    private void instantiateVolleyRequestQueue(){
        //Start our queue and persist it through app lifetime
        queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
    }


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
