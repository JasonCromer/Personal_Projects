package com.example.jason.healthcaredemo;

import android.app.Activity;
import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;


public class ExerciseList extends Activity{

    private ListView listView;

    private String[] itemNames = {
            "Exercise1",
            "Exercise2",
            "Exercise3",
            "Exercise4",
            "Exercise5",
            "Exercise6",
            "Exercise7",
            "Exercise8",
            "Exercise9",
            "Exercise10",
            "Exercise11",
            "Exercise12",
            "Exercise13",
            "Exercise14",
            "Exercise15",
            "Exercise16",
            "Exercise17",
            "Exercise18",
            "Exercise19",
            "Exercise20",
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        //get listview object from xml
        listView = (ListView) findViewById(R.id.listView);

        //define a new adapter
        //params: (Context, Layout for row, ID of Textview, Array of data)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, itemNames);

        //Assign adapter to listView
        listView.setAdapter(adapter);

        //ListView item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int itemPosition = position;

                String itemValue = (String) listView.getItemAtPosition(position);

                Toast.makeText(getApplicationContext(), "Position :" + itemPosition +
                " List Item :" + itemValue, Toast.LENGTH_LONG).show();

            }
        });

    }
}
