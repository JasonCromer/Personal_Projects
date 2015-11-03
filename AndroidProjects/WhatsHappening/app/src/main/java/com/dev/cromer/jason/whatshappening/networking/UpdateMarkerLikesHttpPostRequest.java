package com.dev.cromer.jason.whatshappening.networking;


import android.os.AsyncTask;

import com.dev.cromer.jason.whatshappening.objects.MarkerLikesPostRequestParams;

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

public class UpdateMarkerLikesHttpPostRequest extends AsyncTask<MarkerLikesPostRequestParams, String, String> {

    private static final String REQUEST_METHOD = "POST";
    private static final String REQUEST_PROPERTY_JSON = "application/json";
    private static final String CHARACTER_ENCODING = "UTF-8";

    @Override
    protected String doInBackground(MarkerLikesPostRequestParams... params) {
        String httpURL = params[0].getUrl();
        String voteType = params[0].getVoteType();
        HttpURLConnection httpURLConnection;
        String data;
        String result = null;

        try {
            //Connect
            httpURLConnection = (HttpURLConnection) ((new URL(httpURL).openConnection()));
            httpURLConnection.setDoOutput(true);

            //set headers
            httpURLConnection.setRequestProperty("Content-Type", REQUEST_PROPERTY_JSON);
            httpURLConnection.setRequestProperty("Accept", REQUEST_PROPERTY_JSON);

            httpURLConnection.setRequestMethod(REQUEST_METHOD);
            httpURLConnection.connect();

            //Create a new JSON object
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("voteType", voteType);
            data = jsonObject.toString();

            //Write the data to an output stream
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, CHARACTER_ENCODING));
            bufferedWriter.write(data);
            bufferedWriter.close();
            outputStream.close();

            //Read incoming response from API
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), CHARACTER_ENCODING));
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
