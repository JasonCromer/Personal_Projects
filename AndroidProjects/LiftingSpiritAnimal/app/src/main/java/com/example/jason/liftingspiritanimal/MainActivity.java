package com.example.jason.liftingspiritanimal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.NumberPicker;

public class MainActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    private NumberPicker numberPicker;

    //Constants
    private static final int NUM_PICK_MIN_VALUE = 0;
    private static final int NUM_PICK_MAX_VALUE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set reference to the numberPicker
        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numberPicker.setOnValueChangedListener(this);
        setNumberPickerSettings();
    }


    private void setNumberPickerSettings(){
        numberPicker.setMinValue(NUM_PICK_MIN_VALUE);
        numberPicker.setMaxValue(NUM_PICK_MAX_VALUE);
        numberPicker.setWrapSelectorWheel(true);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }
}
