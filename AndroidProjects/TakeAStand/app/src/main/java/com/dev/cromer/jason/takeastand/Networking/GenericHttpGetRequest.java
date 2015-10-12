package com.dev.cromer.jason.takeastand.Networking;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 */
public class GenericHttpGetRequest extends AsyncTask<String, Void, String> {

    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;

    @Override
    protected String doInBackground(String... params) {
        String stringURL = params[0];
        String result;
        String inputLine;
        BufferedReader reader;
        StringBuilder stringBuilder;
        InputStreamReader streamReader;

        try{
            //Create a new URL connection
            HttpURLConnection connection = (HttpURLConnection) (new URL(stringURL).openConnection());

            //Set methods for connecting to API
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            connection.connect();

            //Create a new inputStream
            streamReader = new InputStreamReader(connection.getInputStream());
            reader = new BufferedReader(streamReader);
            stringBuilder = new StringBuilder();

            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }

            streamReader.close();
            reader.close();
            result = stringBuilder.toString();
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
