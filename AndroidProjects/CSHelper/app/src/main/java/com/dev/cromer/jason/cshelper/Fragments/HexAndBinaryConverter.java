package com.dev.cromer.jason.cshelper.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.cromer.jason.cshelper.R;


public class HexAndBinaryConverter extends Fragment {

    private View hexAndBinaryConverterView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        hexAndBinaryConverterView = inflater.inflate(R.layout.fragment_hex_and_binary_converter, container, false);

        return hexAndBinaryConverterView;
    }


}
