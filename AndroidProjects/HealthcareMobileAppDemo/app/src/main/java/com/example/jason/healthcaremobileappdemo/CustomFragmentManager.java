package com.example.jason.healthcaremobileappdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentActivity;
        import android.support.v4.app.FragmentPagerAdapter;
        import android.support.v4.view.ViewPager;
        import android.os.Bundle;
        import android.support.v4.app.FragmentManager;


public class CustomFragmentManager extends FragmentActivity {

    private ViewPager viewPager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_manager);

        //Get viewpager object
        viewPager = (ViewPager) findViewById(R.id.pager);

        //Create fragment manager that will handle tab functionality
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        viewPager.setAdapter(new MyAdapter(fragmentManager));
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