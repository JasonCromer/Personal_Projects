package com.example.jason.renderscripthelloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static final String FRAG_TAG_FILTER_FRAGMENT = "filter_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showFilterFragment();
    }

    private void showFilterFragment() {
        FilterFragment filterFragment = (FilterFragment) getSupportFragmentManager()
                .findFragmentByTag(FRAG_TAG_FILTER_FRAGMENT);

        if (filterFragment == null) {
            filterFragment = FilterFragment.newInstance();
        }

        Utils.replaceFragment(getSupportFragmentManager(), R.id.activity_main, filterFragment,
                FRAG_TAG_FILTER_FRAGMENT, false /* addToBackStack */);
    }
}
