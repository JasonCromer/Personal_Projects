package com.example.jason.healthcaremobileappdemo.Networking;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpRequest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url = "http://10.0.2.2:5000/api/users";
        new httpPOST().execute(url);
    }


    /* Create a sub class that executes http request
        This class will make a connection to POST to the requested URL
        and return a result (String) containing the data that the API
        returns from the http request
     */

    private class httpPOST extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String httpURL = url[0];
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
                json.put("username", "Jason");
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
            catch(IOException e){
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            //print
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
        }
    }
}
