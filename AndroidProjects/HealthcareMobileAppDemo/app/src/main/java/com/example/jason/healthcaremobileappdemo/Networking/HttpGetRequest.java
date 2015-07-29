package com.example.jason.healthcaremobileappdemo.Networking;


import android.media.audiofx.BassBoost;
import android.os.AsyncTask;

import com.example.jason.healthcaremobileappdemo.Activities.FragmentManagement.CustomFragmentManager;
import com.example.jason.healthcaremobileappdemo.Activities.SettingsActivity;
import com.example.jason.healthcaremobileappdemo.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

/**
 * Created by jason on 7/29/15.
 */
public class HttpGetRequest extends AsyncTask<String, Void, String>{

    @Override
    protected String doInBackground(String... url) {
        String stringURL = url[0];
        String result = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder = null;

        try {
            HttpURLConnection connection = (HttpURLConnection) ((new URL(stringURL).openConnection()));

            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
            connection.connect();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            stringBuilder = new StringBuilder();

            String inputLine = null;
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine + "\n");
            }

        }
        catch(IOException e){
            e.printStackTrace();
        }

        finally{
            //close reader and catch its exception
            if(reader != null){
                try{
                    reader.close();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        }

        result = stringBuilder.toString();

        return result;
    }

    @Override
    public void onPostExecute(String result) {
    }

}
