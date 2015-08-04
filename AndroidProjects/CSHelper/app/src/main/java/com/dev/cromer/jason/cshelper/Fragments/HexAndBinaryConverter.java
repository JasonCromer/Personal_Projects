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
    private String outputString;

    private String EMPTY_INPUT_ERROR = "Cannot convert an empty input.";
    private String ALPHA_INPUT_ERROR = "Numbers only please!";
    private String INT_TOO_LONG_ERROR = "Your number is too large";


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
        //Check if there are no numbers
        if(!userInputField.getText().toString().matches("[0-9]+")) {
            Toast.makeText(getActivity(), EMPTY_INPUT_ERROR, Toast.LENGTH_SHORT).show();
        }
        //check for appropriate integer parsing length (greater than 9)
        else if(userInputField.getText().length() > 9){
            Toast.makeText(getActivity(), INT_TOO_LONG_ERROR, Toast.LENGTH_SHORT).show();
        }
        //Make sure input is numeric only and less than length 10
        else if(userInputField.getText().toString().matches("[0-9]+")  && userInputField.getText().length() < 10) {

            //parse integer input
            final int userInputInteger = Integer.parseInt(userInputField.getText().toString());

            if(v == binaryConvertButton) {
                conversionOutputField.setText("");
                outputString = Integer.toBinaryString(userInputInteger);
                conversionOutputField.setText(outputString);
            }
            if(v == hexConvertButton) {
                conversionOutputField.setText("");
                outputString = Integer.toHexString(userInputInteger);
                conversionOutputField.setText(outputString);
            }
        }

        //Catch any whitespace or possible letters and print this error
        else {
            Toast.makeText(getActivity(), ALPHA_INPUT_ERROR, Toast.LENGTH_SHORT).show();
        }
    }
}
