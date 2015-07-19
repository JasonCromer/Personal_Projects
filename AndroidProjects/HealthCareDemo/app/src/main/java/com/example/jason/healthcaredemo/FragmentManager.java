package com.example.jason.healthcaredemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

/**
 * Created by jason on 7/19/15.
 */
public class FragmentManager extends FragmentActivity {

    //Create a new ViewPager that will serve as our tabs
    protected ViewPager viewPager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        //Get the viewPager object
        //viewPager = (ViewPager) findViewById(R.id.pager);

        //Create a fragment manager that will handle all of the tab functionality
        //android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        //Pass our viewPager object into our MyAdapter class with our fragmentManager functionality
        //viewPager.setAdapter(new MyAdapter(fragmentManager));
    }
}







/*
    This class is responsible for creating and handling the
    tabs (or "Fragments") in the home page.
    This class works by creating 3 tabs in position 0,1,2 respectively.
    The getCount function is called each frame to retain the correct count of tabs.
    The getPageTitle function returns the title of each tab
 */
class MyAdapter extends FragmentPagerAdapter {

    public MyAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if(position == 0)
        {
            fragment = new ExerciseList();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position){
        String title = new String();
        if(position == 0)
        {
            return "My Plan";
        }
        return null;
    }
}