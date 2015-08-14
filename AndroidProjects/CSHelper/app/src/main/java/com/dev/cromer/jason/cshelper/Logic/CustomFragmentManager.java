package com.dev.cromer.jason.cshelper.Logic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.inputmethod.InputMethodManager;

import com.dev.cromer.jason.cshelper.Fragments.DataStructures;
import com.dev.cromer.jason.cshelper.Fragments.HexAndBinaryConverter;
import com.dev.cromer.jason.cshelper.Fragments.HomeScreen;
import com.dev.cromer.jason.cshelper.R;

public class CustomFragmentManager extends FragmentActivity {

    private ViewPager viewPager = null;
    public SharedPreferences preferenceManager;
    public boolean welcomeScreenShown;
    private String welcomeScreenShownPref = "welcomeScreenShown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_manager);

        //Create welcome screen if first time user
        preferenceManager = PreferenceManager.getDefaultSharedPreferences(this);
        welcomeScreenShown = preferenceManager.getBoolean(welcomeScreenShownPref, false);
        if(!welcomeScreenShown){
            showWelcomeScreen();
        }


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

    public void showWelcomeScreen() {
        final String whatsNewTitle = "I see this is your first time!";
        final String whatsNewText = "Just swipe the screen left and right to switch between tabs.";
        new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Dialog))
                .setIcon(android.R.drawable.ic_dialog_alert).setTitle(whatsNewTitle)
                .setMessage(whatsNewText).setPositiveButton("Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setInverseBackgroundForced(true).show();
        SharedPreferences.Editor editor = preferenceManager.edit();
        editor.putBoolean(welcomeScreenShownPref, true);
        editor.apply();
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
            fragment = new HomeScreen();
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

        if(position == 0) {
            return "Home";
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
