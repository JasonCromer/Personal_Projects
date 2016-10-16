package com.example.jason.renderscripthelloworld;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.commit451.nativestackblur.NativeStackBlur;

public class FilterFragment extends Fragment implements View.OnClickListener {

    private static final float BLUR_RADIUS = 25.0f;

    private ImageView mBeforeImageView;
    private ImageView mAfterImageView;
    private TextView mTimeLabel;
    private TextView mTitleLabel;
    private Bitmap mBeforeImage;
    private Bitmap mAfterImage;

    public FilterFragment() {
        // Required empty constructor
    }

    public static FilterFragment newInstance() {
        return new FilterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        mBeforeImageView = (ImageView) view.findViewById(R.id.before_image_view);
        mAfterImageView = (ImageView) view.findViewById(R.id.after_image_view);
        mTimeLabel = (TextView) view.findViewById(R.id.time_label);
        mTitleLabel = (TextView) view.findViewById(R.id.algorithm_title);
        view.findViewById(R.id.next_button).setOnClickListener(this);

        // Convert drawable to bitmap
        mBeforeImage = BitmapFactory.decodeResource(getResources(), R.drawable.landscape);
        mBeforeImageView.setImageBitmap(mBeforeImage);

        // Equalize our before bitmap and set it to mAfterImageView
        //new HistogramEqualizationTask().execute();

        // Blur image using standard Gaussian Blur
        standardGaussianBlur();

        return view;
    }

    private void setResultBitmap(Long timeToComplete) {
        mAfterImageView.setImageBitmap(mAfterImage);
        mTimeLabel.setText(getResources().getString(R.string.time_label, timeToComplete));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_button:
                //slowEqualize(mBeforeImage);
                nativeStackBlur();
                break;
        }

    }

    private void standardGaussianBlur() {
        Bitmap blurredImage = BitmapFactory.decodeResource(getResources(), R.drawable.landscape);
        long startTime = System.currentTimeMillis();
        blurredImage = Utils.gaussianBlur(blurredImage, getContext(), BLUR_RADIUS);
        long endTime = System.currentTimeMillis();
        mTimeLabel.setText(getString(R.string.time_label, endTime - startTime));
        mTitleLabel.setText(getString(R.string.standard_gaussian_blur));
        mAfterImageView.setImageBitmap(blurredImage);
    }

    private void nativeStackBlur() {
        Bitmap blurredImage = BitmapFactory.decodeResource(getResources(), R.drawable.landscape);
        long startTime = System.currentTimeMillis();
        blurredImage = NativeStackBlur.process(blurredImage, Math.round(BLUR_RADIUS));
        long endTime = System.currentTimeMillis();
        mTimeLabel.setText(getString(R.string.time_label, endTime - startTime));
        mTitleLabel.setText(getString(R.string.native_stack_blur));
        mAfterImageView.setImageBitmap(blurredImage);

    }

    private void slowEqualize(Bitmap src) {
        long startTime = System.currentTimeMillis();

        float histogram[][];
        histogram = new float[3][];

        histogram[0] = getHistogramByColor(src, 1);
        histogram[1] = getHistogramByColor(src, 2);
        histogram[2] = getHistogramByColor(src, 3);

        normalizedFunction(histogram[0], 0, histogram[0].length - 1);
        normalizedFunction(histogram[1], 0, histogram[0].length - 1);
        normalizedFunction(histogram[2], 0, histogram[0].length - 1);

        histogramEqualization(histogram[0], 0, 255);
        histogramEqualization(histogram[1], 0, 255);
        histogramEqualization(histogram[2], 0, 255);

        mBeforeImageView.setImageBitmap(mBeforeImage);

        long endTime = System.currentTimeMillis();
        mTimeLabel.setText(getString(R.string.time_label, endTime - startTime));
        mAfterImageView.setImageBitmap(src);
    }

    public void histogramEqualization(float histogram[], int low, int high) {

        float sumr, sumrx;
        sumr = 0;
        for (int i = low; i <= high; i++) {
            sumr += (histogram[i]);
            sumrx = low + (high - low) * sumr;
            int valr = (int) (sumrx);
            if (valr > 255) {
                histogram[i] = 255;
            } else {
                histogram[i] = valr;
            }
        }
    }

    public void normalizedFunction(float myArr[], int low, int high) {

        float sumV = 0.0f;
        for (int i = low; i <= high; i++) {
            sumV = sumV + (myArr[i]);
        }
        for (int i = low; i <= high; i++) {
            myArr[i] /= sumV;
        }
    }

    public float[] getHistogramByColor(Bitmap input, int colorVal) {
        // colorVal 1 -> RED     2 -> GREEN     3 -> BLUE
        float[] histogram = new float[256];

        for (int i = 0; i < histogram.length; i++) {
            histogram[i] = 0.0f;
        }
        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                int red = 0;
                switch (colorVal) {
                    case 1:
                        red = Color.red(input.getPixel(i, j));
                        break;
                    case 2:
                        red = Color.green(input.getPixel(i, j));
                        break;
                    case 3:
                        red = Color.blue(input.getPixel(i, j));
                        break;
                }
                histogram[red]++;
            }
        }
        return histogram;
    }

    private class HistogramEqualizationTask extends AsyncTask<Void, Void, Long> {
        @Override
        protected Long doInBackground(Void... voids) {
            long startTime = System.currentTimeMillis();
            mAfterImage = Utils.histogramEqualization(mBeforeImage, getActivity());
            long endTime = System.currentTimeMillis();

            return endTime - startTime;
        }

        @Override
        protected void onPostExecute(Long timeToComplete) {
            setResultBitmap(timeToComplete);
        }
    }
}
