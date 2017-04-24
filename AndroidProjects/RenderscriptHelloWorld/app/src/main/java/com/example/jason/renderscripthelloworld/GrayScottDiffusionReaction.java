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
        void onDiffusionImageLoaded(Bitmap image);
    }

    private static final int IMAGE_DIMENSION = 1000;

    private Context mContext;
    private int mMaxRuns;
    private Listener mListener;

    // For diffusion reaction
    private double[] mU0 = new double[IMAGE_DIMENSION * IMAGE_DIMENSION];
    private double[] mV0 = new double[IMAGE_DIMENSION * IMAGE_DIMENSION];
    private double[] mU1 = new double[IMAGE_DIMENSION * IMAGE_DIMENSION];
    private double[] mV1 = new double[IMAGE_DIMENSION * IMAGE_DIMENSION];

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
        initRenderscriptObjects();
        mMaxRuns = iterations;

        try {
            mListener = (Listener) mContext;
        } catch (Exception e) {
            Log.d("Error: ", "Class not does implement Listener");
        }
        initDiffusionReaction();
    }

    private void initRenderscriptObjects() {
        mRenderscript = RenderScript.create(mContext);
        mScript = new ScriptC_GrayScottDiffusionReaction(mRenderscript);

        // Create Allocations for arrays
        Type t = new Type.Builder(mRenderscript, Element.F64(mRenderscript)).setX(IMAGE_DIMENSION * IMAGE_DIMENSION).create();
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
//            updateReactionDiffusionJava(mU0, mU1, mV0, mV1);
            setImageFromArray(mV0);
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

    private void setImageFromArray(double[] input) {
        final int[] intArray = new int[input.length];
        for (int i = 0; i < intArray.length; ++i) {
            intArray[i] = Color.rgb((int) (input[i] * 255), (int) (input[i] * 255), (int) (input[i] * 255));
        }

        Bitmap result = Bitmap.createBitmap(intArray, IMAGE_DIMENSION, IMAGE_DIMENSION, Bitmap.Config.ARGB_8888);
        mListener.onDiffusionImageLoaded(result);
    }

    private void initDiffusionReaction() {
        Arrays.fill(mU0, 1);
        Arrays.fill(mU1, 1);
        Arrays.fill(mV0, 0);
        Arrays.fill(mV1, 0);

        // set middle pixels
        mU0[(500 * IMAGE_DIMENSION) + 500] = .5;
        mU1[(500 * IMAGE_DIMENSION) + 500] = .5;
        mV0[(500 * IMAGE_DIMENSION) + 500] = .5;
        mV1[(500 * IMAGE_DIMENSION) + 500] = .5;

        mU0[(500 * IMAGE_DIMENSION) + 501] = .5;
        mU1[(500 * IMAGE_DIMENSION) + 501] = .5;
        mV0[(500 * IMAGE_DIMENSION) + 501] = .5;
        mV1[(500 * IMAGE_DIMENSION) + 501] = .5;

        mU0[(501 * IMAGE_DIMENSION) + 500] = .5;
        mU1[(501 * IMAGE_DIMENSION) + 500] = .5;
        mV0[(501 * IMAGE_DIMENSION) + 500] = .5;
        mV1[(501 * IMAGE_DIMENSION) + 500] = .5;

        mU0[(499 * IMAGE_DIMENSION) + 500] = .5;
        mU1[(499 * IMAGE_DIMENSION) + 500] = .5;
        mV0[(500 * IMAGE_DIMENSION) + 500] = .5;
        mV1[(499 * IMAGE_DIMENSION) + 500] = .5;

        mU0[(500 * IMAGE_DIMENSION) + 499] = .5;
        mU1[(500 * IMAGE_DIMENSION) + 499] = .5;
        mV0[(501 * IMAGE_DIMENSION) + 499] = .5;
        mV1[(501 * IMAGE_DIMENSION) + 499] = .5;
//
//        mU0[(501 * IMAGE_DIMENSION) + 501] = .5;
//        mU1[(501 * IMAGE_DIMENSION) + 501] = .5;
//        mV0[(501 * IMAGE_DIMENSION) + 501] = .5;
//        mV1[(501 * IMAGE_DIMENSION) + 501] = .5;

//        mU0[(502 * IMAGE_DIMENSION) + 500] = .5;
//        mU1[(502 * IMAGE_DIMENSION) + 500] = .5;
//        mV0[(502 * IMAGE_DIMENSION) + 500] = .5;
//        mV1[(502 * IMAGE_DIMENSION) + 500] = .5;
//
//        mU0[(502 * IMAGE_DIMENSION) + 501] = .5;
//        mU1[(502 * IMAGE_DIMENSION) + 501] = .5;
//        mV0[(502 * IMAGE_DIMENSION) + 501] = .5;
//        mV1[(502 * IMAGE_DIMENSION) + 501] = .5;

        // Create Allocation via our Bitmap. This serves as a dummy image to iterate over
        // while we operate on the u1 and v1 arrays
        Bitmap mInputDummy = Bitmap.createBitmap(IMAGE_DIMENSION, IMAGE_DIMENSION, Bitmap.Config.ARGB_8888);
        mInputAllocation = Allocation.createFromBitmap(mRenderscript, mInputDummy);
        mOutputAllocation = Allocation.createTyped(mRenderscript, mInputAllocation.getType(), Allocation.USAGE_GRAPHICS_TEXTURE);
    }

    private void updateReactionDiffusion() {
        long start = System.currentTimeMillis();
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
        Log.d("TOTAL TIME: ", String.valueOf(System.currentTimeMillis() - start));
    }

    private void updateReactionDiffusionJava(double[] u0, double[] u1, double[] v0, double[] v1) {
        long start = System.currentTimeMillis();
        for (int x = 1; x < IMAGE_DIMENSION - 1; x++) {
            for (int y = 1; y < IMAGE_DIMENSION - 1; y++) {
                double uv2 = elementAt(u1, x, y) * elementAt(v1, x, y) * elementAt(v1, x, y);

                double tempU1 = elementAt(u1, x, y)
                        + .2f * (elementAt(u1, x + 1, y) + elementAt(u1, x - 1, y)
                        + elementAt(u1, x, y + 1) + elementAt(u1, x, y - 1)
                        - 4 * elementAt(u1, x, y))
                        - uv2 + .025f * (1 - elementAt(u1, x, y));

                tempU1 = Math.min(1.0f, tempU1);
                setElementAt(u0, Math.max(0.0f, tempU1), x, y);

                double tempV1 = elementAt(v1, x, y) + .1 * (elementAt(v1, x + 1, y) + elementAt(v1, x - 1, y)
                        + elementAt(v1, x, y + 1) + elementAt(v1, x, y - 1) - 4 * elementAt(v1, x, y))
                        + uv2 - .08f * elementAt(v1, x, y);

                tempV1 = Math.min(1.0f, tempV1);
                setElementAt(v0, Math.max(0.0f, tempV1), x, y);
            }
        }
        Log.d("TOTAL TIME: ", String.valueOf(System.currentTimeMillis() - start));
    }

    private double elementAt(double[] input, int x, int y) {
        return input[(x * IMAGE_DIMENSION) + y];
    }

    private void setElementAt(double[] input, double val, int x, int y) {
        input[(x * IMAGE_DIMENSION) + y] = val;
    }

    void cleanRenderscriptObjects() {
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
