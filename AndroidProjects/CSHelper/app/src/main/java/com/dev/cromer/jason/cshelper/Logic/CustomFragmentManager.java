package com.dev.cromer.jason.cshelper.Logic;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.dev.cromer.jason.cshelper.Fragments.AsciiChart;
import com.dev.cromer.jason.cshelper.Fragments.DataStructures;
import com.dev.cromer.jason.cshelper.Fragments.HexAndBinaryConverter;
import com.dev.cromer.jason.cshelper.R;

public class CustomFragmentManager extends FragmentActivity {

    private ViewPager viewPager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_manager);

        //Get viewpager object from layout
        viewPager = (ViewPager) findViewById(R.id.fragmentViewPager);

        //create fragment manager that will handle tab functionality
        FragmentManager fragmentManager = getSupportFragmentManager();

        viewPager.setAdapter(new MyAdapter(fragmentManager));

        //Hide keyboard when tabs are changed
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                final InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}



class MyAdapter extends FragmentPagerAdapter {

    //default constructor
    public MyAdapter(FragmentManager fm) {
        super(fm);
    }


    //Organize classes into fragment positions
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        if(position == 0) {
            fragment = new AsciiChart();
        }

        if(position == 1) {
            fragment = new HexAndBinaryConverter();
        }

        if(position == 2) {
            fragment = new DataStructures();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = new String();

        if(position == 0) {
            return "Ascii Chart";
        }

        if(position == 1) {
            return "Converter";
        }

        if(position == 2) {
            return "Data Structures";
        }

        return null;
    }
}
