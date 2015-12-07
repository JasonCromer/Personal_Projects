package com.example.jason.liftingspiritanimal;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener, View.OnClickListener {

    private NumberPicker numberPicker;
    private TextView numTextView;
    private ImageButton doneButton;

    //Constants
    private static final int NUM_PICK_MIN_VALUE = 0;
    private static final int NUM_PICK_MAX_VALUE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set references to our view items
        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numTextView = (TextView) findViewById(R.id.enterNumTextView);
        doneButton = (ImageButton) findViewById(R.id.doneButton);

        //Set on-click listeners for our view items
        numberPicker.setOnValueChangedListener(this);
        doneButton.setOnClickListener(this);

        //Additional settings
        setNumberPickerSettings();
        setTextViewFont();
    }


    private void setTextViewFont(){
        numTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf"));
    }


    private void setNumberPickerSettings(){
        numberPicker.setMinValue(NUM_PICK_MIN_VALUE);
        numberPicker.setMaxValue(NUM_PICK_MAX_VALUE);
        numberPicker.setWrapSelectorWheel(true);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }

    @Override
    public void onClick(View v) {
        if(v == doneButton){
            Intent resultsIntent = new Intent(this, DisplayResultsActivity.class);
            startActivity(resultsIntent);
        }
    }
}
