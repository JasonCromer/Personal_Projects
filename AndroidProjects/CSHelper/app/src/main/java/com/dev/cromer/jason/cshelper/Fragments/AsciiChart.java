package com.dev.cromer.jason.cshelper.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.cromer.jason.cshelper.R;


public class AsciiChart extends Fragment {

    private View asciiView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        asciiView = inflater.inflate(R.layout.fragment_ascii, container, false);

        return asciiView;

    }


}
