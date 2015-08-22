package com.dev.cromer.jason.coverme.Networking;


import android.os.AsyncTask;

import com.dev.cromer.jason.coverme.Logic.PostRequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPostRequest extends AsyncTask<PostRequestParams, String, String> {


    @Override
    protected String doInBackground(PostRequestParams... params) {
        String httpURL = params[0].getUrl();
        String latitude = params[0].getLatitude();
        String longitude = params[0].getLongitude();
        String markerTitle = params[0].getMarkerTitle();
        HttpURLConnection httpURLConnection;
        String data;
        String result = null;

        try {
            //Connect
            httpURLConnection = (HttpURLConnection) ((new URL(httpURL).openConnection()));
            httpURLConnection.setDoOutput(true);

            //set headers
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();

            //Create a new JSON object
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
            jsonObject.put("markerTitle", markerTitle);
            data = jsonObject.toString();

            //Write the data to an output stream
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bufferedWriter.write(data);
            bufferedWriter.close();
            outputStream.close();

            //Read incoming response from API
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();

            //Append all input lines to the String builder; break when input is null
            while((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            bufferedReader.close();
            result = stringBuilder.toString();
        }
        catch(IOException | JSONException e){
            e.printStackTrace();
        }

        return result;
    }

}
