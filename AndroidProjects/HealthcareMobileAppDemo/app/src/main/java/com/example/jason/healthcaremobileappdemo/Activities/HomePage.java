package com.example.jason.healthcaremobileappdemo.Activities;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jason.healthcaremobileappdemo.Activities.FragmentManagement.CustomFragmentManager;
import com.example.jason.healthcaremobileappdemo.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public class HomePage extends Fragment implements View.OnClickListener {

    private View fragmentView;
    private Button startPlanButton;
    private BarChart exerciseBarChart;
    private PieChart planStatusPieChart;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.activity_home_page, container, false);

        //set up bar chart
        exerciseBarChart = (BarChart) fragmentView.findViewById(R.id.barChart);
        createBarChart(exerciseBarChart);

        //set up pie chart
        planStatusPieChart = (PieChart) fragmentView.findViewById(R.id.pieChart);


        startPlanButton = (Button) fragmentView.findViewById(R.id.startPlanButton);
        startPlanButton.setOnClickListener(this);


        return fragmentView;
    }

    @Override
    public void onClick(View v) {
        if(v == startPlanButton) {
            //Sets the fragment to position 1 with smooth scrolling enabled
            ((CustomFragmentManager)getActivity()).setCurrentItem(1, true);
            onDestroyView();

        }

    }


    public void createBarChart(BarChart thisChart) {

        //remove axis lines
        XAxis xAxis = thisChart.getXAxis();
        YAxis rightAxis = thisChart.getAxisRight();
        YAxis leftAxis = thisChart.getAxisLeft();
        xAxis.setDrawGridLines(false);
        rightAxis.setDrawGridLines(false);
        leftAxis.setDrawGridLines(false);

        //customize legend position
        Legend thislegend = thisChart.getLegend();
        thislegend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        //create bar data and add our data to it
        BarData data = new BarData(getXAxisValues(), getDataSet());
        thisChart.setData(data);
        thisChart.getAxisRight().setEnabled(false);
        thisChart.setDrawGridBackground(false);
        thisChart.setDescription("");
        thisChart.setDoubleTapToZoomEnabled(false);
        thisChart.setPinchZoom(false);
        thisChart.animateXY(1500, 1500);
        thisChart.invalidate();

    }


    private ArrayList<BarDataSet> getDataSet() {
        //instantiate a null dataset
        ArrayList<BarDataSet> dataSets = new ArrayList<>();

        //Create a value set for completion
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        //Create a Bar entry for episode completion each day
        BarEntry mondayComp = new BarEntry(110.000f, 0);
        valueSet1.add(mondayComp);
        BarEntry tuesdayComp = new BarEntry(40.000f, 1);
        valueSet1.add(tuesdayComp);
        BarEntry wednesdayComp = new BarEntry(60.000f, 2);
        valueSet1.add(wednesdayComp);
        BarEntry thursdayComp = new BarEntry(30.000f, 3);
        valueSet1.add(thursdayComp);
        BarEntry fridayComp = new BarEntry(100.000f, 4);
        valueSet1.add(fridayComp);

        //Create a value set for accuracy
        ArrayList<BarEntry> valueSet2 = new ArrayList<>();

        //Create a Bar entry for episode accuracy each day
        BarEntry mondayAcc = new BarEntry(150.000f, 0);
        valueSet2.add(mondayAcc);
        BarEntry tuesdayAcc = new BarEntry(90.000f, 1);
        valueSet2.add(tuesdayAcc);
        BarEntry wednesdayAcc = new BarEntry(120.000f, 2);
        valueSet2.add(wednesdayAcc);
        BarEntry thursdayAcc = new BarEntry(60.000f, 3);
        valueSet2.add(thursdayAcc);
        BarEntry fridayAcc = new BarEntry(20.000f, 4);
        valueSet2.add(fridayAcc);

        BarDataSet episodeCompletion = new BarDataSet(valueSet1, "Episode Completion");
        BarDataSet episodeAccuracy = new BarDataSet(valueSet2, "Episode Accuracy");

        episodeCompletion.setColor(Color.rgb(160, 209, 247));
        episodeCompletion.setDrawValues(false);
        episodeAccuracy.setColor(Color.rgb(217, 138, 222));
        episodeAccuracy.setDrawValues(false);

        dataSets.add(episodeCompletion);
        dataSets.add(episodeAccuracy);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Monday");
        xAxis.add("Tuesday");
        xAxis.add("Wednesday");
        xAxis.add("Thursday");
        xAxis.add("Friday");
        return xAxis;
    }


    private void setPieChartData(PieChart thisPieChart) {
    }
}
