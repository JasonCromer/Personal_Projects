package com.example.jason.renderscripthelloworld;

import android.os.Build;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FilterFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final float BLUR_RADIUS = 25.0f;

    private Spinner mSpinner;
    private TextView mStandardGaussianResultsLabel;
    private TextView mStandardGaussianAverageLabel;
    private TextView mNativeStackBlurResultsLabel;
    private TextView mNativeStackBlurAverageLabel;

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
        mStandardGaussianAverageLabel = (TextView) view.findViewById(R.id.standard_gaussian_average_label);
        mNativeStackBlurResultsLabel = (TextView) view.findViewById(R.id.native_stack_blur_results_label);
        mNativeStackBlurAverageLabel = (TextView) view.findViewById(R.id.native_stack_blur_average_label);

        // set toolbar title
        setToolbarTitle();

        // initialize spinner with items
        initSpinner();

        return view;
    }

    private void setToolbarTitle() {
        getActivity().setTitle(getString(R.string.toolbar_title));
    }

    private void initSpinner() {
        int[] runTimesList = new int[] {0, 1, 5, 10, 20, 30, 40, 50};
        List<Integer> mSpinnerList = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mSpinnerList = IntStream.of(runTimesList).boxed().collect(Collectors.toList());
        } else {
            for (int i : runTimesList) {
                mSpinnerList.add(i);
            }
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mSpinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        final int maxRuns = (int) adapterView.getItemAtPosition(i);

        executeBenchmark(maxRuns, RenderScriptAsyncHelper.SCRIPT_TYPE_STANDARD_BLUR,
                RenderScriptAsyncHelper.SCRIPT_TYPE_STACK_BLUR);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void executeBenchmark(int maxRuns, @RenderScriptAsyncHelper.ScriptType int firstScriptType,
                                  @RenderScriptAsyncHelper.ScriptType int secondScriptType) {
        // Benchmark first algorithm
        RenderScriptAsyncHelper mRenderScriptHelper = new RenderScriptAsyncHelper(getContext());
        mRenderScriptHelper.init(firstScriptType, maxRuns, BLUR_RADIUS, mStandardGaussianResultsLabel, mStandardGaussianAverageLabel);
        mRenderScriptHelper.execute();

        // Benchmark second algorithm
        mRenderScriptHelper = new RenderScriptAsyncHelper(getContext());
        mRenderScriptHelper.init(secondScriptType, maxRuns, BLUR_RADIUS, mNativeStackBlurResultsLabel, mNativeStackBlurAverageLabel);
        mRenderScriptHelper.execute();
    }
}
