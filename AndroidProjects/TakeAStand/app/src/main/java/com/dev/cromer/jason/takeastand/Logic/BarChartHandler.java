package com.dev.cromer.jason.takeastand.Logic;


import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Arrays;

public class BarChartHandler {

    private BarChart barChart;
    private ArrayList<String> xAxis;
    protected BarDataSet barDataSet;

    //constants
    private static final int LABELS_TO_SKIP = 0;
    private static final int LIST_START_INDEX = 0;
    private static final boolean SET_DRAW_VALUES = false;
    private static final int DARK_GREY_COLOR = Color.rgb(33,33,33);

    public BarChartHandler(BarChart chart){
        this.barChart = chart;
    }

    public void configureBarChartSettings(boolean drawBarShadow, boolean pinchZoom,
                                          boolean gridBackground, boolean legendOn, boolean xValuesOn,
                                          boolean yValuesOn){

        //Set to bottom position and prevent label skipping
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setLabelsToSkip(LABELS_TO_SKIP);

        //Set bar chart background and zoom capabilities
        barChart.setDrawBarShadow(drawBarShadow);
        barChart.setPinchZoom(pinchZoom);
        barChart.setDoubleTapToZoomEnabled(pinchZoom);

        //Grid Background enabled?
        barChart.setDrawGridBackground(gridBackground);

        //xAxis
        barChart.getXAxis().setEnabled(xValuesOn);
        barChart.getXAxis().setDrawGridLines(gridBackground);

        //yAxis
        barChart.getAxisRight().setDrawLabels(!gridBackground);
        barChart.getAxisRight().setDrawGridLines(gridBackground);
        barChart.getAxisRight().setEnabled(yValuesOn);

        barChart.getAxisLeft().setDrawLabels(!gridBackground);
        barChart.getAxisLeft().setDrawGridLines(gridBackground);
        barChart.getAxisLeft().setEnabled(yValuesOn);

        //Set legend on or off
        barChart.getLegend().setEnabled(legendOn);

        //Set text and background color
        barChart.getXAxis().setTextColor(Color.WHITE);
        barChart.setDescriptionColor(Color.WHITE);
        barChart.setBackgroundColor(DARK_GREY_COLOR);
        barChart.setGridBackgroundColor(DARK_GREY_COLOR);

    }

    public void setDataSet(int[] data, String dataName){
        ArrayList<BarEntry> valueSet = new ArrayList<>();

        //Parse through data input and add each value to our ValueSet. Start at 1 to avoid leading zero
        for(int i = 1; i < data.length; i++){
            BarEntry value = new BarEntry(data[i], i-1);
            valueSet.add(value);
        }

        //Add our completed valueSet to a BarDataSet
        barDataSet = new BarDataSet(valueSet, dataName);
        barDataSet.setColors(getReligionColors());
        barDataSet.setDrawValues(SET_DRAW_VALUES);
    }

    public BarDataSet getBarDataSet(){
        return barDataSet;
    }

    public void setXAxis(){
        final String[] religionTypes = getReligionTypes();
        xAxis = new ArrayList<>();

        //Add all contents of our religionTypes to the xAxis
        xAxis.addAll(Arrays.asList(religionTypes).subList(LIST_START_INDEX, religionTypes.length));
    }

    public ArrayList<String> getXAxis(){
        return xAxis;
    }



    public void createBarChart(String description){

        //ensure our x-Axis and data-entries are of the same length
        if(getBarDataSet().getEntryCountStacks() == getXAxis().size()){
            BarData data = new BarData(getXAxis(), getBarDataSet());
            barChart.setData(data);
            barChart.setDescription(description);
        }
        else{
            Log.d("BARCHART DATA", "xAxis and Dataset are not the same value");
        }
    }


    public void animateChart(int xValue, int yValue){
        barChart.animateXY(xValue, yValue);
    }


    private int[] getReligionColors(){
        return new int[]{
                Color.rgb(0,127,255),           //Azure
                Color.rgb(255,0,255),           //Magenta
                Color.rgb(0,0,255),             //Blue
                Color.rgb(255,0,127),           //Rose
                Color.rgb(255,0,0),             //Red
                Color.rgb(0,255,0),             //Green
                Color.rgb(255,255,0)            //Yellow
        };
    }

    private String[] getReligionTypes(){
        return new String[] {
                "Christian",
                "Islam",
                "Catholic",
                "Hindu",
                "Buddhist",
                "Agnostic",
                "Athiest"
        };
    }
}
