package com.dev.cromer.jason.whatshappening.networking;


import android.content.Context;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class VolleyPostRequest implements Response.Listener<JSONObject>, Response.ErrorListener{

    //Application Context to persist RequestQueue and perform Toast messages
    private Context applicationContext;


    //Constructor
    public VolleyPostRequest(Context appContext){
        this.applicationContext = appContext;
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        //Convert our error to a NetworkResponse to get more details
        NetworkResponse errorResponse = error.networkResponse;
        String errorResponseString = "Sorry, we ran into some network issues";

        if(errorResponse != null && errorResponse.data != null){
            Toast.makeText(applicationContext, errorResponseString, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onResponse(JSONObject response) {}


    public JsonObjectRequest getRequestObject(String url, HashMap<String, String> dataMap){

        //Create a new json object and jsonRequest object
        JsonObjectRequest jsonRequest;
        JSONObject jsonObj = new JSONObject();

        try {
            //Iterate through HashMap to add all values to our json object
            for(Map.Entry<String,String> entry : dataMap.entrySet()){
                jsonObj.put(entry.getKey(), entry.getValue());
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        //If we've supplied a non-null url, create the jsonRequest object
        if(url != null) {
            jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObj, this, this);
        }
        //Otherwise, return an empty jsonRequest object
        else{
            jsonRequest = new JsonObjectRequest(Request.Method.POST, "", jsonObj, this, this);
        }

        return jsonRequest;
    }

}
