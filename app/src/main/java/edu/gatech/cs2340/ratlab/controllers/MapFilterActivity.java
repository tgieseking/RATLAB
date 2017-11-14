package edu.gatech.cs2340.ratlab.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.DatePicker;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.gatech.cs2340.ratlab.R;

/**
 * Activity that displays the filter for the map.
 */
public class MapFilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_filter);
    }

    /**
     * Creates a MapsActivity instance when view is clicked.
     *
     * @param view filter button
     */
    public void onClickFilter(View view) {
        Intent intent = new Intent(this, MapsActivity.class);

        DatePicker startDatePicker = (DatePicker) findViewById(R.id.startDatePicker);
        DatePicker endDatePicker = (DatePicker) findViewById(R.id.endDatePicker);

        int startDay = startDatePicker.getDayOfMonth();
        int startMonth = endDatePicker.getMonth() + 1;
        int startYear = startDatePicker.getYear();
        int endDay = endDatePicker.getDayOfMonth();
        int endMonth = startDatePicker.getMonth() + 1;
        int endYear = endDatePicker.getYear();

        String start = startMonth + "/" + startDay + "/" + startYear + " 0:00";
        String end = endMonth + "/" + endDay + "/" + endYear + " 23:59";
        Date startDate = parseDate(start);
        Date endDate = parseDate(end);

        intent.putExtra("start_date", startDate.getTime());
        intent.putExtra("end_date", endDate.getTime());

        Checkable bronxBox = (CheckBox) findViewById(R.id.bronxBox);
        intent.putExtra("bronx", bronxBox.isChecked());
        Checkable brooklynBox = (CheckBox) findViewById(R.id.brooklynBox);
        intent.putExtra("brooklyn", brooklynBox.isChecked());
        Checkable queensBox = (CheckBox) findViewById(R.id.queensBox);
        intent.putExtra("queens", queensBox.isChecked());
        Checkable manhattanBox = (CheckBox) findViewById(R.id.manhattanBox);
        intent.putExtra("manhattan", manhattanBox.isChecked());
        Checkable statenIslandBox = (CheckBox) findViewById(R.id.statenIslandBox);
        intent.putExtra("staten_island", statenIslandBox.isChecked());
        Checkable unknownBox = (CheckBox) findViewById(R.id.unknownBox);
        intent.putExtra("unknown_borough", unknownBox.isChecked());

        startActivity(intent);
    }

    private Date parseDate(String dateString) {
        String format = "M/d/yyyy H:mm";
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        try {
            return dateFormat.parse(dateString);
        } catch (Exception e) {
            Log.e("filter_test", "parse error", e);
            return new Date();
        }
    }
}
