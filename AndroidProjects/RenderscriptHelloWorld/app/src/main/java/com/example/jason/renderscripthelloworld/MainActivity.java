package com.example.jason.renderscripthelloworld;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RenderScriptAsyncHelper.Listener,
        GrayScottDiffusionReaction.Listener {

    private static final String FRAG_TAG_FILTER_FRAGMENT = "filter_fragment";
    private static final String FRAG_TAG_PICTURE_FRAGMENT = "picture_fragment";
    private static final String FRAG_TAG_RIPPLE_FRAGMENT = "ripple_fragment";

    private PictureFragment mPictureFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        showFilterFragment();
        showPictureFragment();
//        showRippleFragment();
    }

    private void showRippleFragment() {
        RippleFragment rippleFragment = (RippleFragment) getSupportFragmentManager()
                .findFragmentByTag(FRAG_TAG_RIPPLE_FRAGMENT);

        if (rippleFragment == null) {
            rippleFragment = RippleFragment.newInstance();
        }

        Utils.replaceFragment(getSupportFragmentManager(), R.id.activity_main, rippleFragment,
                FRAG_TAG_RIPPLE_FRAGMENT, false /* addToBackStack */);
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
    public void onDiffusionImageLoaded(Bitmap image) {
        if (image != null && mPictureFragment.isAdded()) {
            runOnUiThread(() -> mPictureFragment.setImageView(image));
        }
    }

    @Override
    public void onImageResult(Bitmap result) {
        if (result != null && mPictureFragment.isAdded()) {
            mPictureFragment.setImageView(result);
        }
    }
}
