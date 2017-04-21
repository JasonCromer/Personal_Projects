package com.example.jason.renderscripthelloworld;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.Type;
import android.util.Log;

import java.util.Arrays;

class GrayScottDiffusionReaction extends AsyncTask<Void, Void, Long> {

    interface Listener {
        void onDiffusionFinished(Bitmap result);
    }

    private static final int IMAGE_WIDTH = 1000;

    private Context mContext;
    private int mMaxRuns;
    private Listener mListener;
    private Bitmap mImageResult;

    // For diffusion reaction
    private double[] mU0 = new double[IMAGE_WIDTH * IMAGE_WIDTH];
    private double[] mV0 = new double[IMAGE_WIDTH * IMAGE_WIDTH];
    private double[] mU1 = new double[IMAGE_WIDTH * IMAGE_WIDTH];
    private double[] mV1 = new double[IMAGE_WIDTH * IMAGE_WIDTH];
    private Bitmap mInputDummy;

    // Renderscript Objects
    private RenderScript mRenderscript;
    private ScriptC_GrayScottDiffusionReaction mScript;
    private Allocation mInputAllocation;
    private Allocation mOutputAllocation;
    private Allocation mU0Allocation;
    private Allocation mU1Allocation;
    private Allocation mV0Allocation;
    private Allocation mV1Allocation;

    GrayScottDiffusionReaction(Context context, int iterations) {
        mContext = context;
        mMaxRuns = iterations;
        initRenderscriptObjects();
        initDiffusionReaction();

        try {
            mListener = (Listener) mContext;
        } catch (Exception e) {
            Log.d("Error: ", "Class not does implement Listener");
        }
    }

    private void initRenderscriptObjects() {
        mRenderscript = RenderScript.create(mContext);
        mScript = new ScriptC_GrayScottDiffusionReaction(mRenderscript);

        // Create Allocations for arrays
        Type t = new Type.Builder(mRenderscript, Element.F64(mRenderscript)).setX(1000 * 1000).create();
        mU0Allocation = Allocation.createTyped(mRenderscript, t);
        mU1Allocation = Allocation.createTyped(mRenderscript, t);
        mV0Allocation = Allocation.createTyped(mRenderscript, t);
        mV1Allocation = Allocation.createTyped(mRenderscript, t);
        mScript.set_u0(mU0Allocation);
        mScript.set_u1(mU1Allocation);
        mScript.set_v0(mV0Allocation);
        mScript.set_v1(mV1Allocation);
    }

    @Override
    protected Long doInBackground(Void... voids) {
        long startTime = System.currentTimeMillis();
        double[] tempArray;

        for (int i = 0; i < mMaxRuns; i++) {
            updateReactionDiffusion();
            tempArray = mU0;
            mU0 = mU1;
            mU1 = tempArray;
            tempArray = mV0;
            mV0 = mV1;
            mV1 = tempArray;
        }
        long endTime = System.currentTimeMillis();

        return endTime - startTime;
    }

    @Override
    protected void onPostExecute(Long aLong) {
        setImageFromArray(mU0);
    }

    private void setImageFromArray(double[] input) {
        final int[] intArray = new int[input.length];
        for (int i = 0; i < intArray.length; ++i) {
            intArray[i] = Color.rgb((int) (input[i] * 255), (int) (input[i] * 255), (int) (input[i] * 255));
        }

        mImageResult = Bitmap.createBitmap(intArray, 1000, 1000, Bitmap.Config.ARGB_8888);

        if (mImageResult != null) {
            Runnable imageRunnable = new Runnable() {
                @Override
                public void run() {
                    mListener.onDiffusionFinished(mImageResult);
                }
            };

            imageRunnable.run();
        }
    }

    private void initDiffusionReaction() {
        Arrays.fill(mU0, 1);
        Arrays.fill(mU1, 1);
        Arrays.fill(mV0, 0);
        Arrays.fill(mV1, 0);

        // set middle pixels
        mU0[(500 * 1000) + 500] = .5;
        mU1[(500 * 1000) + 500] = .5;
        mV0[(500 * 1000) + 500] = .5;
        mV1[(500 * 1000) + 500] = .5;

        mU0[(500 * 1000) + 501] = .5;
        mU1[(500 * 1000) + 501] = .5;
        mV0[(500 * 1000) + 501] = .5;
        mV1[(500 * 1000) + 501] = .5;

        mU0[(501 * 1000) + 500] = .5;
        mU1[(501 * 1000) + 500] = .5;
        mV0[(501 * 1000) + 500] = .5;
        mV1[(501 * 1000) + 500] = .5;

        mU0[(501 * 1000) + 501] = .5;
        mU1[(501 * 1000) + 501] = .5;
        mV0[(501 * 1000) + 501] = .5;
        mV1[(501 * 1000) + 501] = .5;

        mU0[(502 * 1000) + 500] = .5;
        mU1[(502 * 1000) + 500] = .5;
        mV0[(502 * 1000) + 500] = .5;
        mV1[(502 * 1000) + 500] = .5;

        mU0[(502 * 1000) + 501] = .5;
        mU1[(502 * 1000) + 501] = .5;
        mV0[(502 * 1000) + 501] = .5;
        mV1[(502 * 1000) + 501] = .5;

        // Create Allocation via our Bitmap. This serves as a dummy image to iterate over
        // while we operate on the u1 and v1 arrays
        mInputDummy = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        mInputAllocation = Allocation.createFromBitmap(mRenderscript, mInputDummy);
        mOutputAllocation = Allocation.createTyped(mRenderscript, mInputAllocation.getType());
    }

    private void updateReactionDiffusion() {
        // Copy Java array contents to Renderscript allocations
        mU0Allocation.copyFrom(mU0);
        mU1Allocation.copyFrom(mU1);
        mV0Allocation.copyFrom(mV0);
        mV1Allocation.copyFrom(mV1);

        // Invoke Diffusion Reaction kernel
        mScript.invoke_processImage(mInputAllocation, mOutputAllocation);

        // Copy Renderscript arrays back to Java context
        mScript.get_u0().copyTo(mU0);
        mScript.get_u1().copyTo(mU1);
        mScript.get_v0().copyTo(mV0);
        mScript.get_v1().copyTo(mV1);
    }

    public void cleanRenderscriptObjects() {
        mScript.destroy();
        mRenderscript.destroy();
        mInputAllocation.destroy();
        mOutputAllocation.destroy();
        mU0Allocation.destroy();
        mU1Allocation.destroy();
        mV0Allocation.destroy();
        mV1Allocation.destroy();
    }
}
