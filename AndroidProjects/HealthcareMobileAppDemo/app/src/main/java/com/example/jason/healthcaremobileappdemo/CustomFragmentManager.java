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

/*
    This class is merely the controller for managing the application fragments.
    The view is simply a titlebar centered at the top of the screen. This class utilizes
    the lower class, MyAdapter, to create a fragment view.

 */
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

    public void setCurrentItem(int item, boolean smoothScroll) {
        viewPager.setCurrentItem(item, smoothScroll);
    }

}




/*
    This class is responsible for creating and handling the
    tabs (or "Fragments").
    This class works by creating tabs in positions e.g. 0,1,2 respectively.
    The getCount function is called each frame to retain the correct count of tabs.
    The getPageTitle function returns the title of each tab
 */
class MyAdapter extends FragmentPagerAdapter {

    //Default constructor
    public MyAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }


    //Organizes classes in fragment positions
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if(position == 0)
        {
            fragment = new HomePage();
        }
        if(position == 1)
        {
            fragment = new ExerciseList();
        }
        return fragment;
    }


    //Get the count of fragments via pointer. This must always be correct,
    //or a null reference error will occur.
    @Override
    public int getCount() {
        return 2;
    }


    //returns a title of each fragment
    @Override
    public CharSequence getPageTitle(int position){
        String title = new String();
        if(position == 0)
        {
            return "Home";
        }
        if(position == 1)
        {
            return "My Plan";
        }
        return null;
    }





}