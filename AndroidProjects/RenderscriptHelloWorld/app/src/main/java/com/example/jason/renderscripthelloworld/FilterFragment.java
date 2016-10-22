package com.example.jason.renderscripthelloworld;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.commit451.nativestackblur.NativeStackBlur;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final float BLUR_RADIUS = 25.0f;

    private Spinner mSpinner;
    private TextView mStandardGaussianResultsLabel;
    private TextView mNativeStackBlurResultsLabel;

    private int mSelectedAmount;

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

        // Convert drawable to bitmap
        mSpinner = (Spinner) view.findViewById(R.id.spinner);
        mSpinner.setOnItemSelectedListener(this);
        mStandardGaussianResultsLabel = (TextView) view.findViewById(R.id.standard_gaussian_results_label);
        mNativeStackBlurResultsLabel = (TextView) view.findViewById(R.id.native_stack_blur_results_label);

        // set toolbar title
        setToolbarTitle();

        // initialize spinner with items
        initSpinner();

        // Equalize our before bitmap and set it to mAfterImageView
        //new HistogramEqualizationTask().execute();

        return view;
    }

    private void setToolbarTitle() {
        getActivity().setTitle(getString(R.string.gaussian_blur_toolbar_title));
    }

    private void initSpinner() {
        List<Integer> mSpinnerList = new ArrayList<>();
        mSpinnerList.add(0);
        mSpinnerList.add(1);
        mSpinnerList.add(5);
        mSpinnerList.add(10);
        mSpinnerList.add(20);
        mSpinnerList.add(30);
        mSpinnerList.add(40);
        mSpinnerList.add(50);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mSpinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
    }

    private void standardGaussianBlur() {
        Bitmap blurredImage = BitmapFactory.decodeResource(getResources(), R.drawable.landscape);
        Utils.gaussianBlur(blurredImage, getContext(), BLUR_RADIUS);
    }

    private void nativeStackBlur() {
        Bitmap blurredImage = BitmapFactory.decodeResource(getResources(), R.drawable.landscape);
        NativeStackBlur.process(blurredImage, Math.round(BLUR_RADIUS));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mSelectedAmount = (int) adapterView.getItemAtPosition(i);
        new StandardGaussianTask().execute();
        new NativeStackBlurTask().execute();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private class StandardGaussianTask extends AsyncTask<Void, Void, Long> {

        @Override
        protected Long doInBackground(Void... voids) {
            long startTime = System.currentTimeMillis();
            for (int j = 0; j < mSelectedAmount; j++) {
                standardGaussianBlur();
            }
            long endTime = System.currentTimeMillis();

            return endTime - startTime;
        }

        @Override
        protected void onPostExecute(Long runTime) {
            mStandardGaussianResultsLabel.setText(getString(R.string.standard_gaussian_result, mSelectedAmount, runTime));
        }
    }

    private class NativeStackBlurTask extends AsyncTask<Void, Void, Long> {

        @Override
        protected Long doInBackground(Void... voids) {
            long startTime = System.currentTimeMillis();
            for (int j = 0; j < mSelectedAmount; j++) {
                nativeStackBlur();
            }
            long endTime = System.currentTimeMillis();

            return endTime - startTime;
        }

        @Override
        protected void onPostExecute(Long runTime) {
            mNativeStackBlurResultsLabel.setText(getString(R.string.native_stack_blur_result, mSelectedAmount, runTime));
        }
    }

    private class HistogramEqualizationTask extends AsyncTask<Void, Void, Long> {
        @Override
        protected Long doInBackground(Void... voids) {
            Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.landscape);
            long startTime = System.currentTimeMillis();
            Utils.histogramEqualization(image, getActivity());
            long endTime = System.currentTimeMillis();

            return endTime - startTime;
        }

        @Override
        protected void onPostExecute(Long timeToComplete) {
        }
    }
}
