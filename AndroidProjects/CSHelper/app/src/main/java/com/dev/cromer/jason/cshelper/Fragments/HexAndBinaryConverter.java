package com.dev.cromer.jason.cshelper.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.cromer.jason.cshelper.R;


public class HexAndBinaryConverter extends Fragment implements View.OnClickListener {

    private View hexAndBinaryConverterView;
    private EditText userInputField;
    private TextView conversionOutputField;
    private Button binaryConvertButton;
    private Button hexConvertButton;

    private String EMPTY_INPUT_ERROR = "Cannot convert an empty input.";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        hexAndBinaryConverterView = inflater.inflate(R.layout.fragment_hex_and_binary_converter, container, false);

        userInputField = (EditText) hexAndBinaryConverterView.findViewById(R.id.userInputFieldEditText);
        conversionOutputField = (TextView) hexAndBinaryConverterView.findViewById(R.id.conversionOutputTextView);
        binaryConvertButton = (Button) hexAndBinaryConverterView.findViewById(R.id.binaryConvertButton);
        hexConvertButton = (Button) hexAndBinaryConverterView.findViewById(R.id.hexConvertButton);

        binaryConvertButton.setOnClickListener(this);
        hexConvertButton.setOnClickListener(this);


        return hexAndBinaryConverterView;
    }


    @Override
    public void onClick(View v) {
        if(userInputField.getText().toString() == "" || userInputField == null) {
            Toast.makeText(getActivity(), EMPTY_INPUT_ERROR, Toast.LENGTH_SHORT).show();
        }
        else if(userInputField.getText().toString().matches("[0-9]+")) {
            Toast.makeText(getActivity(), "Nice!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }
}
