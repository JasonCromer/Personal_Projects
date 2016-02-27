package com.example.jason.simplefraction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        TextView.OnEditorActionListener, View.OnClickListener {

    private EditText inputOne;
    private EditText inputTwo;
    private Spinner spinner;
    private Button doneButton;
    private TextView fractionTextView;

    private SimpleFraction operandOne;
    private SimpleFraction operandTwo;
    private boolean secondFraction = false;

    //Constants
    private static final int ANIM_DURATION_MILLI = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create object references
        inputOne = (EditText) findViewById(R.id.inputOne);
        inputTwo = (EditText) findViewById(R.id.inputTwo);
        spinner = (Spinner) findViewById(R.id.spinner);
        doneButton = (Button) findViewById(R.id.doneButton);
        fractionTextView = (TextView) findViewById(R.id.fractionTextView);

        //Add listener to spinner and both textviews
        spinner.setOnItemSelectedListener(this);
        inputOne.setOnEditorActionListener(this);
        inputTwo.setOnEditorActionListener(this);
        doneButton.setOnClickListener(this);

        //Set String items in spinner
        setSpinnerOptions();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

        @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Execute a series of items on our spinner list depending on position
        executeSpinnerSelection(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE){

            //If first input is valid, reveal second edit text
            if(v == inputOne && isNumeratorValid(inputOne.getText().toString())){
                unvealInputTwo();
            }
            else if(v == inputTwo && isDenominatorValid(inputTwo.getText().toString())){
                //If we are inputting a second fraction, unveal the "GO" button once done
                if(secondFraction){
                    unvealButton();
                    setSecondFraction();
                }
                //Otherwise unveil spinner and save our first fraction
                else{
                    unvealSpinner();
                    setFirstFraction();
                }
            }
            else{
                Toast.makeText(this, "Please enter valid number", Toast.LENGTH_SHORT).show();
            }
        }

        return false;
    }


    @Override
    public void onClick(View v) {
        //Create new null Interface
        SimpleFractionInterface result;

        //Get current spinner position
        int position = spinner.getSelectedItemPosition();

        switch (position){

            //Add
            case 1:
                //Add our first fraction to the second and set equal to interface
                result = operandOne.add(operandTwo);

                //Display results
                fractionTextView.setText(result.toString());
                return;

            //Subtract
            case 2:
                result = operandOne.subtract(operandTwo);
                fractionTextView.setText(result.toString());
                return;

            //Multiply
            case 3:
                result = operandOne.multiply(operandTwo);
                fractionTextView.setText(result.toString());
                return;

            //Divide
            case 4:
                result = operandOne.multiply(operandTwo);
                fractionTextView.setText(result.toString());
                return;



        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){

            case R.id.action_refresh:
                //Delete all data, hide everything but first input, set boolean to false
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void executeSpinnerSelection(int position){
        if(position > 7){
            unvealButton();
        }
        else if(position > 0 && position < 7){
            resetDisplayForSecondFraction();
        }
    }


    private void setFirstFraction(){
        int intNum = Integer.parseInt(inputOne.getText().toString());
        int intDen = Integer.parseInt(inputTwo.getText().toString());
        operandOne = new SimpleFraction(intNum, intDen);
    }

    private void setSecondFraction(){
        int intNum = Integer.parseInt(inputOne.getText().toString());
        int intDen = Integer.parseInt(inputTwo.getText().toString());
        operandTwo = new SimpleFraction(intNum, intDen);
    }


    private void resetDisplayForSecondFraction(){
        secondFraction = true;
        inputOne.setText("");
        inputTwo.setText("");
        inputOne.requestFocus();
        inputTwo.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.INVISIBLE);
    }

    private void setSpinnerOptions(){
        //Create list of our tests
        final String[] items = {"Select Item","Add","Subtract","Multiply","Divide","Compare To",
            "Equals","Reciprocal","To Double", "Set Fraction"};

        //Create Array Adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,
                items);

        //Set adapter to spinner
        spinner.setAdapter(adapter);
    }


    private boolean isNumeratorValid(String num){
        return (num.length() > 0);
    }

    private boolean isDenominatorValid(String den){
        final String zero = "0";
        return (den.length() > 0 && !den.equals(zero));
    }

    private void unvealButton(){
        //Create slide-in animation for button
        Animation buttonAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        buttonAnimation.setDuration(ANIM_DURATION_MILLI);

        //Apply animation to button
        doneButton.setAnimation(buttonAnimation);

        //Make button visible and animate it
        doneButton.setVisibility(View.VISIBLE);
        doneButton.animate();
    }

    private void unvealInputTwo(){
        //Create fade-in animation for edit text
        Animation editTextAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        editTextAnimation.setDuration(ANIM_DURATION_MILLI);

        //Apply animation to EditText
        inputTwo.setAnimation(editTextAnimation);

        //Make visible, animate and change focus to new edit text
        inputTwo.setVisibility(View.VISIBLE);
        inputTwo.animate();
        inputTwo.requestFocus();
    }

    private void unvealSpinner(){
        //Create fade-in animation for edit text
        Animation spinnerAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        spinnerAnimation.setDuration(ANIM_DURATION_MILLI);

        //Apply animation to spinner
        spinner.setAnimation(spinnerAnimation);

        //Make visible, animate and request focus
        spinner.setVisibility(View.VISIBLE);
        spinner.animate();
        spinner.requestFocus();
    }

}
