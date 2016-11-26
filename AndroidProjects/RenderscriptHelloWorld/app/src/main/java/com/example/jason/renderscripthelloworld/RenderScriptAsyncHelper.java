package com.example.jason.renderscripthelloworld;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.IntDef;
import android.widget.TextView;

import com.commit451.nativestackblur.NativeStackBlur;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

class RenderScriptAsyncHelper extends AsyncTask<Void, Void, Long> {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            SCRIPT_TYPE_STANDARD_BLUR,
            SCRIPT_TYPE_STACK_BLUR,
            SCRIPT_TYPE_EQUALIZE,
            SCRIPT_TYPE_INVERT
    })
    @interface ScriptType {}

    static final int SCRIPT_TYPE_STANDARD_BLUR = 0;
    static final int SCRIPT_TYPE_STACK_BLUR = 1;
    static final int SCRIPT_TYPE_EQUALIZE = 2;
    static final int SCRIPT_TYPE_INVERT = 3;

    private Context mContext;
    private TextView mTotalTimeLabel;
    private TextView mAverageTimeLabel;
    private int mScriptType;
    private int mMaxRuns;
    private float mBlurRadius;
    private double mTotalRunTime;

    RenderScriptAsyncHelper(Context context) {
        mContext = context;
    }g

    void init(@ScriptType int scriptType, int maxRuns, float blurRadius, TextView totalTimeLabel,
              TextView averageTimeLabel) {
        mScriptType = scriptType;
        mMaxRuns = maxRuns;
        mBlurRadius = blurRadius;
        mTotalTimeLabel = totalTimeLabel;
        mAverageTimeLabel = averageTimeLabel;
    }

    @Override
    protected Long doInBackground(Void... voids) {
        long startTime = System.currentTimeMillis();
        for (int j = 0; j < mMaxRuns; j++) {
            switch (mScriptType) {
                case SCRIPT_TYPE_STANDARD_BLUR:
                    standardGaussianBlur(mBlurRadius);
                    break;
                case SCRIPT_TYPE_STACK_BLUR:
                    nativeStackBlur(mBlurRadius);
                    break;
                case SCRIPT_TYPE_EQUALIZE:
                    equalize();
                    break;
                case SCRIPT_TYPE_INVERT:
                    invert();
                    break;
            }
        }
        long endTime = System.currentTimeMillis();

        return endTime - startTime;
    }

    @Override
    protected void onPostExecute(Long runTime) {
        mTotalRunTime += runTime;

        int resultsLabelId;
        switch (mScriptType) {
            case SCRIPT_TYPE_STANDARD_BLUR:
                resultsLabelId = R.string.standard_gaussian_result;
                break;
            case SCRIPT_TYPE_STACK_BLUR:
                resultsLabelId = R.string.native_stack_blur_result;
                break;
            case SCRIPT_TYPE_EQUALIZE:
                resultsLabelId = R.string.equalize_result;
                break;
            case SCRIPT_TYPE_INVERT:
                resultsLabelId = R.string.invert_result;
                break;
            default:
                resultsLabelId = R.string.default_result;
        }

        mTotalTimeLabel.setText(mContext.getString(resultsLabelId, mMaxRuns, runTime));

        // Calculate average
        final double averageTime = mTotalRunTime / mMaxRuns;
        mAverageTimeLabel.setText(mContext.getString(R.string.average_label, averageTime));
    }

    private void standardGaussianBlur(float blurRadius) {
        Bitmap blurredImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.landscape);
        Utils.gaussianBlur(blurredImage, mContext, blurRadius);
    }

    private void nativeStackBlur(float blurRadius) {
        Bitmap blurredImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.landscape);
        NativeStackBlur.process(blurredImage, Math.round(blurRadius));
    }

    private void equalize() {
        Bitmap equalizedImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.landscape);
        Utils.histogramEqualization(equalizedImage, mContext);
    }

    private void invert() {
        Bitmap invertedImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.landscape);
        Utils.invert(mContext, invertedImage);
    }

}
