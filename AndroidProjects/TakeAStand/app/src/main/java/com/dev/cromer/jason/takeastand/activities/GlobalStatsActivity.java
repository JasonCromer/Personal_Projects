package com.dev.cromer.jason.takeastand.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dev.cromer.jason.takeastand.Logic.BarChartHandler;
import com.dev.cromer.jason.takeastand.Logic.RetrieveStatsHandler;
import com.dev.cromer.jason.takeastand.R;
import com.dev.cromer.jason.takeastand.networking.GenericHttpGetRequest;
import com.github.mikephil.charting.charts.BarChart;

public class GlobalStatsActivity extends AppCompatActivity {

    protected BarChart barChart;

    //constants
    private static final String GET_NUM_RELIGION_TYPE = "http://takeastandapi.elasticbeanstalk.com/get_num_religion_type";
    private static final String BAR_CHART_DATA_NAME = "";
    private static final String BAR_CHART_TITLE = "";
    private static final int xValueAnimation = 2000;
    private static final int yValueAnimation = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_stats);

        barChart = (BarChart) findViewById(R.id.barChart);
        createBarChart();
    }


    private void createBarChart(){
        final int[] religionTypeNumsData = getReligionTypeData();
        BarChartHandler barChartHandler = new BarChartHandler(barChart);

        //Customize settings
        barChartHandler.configureBarChartSettings(false, true, false, true, false, true, false);

        //Set the dataSet (yAxis) and xAxis
        barChartHandler.setDataSet(religionTypeNumsData, BAR_CHART_DATA_NAME);
        barChartHandler.setXAxis();

        //Use our data to create and animate the chart
        barChartHandler.createBarChart(BAR_CHART_TITLE);
        barChartHandler.animateChart(xValueAnimation, yValueAnimation);
    }


    private int[] getReligionTypeData(){
        final int[] religionTypeNums;

        GenericHttpGetRequest httpGetRequest = new GenericHttpGetRequest();
        RetrieveStatsHandler statsHandler = new RetrieveStatsHandler(GET_NUM_RELIGION_TYPE, httpGetRequest);

        religionTypeNums = statsHandler.getNumOfReligionTypes();

        return religionTypeNums;
    }

    @Override
    public boolean onNavigateUp() {
        Intent mapIntent = new Intent(this, MapsActivity.class);
        startActivity(mapIntent);
        finish();
        return true;
    }

}
