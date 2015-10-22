package com.dev.cromer.jason.takeastand.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.dev.cromer.jason.takeastand.Logic.RetrieveStatsHandler;
import com.dev.cromer.jason.takeastand.R;
import com.dev.cromer.jason.takeastand.networking.GenericHttpGetRequest;

public class GlobalStatsActivity extends AppCompatActivity {

    private static final String GET_NUM_RELIGION_TYPE = "http://takeastandapi.elasticbeanstalk.com/get_num_religion_type";
    protected int[] religionTypeNums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_stats);

        getReligionTypeData();
    }


    private void getReligionTypeData(){
        GenericHttpGetRequest httpGetRequest = new GenericHttpGetRequest();
        RetrieveStatsHandler statsHandler = new RetrieveStatsHandler(GET_NUM_RELIGION_TYPE, httpGetRequest);

        religionTypeNums = statsHandler.getNumOfReligionTypes();

        //Start at 1 to exclude the default 0 value appended to the start of the array
        for(int i = 1; i < religionTypeNums.length; i++){
            Log.d("TAG", String.valueOf(religionTypeNums[i]));
        }
    }

    @Override
    public boolean onNavigateUp() {
        Intent mapIntent = new Intent(this, MapsActivity.class);
        startActivity(mapIntent);
        finish();
        return true;
    }

}
