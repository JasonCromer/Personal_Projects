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
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;

public class HomePage extends Fragment implements View.OnClickListener {

    private View fragmentView;
    private Button startPlanButton;
    private BarChart exerciseBarChart;
    private PieChart planStatusPieChart;

    private float[] pieYValues = {78f, 22};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.activity_home_page, container, false);

        //set up bar chart
        exerciseBarChart = (BarChart) fragmentView.findViewById(R.id.barChart);
        createBarChart(exerciseBarChart);

        //set up pie chart
        planStatusPieChart = (PieChart) fragmentView.findViewById(R.id.pieChart);
        createPieChart(planStatusPieChart);

        //set up the start plan button
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
        BarEntry mondayComp = new BarEntry(80.000f, 0);
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
        BarEntry mondayAcc = new BarEntry(50.000f, 0);
        valueSet2.add(mondayAcc);
        BarEntry tuesdayAcc = new BarEntry(90.000f, 1);
        valueSet2.add(tuesdayAcc);
        BarEntry wednesdayAcc = new BarEntry(20.000f, 2);
        valueSet2.add(wednesdayAcc);
        BarEntry thursdayAcc = new BarEntry(60.000f, 3);
        valueSet2.add(thursdayAcc);
        BarEntry fridayAcc = new BarEntry(40.000f, 4);
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


    private void createPieChart(PieChart thisPieChart) {

        thisPieChart.setDescription("");
        thisPieChart.setDrawHoleEnabled(true);
        thisPieChart.setHoleRadius(45f);
        thisPieChart.setHoleColorTransparent(true);
        thisPieChart.setTransparentCircleRadius(45f);
        thisPieChart.setRotationEnabled(false);
        thisPieChart.setUsePercentValues(true);


        Legend thisLegend = thisPieChart.getLegend();
        thisLegend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        setPieChartData(thisPieChart);
        thisPieChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);

    }


    private void setPieChartData(PieChart pieChart) {

        ArrayList<Entry> yVals1 = new ArrayList<>();
        yVals1.add(new Entry(pieYValues[0], 0));
        yVals1.add(new Entry(pieYValues[1], 1));


        ArrayList<String> xVals = new ArrayList<>();
        xVals.add("Completion");
        xVals.add("Incomplete");

        PieDataSet dataSet = new PieDataSet(yVals1, "");

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(217,138,222));
        colors.add(Color.GRAY);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }
}
