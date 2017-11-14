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
 * Class that filters for the graph that displays the
 * history of rat sightings depending on time range and borough.
 */
public class HistogramFilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histogram_filter);
    }

    /**
     * Takes the filter input and grabs the appropriate data
     * that matches the filter input.
     *
     * @param view filter button
     */
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


        String start = startMonth + "/" + startDay + "/" + startYear + " 0:00";
        String end = endMonth + "/" + endDay + "/" + endYear + " 23:59";
        Date startDate = parseDate(start);
        Date endDate = parseDate(end);

        intent.putExtra("start_date", startDate.getTime());
        intent.putExtra("end_date", endDate.getTime());

        Checkable bronxBox = (CheckBox) findViewById(R.id.bronxCheckBox);
        intent.putExtra("bronx", bronxBox.isChecked());
        Checkable brooklynBox = (CheckBox) findViewById(R.id.brooklynCheckBox);
        intent.putExtra("brooklyn", brooklynBox.isChecked());
        Checkable queensBox = (CheckBox) findViewById(R.id.queensCheckBox);
        intent.putExtra("queens", queensBox.isChecked());
        Checkable manhattanBox = (CheckBox) findViewById(R.id.manhattanCheckBox);
        intent.putExtra("manhattan", manhattanBox.isChecked());
        Checkable statenIslandBox = (CheckBox) findViewById(R.id.statenCheckBox);
        intent.putExtra("staten_island", statenIslandBox.isChecked());

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
