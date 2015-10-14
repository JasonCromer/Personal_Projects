package com.dev.cromer.jason.takeastand.networking;

import android.os.AsyncTask;

import com.dev.cromer.jason.takeastand.objects.MarkerPostRequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 */
public class HttpMarkerPostRequest extends AsyncTask<MarkerPostRequestParams, Void, Integer> {

    protected HttpURLConnection httpURLConnection;
    protected URL urlContainer;
    protected JSONObject jsonObject;
    protected OutputStream outputStream;
    protected OutputStreamWriter outputStreamWriter;
    protected InputStream inputStream;
    protected InputStreamReader inputStreamReader;
    protected BufferedWriter bufferedWriter;
    protected BufferedReader bufferedReader;
    protected String inputLine;
    protected StringBuilder stringBuilder;
    protected String data;
    protected int result;

    //Constants
    private static final int POST_SUCCESS = 0;
    private static final int POST_FAILED = -1;
    private static final String REQUEST_METHOD = "POST";
    private static final String REQUEST_PROPERTY_JSON = "application/json";
    private static final String CHARACTER_ENCODING = "UTF-8";


    @Override
    protected Integer doInBackground(MarkerPostRequestParams... params) {

        //Get passed in arguments from the params object
        String httpUrl = params[0].getUrl();
        float latitude = params[0].getLatitude();
        float longitude = params[0].getLongitude();
        int religionID = params[0].getReligionID();

        try{
            urlContainer = new URL(httpUrl);

            //Form connection
            httpURLConnection = (HttpURLConnection) (urlContainer.openConnection());

            //Set headers
            httpURLConnection.setRequestProperty("Content-Type", REQUEST_PROPERTY_JSON);
            httpURLConnection.setRequestProperty("Accept", REQUEST_PROPERTY_JSON);
            httpURLConnection.setRequestMethod(REQUEST_METHOD);

            httpURLConnection.connect();

            //Create a new JSON object
            jsonObject = new JSONObject();
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
            jsonObject.put("religionID", religionID);
            data = jsonObject.toString();

            //Write the data to an output stream
            outputStream = httpURLConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream, CHARACTER_ENCODING);
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(data);

            //Close the output Stream and writer
            bufferedWriter.close();
            outputStreamWriter.close();
            outputStream.close();


            //Read incoming response from API
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, CHARACTER_ENCODING);
            bufferedReader = new BufferedReader(inputStreamReader);

            //Append all input lines to the StringBuilder. break when input is null
            stringBuilder = new StringBuilder();
            while((inputLine = bufferedReader.readLine()) != null){
                stringBuilder.append(inputLine);
            }

            //Close input stream and reader
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();


            result = POST_SUCCESS;
        }
        catch (IOException | JSONException e){
            e.printStackTrace();
            result = POST_FAILED;
        }

        return result;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
    }
}
