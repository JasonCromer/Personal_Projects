package com.example.jason.renderscripthelloworld;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

public class RippleFragment extends Fragment implements View.OnClickListener {

    private ImageView mImage;
    private SeekBar mAmplitude;
    private SeekBar mDampening;
    private SeekBar mFrequency;

    public RippleFragment() {
        // Required empty constructor
    }

    public static RippleFragment newInstance() {
        return new RippleFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ripple, container, false);

        mImage = (ImageView) view.findViewById(R.id.image);
        mAmplitude = (SeekBar) view.findViewById(R.id.control_amplitude);
        mDampening = (SeekBar) view.findViewById(R.id.control_dampening);
        mFrequency = (SeekBar) view.findViewById(R.id.control_frequency);
        view.findViewById(R.id.button_enhance).setOnClickListener(this);
        mImage.setImageResource(R.drawable.color_background);

        /*
         * Settings Ranges:
         * A = 0.01 - 1.0
         * D = 0.0001 - 0.01
         * F = 0.01 - 0.5
         */

        mAmplitude.setProgress(40);
        mDampening.setProgress(20);
        mDampening.setProgress(mDampening.getMax());

        mFrequency.setProgress(10);
        mFrequency.setMax(50);

        return view;
    }

    @Override
    public void onClick(View view) {
        mDampening.setProgress(mDampening.getMax());
        Handler handler = new Handler();
        int delay = 500;

        handler.postDelayed(new Runnable() {
            int times = 0;
            @Override
            public void run() {
                drawRipplesOnBackground(mImage, R.drawable.color_background);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mDampening.setProgress(mDampening.getProgress() - 10, false);
                } else {
                    mDampening.setProgress(mDampening.getProgress() - 10);
                }

                if (times < 20 && mDampening.getProgress() > 0) {
                    handler.postDelayed(this, delay);
                }
                times++;
            }
        }, delay);
    }

    private void drawRipplesOnBackground(ImageView imageView, int resId) {
        Bitmap in = BitmapFactory.decodeResource(getResources(), resId);
        Bitmap out = Bitmap.createBitmap(in.getWidth(), in.getHeight(), in.getConfig());

        // Create Renderscript context
        RenderScript rs = RenderScript.create(getContext());

        // Create Allocations from bitmaps
        Allocation allocationIn = Allocation.createFromBitmap(rs, in, Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        Allocation allocationOut = Allocation.createTyped(rs, allocationIn.getType());

        // Create our Ripple script instance
//        ScriptC_imageRipple imageRippleScript = new ScriptC_imageRipple(rs, getResources(), R.raw.wavy);
        ScriptC_newImageRipple imageRippleScript = new ScriptC_newImageRipple(rs, getResources(), R.raw.ripple);

        imageRippleScript.set_centerX(in.getWidth() / 2);
        imageRippleScript.set_centerY(in.getHeight() /2);
        imageRippleScript.set_minRadius(0f);

        float amplitude = Math.max(0.01f, mAmplitude.getProgress() / 100f);
        imageRippleScript.set_scalar(amplitude);
        float dampening = Math.max(0.0001f, mDampening.getProgress() / 10000f);
        imageRippleScript.set_damper(dampening);
        float frequency = Math.max(0.01f, mFrequency.getProgress() / 100f);
        imageRippleScript.set_frequency(frequency);

        //Run the script
        imageRippleScript.forEach_root(allocationIn, allocationOut);

        allocationOut.copyTo(out);
        imageView.setImageBitmap(out);
        //Tear down the RenderScript context
        rs.destroy();
    }
}
