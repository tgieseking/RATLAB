package edu.gatech.cs2340.ratlab.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.gatech.cs2340.ratlab.R;

public class MapFilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_filter);
    }

    public void onClickFilter(View view) {
        Intent intent = new Intent(this, MapsActivity.class);

        //TODO: replace this once the date selector is put in
        String format = "M/d/yyyy H:mm";
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date startDate = new Date();
        Date endDate = new Date();
        try {
            startDate = dateFormat.parse("8/23/2017 0:00");
            endDate = dateFormat.parse("8/25/2017 0:00");
        } catch (Exception e) {
            Log.e("filter_test", "parse error", e);
        }
        intent.putExtra("start_date", startDate.getTime());
        intent.putExtra("end_date", endDate.getTime());

        CheckBox bronxBox = (CheckBox) findViewById(R.id.bronxBox);
        intent.putExtra("bronx", bronxBox.isChecked());
        CheckBox brooklynBox = (CheckBox) findViewById(R.id.brooklynBox);
        intent.putExtra("brooklyn", brooklynBox.isChecked());
        CheckBox queensBox = (CheckBox) findViewById(R.id.queensBox);
        intent.putExtra("queens", queensBox.isChecked());
        CheckBox manhattanBox = (CheckBox) findViewById(R.id.manhattanBox);
        intent.putExtra("manhattan", manhattanBox.isChecked());
        CheckBox statenIslandBox = (CheckBox) findViewById(R.id.statenIslandBox);
        intent.putExtra("staten_island", statenIslandBox.isChecked());

        startActivity(intent);
    }
}
