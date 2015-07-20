package com.example.jason.healthcaremobileappdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomePage extends Fragment implements View.OnClickListener {

    private View fragmentView;
    private Button startPlanButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.activity_home_page, container, false);

        startPlanButton = (Button) fragmentView.findViewById(R.id.startPlanButton);
        startPlanButton.setOnClickListener(this);


        return fragmentView;
    }

    @Override
    public void onClick(View v) {
        if(v == startPlanButton) {
            //Sets the fragment to position 1 with smooth scrolling enabled
            ((CustomFragmentManager)getActivity()).setCurrentItem(1, true);
        }

    }
}
