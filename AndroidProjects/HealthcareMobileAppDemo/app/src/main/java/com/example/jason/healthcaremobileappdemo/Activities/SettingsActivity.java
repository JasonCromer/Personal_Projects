package com.example.jason.healthcaremobileappdemo.Activities;


import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.Toast;
import com.example.jason.healthcaremobileappdemo.Networking.HttpGetRequest;

import com.example.jason.healthcaremobileappdemo.R;

import java.util.concurrent.ExecutionException;

public class SettingsActivity extends Fragment implements View.OnClickListener{

    private Button getUsernameButton;
    private View fragmentView;
    private String url = "http://10.0.2.2:5000/api/users/1";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.activity_settings, container, false);

        getUsernameButton = (Button) fragmentView.findViewById(R.id.getUsernameButton);
        getUsernameButton.setOnClickListener(this);

        return fragmentView;
    }

    @Override
    public void onClick(View v) {
        if(v == getUsernameButton){
            try {
                String receivedData = new HttpGetRequest().execute(url).get();
                Toast.makeText(getActivity(), receivedData, Toast.LENGTH_SHORT).show();
            }
            catch(ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
