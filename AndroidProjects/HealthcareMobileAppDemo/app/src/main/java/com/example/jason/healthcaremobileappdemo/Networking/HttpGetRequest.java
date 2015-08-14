package com.example.jason.healthcaremobileappdemo.Networking;


import android.os.AsyncTask;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpGetRequest extends AsyncTask<String, Void, String>{

    String authUsernameAndPassword = "";


    @Override
    protected String doInBackground(String... url) {
        String stringURL = url[0];
        String result;
        BufferedReader reader = null;
        StringBuilder stringBuilder = null;

        String authUsernameAndPassword = getAuthUsernameAndPassword();

        try {
            HttpURLConnection connection = (HttpURLConnection) ((new URL(stringURL).openConnection()));

            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);

            //encode -u (basic authentication) parameter for the request.
            //Set NO_WRAP so that Base64 doesn't append a new line to the return (which is the default)
            String encoded = Base64.encodeToString((authUsernameAndPassword).getBytes("UTF-8"), Base64.NO_WRAP);
            connection.setRequestProperty("Authorization", "Basic " + encoded);

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


    public void setAuthUsernameAndPassword(String username, String password) {
        authUsernameAndPassword = username + ":" + password;
    }

    public String getAuthUsernameAndPassword() {
        return authUsernameAndPassword;
    }

}
