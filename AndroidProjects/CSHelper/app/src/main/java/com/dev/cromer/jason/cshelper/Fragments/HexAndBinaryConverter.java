package com.dev.cromer.jason.cshelper.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.cromer.jason.cshelper.R;



public class HexAndBinaryConverter extends Fragment implements View.OnClickListener {

    private View hexAndBinaryConverterView;
    private EditText userInputField;
    private TextView conversionOutputField;
    private Button convertButton;
    private Spinner mySpinner;

    private ArrayAdapter<String> spinnerArrayAdapter;
    private String[] spinnerItems = {"Binary", "Hex", "Octal", "Decimal", "Unsigned", "One's Compl.", "Two's Compl."};
    private String outputString;
    private String EMPTY_INPUT_ERROR = "Cannot convert an empty input.";
    private String ALPHA_INPUT_ERROR = "Numbers only please!";
    private String INT_TOO_LONG_ERROR = "Your number is too large";
    private String NOT_VALID_SIGNED_ERROR = "This number doesn't fit in an 8-bit signed int";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        hexAndBinaryConverterView = inflater.inflate(R.layout.fragment_hex_and_binary_converter, container, false);

        //Create spinner and set its adapter and on item selected listener
        mySpinner = (Spinner) hexAndBinaryConverterView.findViewById(R.id.spinner);
        spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        mySpinner.setAdapter(spinnerArrayAdapter);

        //Instantiate layout items and set their respective OnClickListeners
        userInputField = (EditText) hexAndBinaryConverterView.findViewById(R.id.userInputFieldEditText);
        conversionOutputField = (TextView) hexAndBinaryConverterView.findViewById(R.id.conversionOutputTextView);
        convertButton = (Button) hexAndBinaryConverterView.findViewById(R.id.convertButton);

        convertButton.setOnClickListener(this);

        return hexAndBinaryConverterView;
    }


    @Override
    public void onClick(View v) {

        //Check if there are no numbers
        if (!userInputField.getText().toString().matches("-?[0-9]+")) {
            conversionOutputField.setText("-");
            Toast.makeText(getActivity(), EMPTY_INPUT_ERROR, Toast.LENGTH_SHORT).show();
        }

        //check for appropriate integer parsing length (greater than 9)
        else if (userInputField.getText().length() > 9) {
            Toast.makeText(getActivity(), INT_TOO_LONG_ERROR, Toast.LENGTH_SHORT).show();
        }

        //Make sure input is numeric only and less than length 10
        else if (userInputField.getText().toString().matches("-?[0-9]+") && userInputField.getText().length() < 10) {

            //parse integer input
            final int userInputInteger = Integer.parseInt(userInputField.getText().toString());
            //Get spinner item position
            final int spinnerItemPosition = mySpinner.getSelectedItemPosition();

            if(v == convertButton) {
                //binary
                if(spinnerItemPosition == 0) {
                    conversionOutputField.setText("");
                    outputString = Integer.toBinaryString(userInputInteger);
                    conversionOutputField.setText(outputString);
                }
                //hexadecimal
                if(spinnerItemPosition == 1) {
                    conversionOutputField.setText("");
                    outputString = Integer.toHexString(userInputInteger);
                    conversionOutputField.setText(outputString);
                }
                //octal
                if(spinnerItemPosition == 2) {
                    conversionOutputField.setText("");
                    outputString = Integer.toOctalString(userInputInteger);
                    conversionOutputField.setText(outputString);
                }
                //decimal
                if(spinnerItemPosition == 3) {
                    conversionOutputField.setText("");
                    if(String.valueOf(userInputInteger).length() < 2) {
                        outputString = String.valueOf(userInputInteger) + ".0";
                        conversionOutputField.setText(outputString);
                    }
                    else {
                        final char firstNumber = String.valueOf(Math.abs((long) userInputInteger)).charAt(0);
                        final String restOfNumber = String.valueOf(userInputInteger).substring(1);
                        final int numberToRaiseBy = String.valueOf(userInputInteger).length() - 1;
                        outputString = String.valueOf(firstNumber) + "." + restOfNumber + " x 10^" + String.valueOf(numberToRaiseBy);
                        conversionOutputField.setText(outputString);
                    }
                }
                //Unsigned
                if(spinnerItemPosition == 4) {
                    conversionOutputField.setText("");
                    final byte inputAsByte = (byte) (userInputInteger);
                    //If value is above 127 or below -128, it is not within the byte range of a signed int
                    if((userInputInteger > Byte.MAX_VALUE) || (userInputInteger< Byte.MIN_VALUE)) {
                        Toast.makeText(getActivity(), NOT_VALID_SIGNED_ERROR, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity(), String.valueOf(Byte.MAX_VALUE), Toast.LENGTH_SHORT).show();
                        long result = inputAsByte & 0xFF;
                        outputString = String.valueOf(result);
                        conversionOutputField.setText(outputString);
                    }
                }
                //One's Compliment
                if(spinnerItemPosition == 5) {
                    conversionOutputField.setText("");
                    //outputString = String.valueOf(~userInputInteger);   for simple answer like -16
                    final int inputInBinary = Integer.parseInt(Integer.toBinaryString(userInputInteger), 2);
                    final long result = (~inputInBinary);
                    final String resultString = Long.toBinaryString(result);
                    outputString = resultString.substring(Math.max(resultString.length() - 16, 0));
                    conversionOutputField.setText(outputString);
                }
                //Two's Compliment
                if(spinnerItemPosition == 6) {
                    conversionOutputField.setText("");
                    final int inputInBinary = Integer.parseInt(Integer.toBinaryString(userInputInteger), 2);
                    final long result = (~inputInBinary) + 1;
                    final String resultString = Long.toBinaryString(result);
                    outputString = resultString.substring(Math.max(resultString.length() - 16, 0));
                    conversionOutputField.setText(outputString);
                }
            }
        }

        //Catch any whitespace or possible letters and print this error
        else {
            Toast.makeText(getActivity(), ALPHA_INPUT_ERROR, Toast.LENGTH_SHORT).show();
        }
    }
}
