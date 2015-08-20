package com.dev.cromer.jason.coverme.Networking;


import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGetRequest extends AsyncTask<String, Void, String> {



    @Override
    protected String doInBackground(String... url) {
        String stringURL = url[0];
        String result = null;
        BufferedReader reader;
        StringBuilder stringBuilder;


        try {
            HttpURLConnection connection = (HttpURLConnection) ((new URL(stringURL).openConnection()));

            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);

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
