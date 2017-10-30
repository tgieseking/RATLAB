package edu.gatech.cs2340.ratlab.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.gatech.cs2340.ratlab.R;

public class HistogramFilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histogram_filter);
    }

    public void onClickFilterGraph(View view) {
        Intent intent = new Intent(this, HistoricalGraphActivity.class);

        DatePicker startDatePicker = (DatePicker) findViewById(R.id.startDatePicker);
        DatePicker endDatePicker = (DatePicker) findViewById(R.id.endDatePicker);

        int startDay = startDatePicker.getDayOfMonth();
        int startMonth = endDatePicker.getMonth() + 1;
        int startYear = startDatePicker.getYear();
        int endDay = endDatePicker.getDayOfMonth();
        int endMonth = startDatePicker.getMonth() + 1;
        int endYear = endDatePicker.getYear();

        String format = "M/d/yyyy H:mm";
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date startDate = new Date();
        Date endDate = new Date();
        String start = startMonth + "/" + startDay + "/" + startYear + " 0:00";
        String end = endMonth + "/" + endDay + "/" + endYear + " 23:59";

        //not sure if this needs to be a try catch but it was so I'm gonna leave it that way
        try {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
        } catch (Exception e) {
            Log.e("filter_test", "parse error", e);
        }

        intent.putExtra("start_date", startDate.getTime());
        intent.putExtra("end_date", endDate.getTime());

        CheckBox bronxBox = (CheckBox) findViewById(R.id.bronxCheckBox);
        intent.putExtra("bronx", bronxBox.isChecked());
        CheckBox brooklynBox = (CheckBox) findViewById(R.id.brooklynCheckBox);
        intent.putExtra("brooklyn", brooklynBox.isChecked());
        CheckBox queensBox = (CheckBox) findViewById(R.id.queensCheckBox);
        intent.putExtra("queens", queensBox.isChecked());
        CheckBox manhattanBox = (CheckBox) findViewById(R.id.manhattanCheckBox);
        intent.putExtra("manhattan", manhattanBox.isChecked());
        CheckBox statenIslandBox = (CheckBox) findViewById(R.id.statenCheckBox);
        intent.putExtra("staten_island", statenIslandBox.isChecked());

        startActivity(intent);
    }
}
