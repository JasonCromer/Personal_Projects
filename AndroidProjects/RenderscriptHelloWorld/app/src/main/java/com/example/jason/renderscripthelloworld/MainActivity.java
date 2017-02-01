package com.example.jason.renderscripthelloworld;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements RenderScriptAsyncHelper.Listener {

    private static final String FRAG_TAG_FILTER_FRAGMENT = "filter_fragment";
    private static final String FRAG_TAG_PICTURE_FRAGMENT = "picture_fragment";

    private PictureFragment mPictureFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        showFilterFragment();
        showPictureFragment();
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

    private void showPictureFragment() {
        mPictureFragment = (PictureFragment) getSupportFragmentManager()
                .findFragmentByTag(FRAG_TAG_PICTURE_FRAGMENT);

        if (mPictureFragment == null) {
            mPictureFragment = PictureFragment.newInstance();
        }

        Utils.replaceFragment(getSupportFragmentManager(), R.id.activity_main, mPictureFragment,
                FRAG_TAG_PICTURE_FRAGMENT, false /* addToBackStack */);
    }

    @Override
    public void onImageResult(Bitmap result) {
        if (result != null && mPictureFragment.isAdded()) {
            mPictureFragment.setImageView(result);
        }
    }
}
