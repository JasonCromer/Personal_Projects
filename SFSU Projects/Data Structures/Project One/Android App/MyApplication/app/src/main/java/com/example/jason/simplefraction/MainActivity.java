package com.example.jason.simplefraction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

            //Create String objects for both inputs
            final String inputOneString = inputOne.getText().toString();
            final String inputTwoString = inputTwo.getText().toString();


            //If first input is valid, reveal second edit text
            if(v == inputOne && isNumeratorValid(inputOneString)){
                unveilInputTwo();
            }
            else if(v == inputTwo && isDenominatorValid(inputTwoString) && secondFraction) {
                //If we are inputting a second fraction, unveil the "GO" button once done
                unveilButton();
                setSecondFraction();
            }
            else if(v == inputTwo && isDenominatorValid(inputTwoString) && !secondFraction){
                //Otherwise unveil spinner and save our first fraction
                unveilSpinner();
                setFirstFraction();
            }
            else{
                Toast.makeText(this, "Invalid input. Try again", Toast.LENGTH_SHORT).show();
            }
        }

        return false;
    }


    @Override
    public void onClick(View v) {
        //Reset textview
        fractionTextView.setText("");

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

            //CompareTo
            case 5:
                final int intResult;
                intResult = operandOne.compareTo(operandTwo);
                fractionTextView.setText(String.valueOf(intResult));
                return;

            //Equals
            case 6:
                final boolean isEqual;
                isEqual = operandOne.equals(operandTwo);
                fractionTextView.setText(String.valueOf(isEqual));
                return;

            //Reciprocal
            case 7:
                result = operandOne.getReciprocal();
                fractionTextView.setText(result.toString());
                return;

            //ToDouble
            case 8:
                final double doubleResult;
                doubleResult = operandOne.toDouble();
                fractionTextView.setText(String.valueOf(doubleResult));
                return;

            //Set Fraction
            case 9:
                final int num = Integer.parseInt(inputOne.getText().toString());
                final int den = Integer.parseInt(inputTwo.getText().toString());
                operandOne.setSimpleFraction(num,den);
                fractionTextView.setText(operandOne.toString());
                return;

            default:
                final String errorMsg = "Something went wrong. Try again.";
                fractionTextView.setText(errorMsg);
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
                resetUI();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /*
        This method is called when the refresh button is pressed on the Action Menu.
        When pressed, all UI components are reset to how they were set at the creation
        of the Activity.
     */
    private void resetUI(){
        //Second Fraction boolean to false
        secondFraction = false;

        //Clear edit texts and textview
        inputOne.setText("");
        inputTwo.setText("");
        fractionTextView.setText("");

        //set spinner position to default, which is 0
        spinner.setSelection(0);

        //hide all UI components except inputOne
        doneButton.setVisibility(View.INVISIBLE);
        inputTwo.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.INVISIBLE);

        //Set focus to inputOne
        inputOne.requestFocus();
    }


    /*
        This method determines the next UI action after a spinner
        option is selected. If greater or equal to 7, the
        items don't require a second fraction, and thus the "GO"
        button is unveiled via the unveilButton() method.
     */
    private void executeSpinnerSelection(int position){
        //Add,Sub,Mult,Divide,Compare,Equals
        if(position > 0 && position < 7){
            setSecondFractionUIComponents();
        }
        //Reciprocal, toDouble, setFraction
        else if(position >= 7){
            spinner.setVisibility(View.INVISIBLE);
            unveilButton();
        }
    }


    /*
        This method creates an instance of SimpleFraction, called
        operandOne, and assigns it the values given by the user
        via inputOne and InputTwo.
     */
    private void setFirstFraction(){
        int intNum = Integer.parseInt(inputOne.getText().toString());
        int intDen = Integer.parseInt(inputTwo.getText().toString());
        operandOne = new SimpleFraction(intNum, intDen);
    }


    /*
        This method creates an instance of SimpleFraction, called
        operandTwo, and assigns it the values given by the user
        via inputOne and inputTwo.
     */
    private void setSecondFraction(){
        int intNum = Integer.parseInt(inputOne.getText().toString());
        int intDen = Integer.parseInt(inputTwo.getText().toString());
        operandTwo = new SimpleFraction(intNum, intDen);
    }


    /*
        This method clears inputOne and inputTwo, and hides inputTwo and the spinner
        so that the user can input a second fraction.
     */
    private void setSecondFractionUIComponents(){
        //onEditor checks this to indicate whether or not button should be unveiled
        secondFraction = true;

        //Clear edit texts
        inputOne.setText("");
        inputTwo.setText("");

        //Hide UI components, not inputOne
        inputTwo.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.INVISIBLE);

        //Request focus from inputOne
        inputOne.requestFocus();
    }


    /*
        This method creates a list of items for the spinner, and sets it via an ArrayAdapter.
     */
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


    //This method checks if the user input is a valid numerator
    private boolean isNumeratorValid(String num){
        return (num.length() > 0);
    }


    //This method checks if the user input is a valid denominator
    private boolean isDenominatorValid(String den){
        final String zero = "0";
        return (den.length() > 0 && !den.equals(zero));
    }


    /*
        This method unveils the "GO" button by making it visible. It also creates and applies
        the animation for the button.
     */
    private void unveilButton(){
        //Create slide-in animation for button
        Animation buttonAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        buttonAnimation.setDuration(ANIM_DURATION_MILLI);

        //Apply animation to button
        doneButton.setAnimation(buttonAnimation);

        //Make button visible and animate it
        doneButton.setVisibility(View.VISIBLE);
        doneButton.animate();
    }


    /*
        This method unveils the second input for the user to enter a fraction. This method
        also creates and applies an animation to the Edit Text, called inputTwo. It then
        requests focus so that the user can immediately start typing a fraction.
     */
    private void unveilInputTwo(){
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


    /*
        This method unveils the spinner by making it visible. It also creates and applies
        the animation for it when called. Focus for the spinner is also requested.
     */
    private void unveilSpinner(){
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
