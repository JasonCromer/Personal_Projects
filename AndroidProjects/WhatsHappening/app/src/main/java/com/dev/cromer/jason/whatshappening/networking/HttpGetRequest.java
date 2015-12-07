package com.dev.cromer.jason.whatshappening.networking;


import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGetRequest extends AsyncTask<String, Void, String> {

    private static final String REQUEST_METHOD = "GET";
    private static final int READ_TIMEOUT = 15000;

    @Override
    protected String doInBackground(String... url) {
        String stringURL = url[0];
        String result = null;
        BufferedReader reader;
        StringBuilder stringBuilder;


        try {
            HttpURLConnection connection = (HttpURLConnection) ((new URL(stringURL).openConnection()));

            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);

            connection.connect();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            stringBuilder = new StringBuilder();

            String inputLine;
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }

            reader.close();
            result = stringBuilder.toString();

        }
        catch(IOException e){
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void onPostExecute(String result) {

    }

}
