package com.example.jason.renderscripthelloworld;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class RippleFragment extends Fragment implements View.OnTouchListener {

    private static final float FREQUENCY_COEFFICIENT = .035f;
    private static final float DAMPING_COEFFICIENT = .01f;
    private static final float AMPLITUDE_COEFFICIENT = .025f;

    private ImageView mImage;
    private RenderScript mRenderscript;
    private Allocation mAllocationIn;
    private Allocation mAllocationOut;
    ScriptC_imageRipple mImageRippleScript;
    private AsyncTask<Void, Void, Void> mRippleTask;
    private AsyncTask<Void, Void, Void> mFadeTask;
    private float mCurrentDampening;
    private Bitmap mResultBitmap;

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

        mRenderscript = RenderScript.create(getContext());
        mImage = (ImageView) view.findViewById(R.id.image);
        mImage.setOnTouchListener(this);
        mImage.setImageResource(R.drawable.color_background);
        mCurrentDampening = DAMPING_COEFFICIENT;

        initRippleScript();

        return view;
    }

    private void initRippleScript() {
        mImageRippleScript = new ScriptC_imageRipple(mRenderscript);
        mImageRippleScript.set_minRadius(0f);
        mImageRippleScript.set_amplitude(AMPLITUDE_COEFFICIENT);
        mImageRippleScript.set_damper(DAMPING_COEFFICIENT);
        mImageRippleScript.set_frequency(FREQUENCY_COEFFICIENT);
    }

    @Override
    public void onDestroy() {
        // Destroy Renderscript object
        mRenderscript.destroy();
        mAllocationIn.destroy();
        mAllocationOut.destroy();

        if (mRippleTask != null) {
            mRippleTask.cancel(true);
        }
        if (mFadeTask != null) {
            mFadeTask.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mImage.setImageResource(R.drawable.color_background);
        if (mRippleTask != null) {
            mRippleTask.cancel(true);
        }
        if (mFadeTask != null) {
            mFadeTask.cancel(true);
        }
        doRipple(motionEvent.getRawX(), motionEvent.getRawY());
        return false;
    }

    private void doRipple(float xPos, float yPos) {
        mRippleTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                drawRipplesOnBackground(mImage, xPos, yPos, mCurrentDampening, false);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                setImage();
                mCurrentDampening -= .001f;

                if (mCurrentDampening > 0) {
                    doRipple(xPos, yPos);
                } else {
                    doRippleFade(xPos, yPos);
                }
            }
        };
        mRippleTask.execute();
    }

    private void doRippleFade(float xPos, float yPos) {
        mFadeTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                drawRipplesOnBackground(mImage, xPos, yPos, mCurrentDampening, true);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                setImage();
                mCurrentDampening += .001f;

                if (mCurrentDampening <= DAMPING_COEFFICIENT) {
                    doRippleFade(xPos, yPos);
                }
            }
        };
        mFadeTask.execute();
    }

    private void setImage() {
        if (mResultBitmap != null) {
            Runnable imageRunnable = new Runnable() {
                @Override
                public void run() {
                    mImage.setImageBitmap(mResultBitmap);
                }
            };
            imageRunnable.run();
        }
    }

    private void drawRipplesOnBackground(ImageView imageView, float xPos, float yPos, float dampening, boolean reverse) {
        if (imageView != null && imageView.getDrawable() != null) {
            Bitmap in = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            Bitmap out = Bitmap.createBitmap(in.getWidth(), in.getHeight(), in.getConfig());

            // Create Allocations from bitmaps
            mAllocationIn = Allocation.createFromBitmap(mRenderscript, in, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            mAllocationOut = Allocation.createTyped(mRenderscript, mAllocationIn.getType());

            // Multiply by two due to Emulator resolution scale vs bitmap resolution
            mImageRippleScript.set_positionX(xPos * 2.0f);
            mImageRippleScript.set_positionY(yPos * 2.0f);
            mImageRippleScript.set_damper(dampening);

            //Run the script
            if (reverse) {
                mImageRippleScript.forEach_unRipple(mAllocationIn, mAllocationOut);
            } else {
                mImageRippleScript.forEach_root(mAllocationIn, mAllocationOut);
            }

            mAllocationOut.copyTo(out);
            mResultBitmap = out.copy(out.getConfig(), true);
        }
    }
}
