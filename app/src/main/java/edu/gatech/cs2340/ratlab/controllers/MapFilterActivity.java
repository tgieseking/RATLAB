package edu.gatech.cs2340.ratlab.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import edu.gatech.cs2340.ratlab.R;

public class MapFilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_filter);
    }

    public void onClickFilter(View view) {
        //method call to load filtered data
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
