package edu.gatech.cs2340.ratlab.controllers;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import edu.gatech.cs2340.ratlab.R;
import edu.gatech.cs2340.ratlab.model.Borough;
import edu.gatech.cs2340.ratlab.model.LocationType;
import edu.gatech.cs2340.ratlab.model.RatSighting;
import edu.gatech.cs2340.ratlab.model.SightingsManager;

public class HistoricalGraphActivity extends AppCompatActivity {

    private Set<Set<RatSighting>> sightingsByBorough;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_graph);
        Intent startIntent = getIntent();

        LineChart reportLineChart = (LineChart) findViewById(R.id.reportLineChart);

        Date startDate = new Date(startIntent.getLongExtra("start_date", 0));
        Date endDate = new Date(startIntent.getLongExtra("end_date", 0));
        Set<LocationType> locationTypes = new HashSet<>(Arrays.asList(LocationType.values()));

        sightingsByBorough = new HashSet<Set<RatSighting>>();

        Log.d("parcel_test", "1");
        Set<Borough> boroughs = new HashSet<>();
        if (startIntent.getBooleanExtra("bronx", false)) {
            boroughs.add(Borough.BRONX);
            sightingsByBorough.add(SightingsManager.getInstance()
                    .filterRatSightings(startDate, endDate, boroughs, locationTypes, 1000));
            boroughs.clear();
            Log.d("filters", "bronx");
        }
        if (startIntent.getBooleanExtra("brooklyn", false)) {
            boroughs.add(Borough.BROOKLYN);
            sightingsByBorough.add(SightingsManager.getInstance()
                    .filterRatSightings(startDate, endDate, boroughs, locationTypes, 1000));
            boroughs.clear();
            Log.d("filters", "brooklyn");
        }
        if (startIntent.getBooleanExtra("queens", false)) {
            boroughs.add(Borough.QUEENS);
            sightingsByBorough.add(SightingsManager.getInstance()
                    .filterRatSightings(startDate, endDate, boroughs, locationTypes, 1000));
            boroughs.clear();
            Log.d("filters", "queens");
        }
        if (startIntent.getBooleanExtra("manhattan", false)) {
            boroughs.add(Borough.MANHATTAN);
            sightingsByBorough.add(SightingsManager.getInstance()
                    .filterRatSightings(startDate, endDate, boroughs, locationTypes, 1000));
            boroughs.clear();
            Log.d("filters", "manhattan");
        }
        if (startIntent.getBooleanExtra("staten_island", false)) {
            boroughs.add(Borough.STATEN_ISLAND);
            sightingsByBorough.add(SightingsManager.getInstance()
                    .filterRatSightings(startDate, endDate, boroughs, locationTypes, 1000));
            boroughs.clear();
            Log.d("filters", "staten_island");
        }

        double timeRange = (endDate.getTime() - startDate.getTime()) / 10;
        //iterates through the individual borough sets of sightings
        List<ILineDataSet> boroughDataSets = new ArrayList<ILineDataSet>();
        int colorCount = 0;
        int[] colorArray = {Color.rgb(82, 94, 97), Color.rgb(218, 121, 89),
                Color.rgb(146, 171, 178), Color.rgb(102, 136, 145), Color.rgb(194, 201, 115)};
        for (Set<RatSighting> boroughSet : sightingsByBorough) {
            int[] rangeValues = new int[10];
            String borough = "Unknown";
            for (RatSighting sighting : boroughSet) {
                borough = sighting.getBorough().toString();
                //adds one to the time range that this entry fits into
                rangeValues[(int)((Math.abs(startDate.getTime() - sighting.getCreatedDate().getTime()))
                        / timeRange)]++;
            }
            List<Entry> boroughData = new ArrayList<Entry>();
            for (int i = 0; i < 10; i++) {
                boroughData.add(new Entry((float) (startDate.getTime() + (i * timeRange)),
                        (float) rangeValues[i]));
            }
            LineDataSet toAdd = new  LineDataSet(boroughData, borough);
            toAdd.setColor(colorArray[colorCount]);
            toAdd.setLineWidth(1.25f);
            toAdd.setCircleColor(colorArray[colorCount]);
            toAdd.setCircleRadius(3f);
            boroughDataSets.add(toAdd);
            colorCount++;
        }
        LineData data = new LineData(boroughDataSets);
        reportLineChart.setData(data);
        XAxis xAxis = reportLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(7.5f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);
        xAxis.setLabelCount(10, true);
        xAxis.setLabelRotationAngle(30);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String format = "M/d/yyyy H:mm";
                DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
                Date axisValue = new Date((long) value);
                return dateFormat.format(axisValue);
            }
        });
        //this updates the graph
        Description chartDescription = new Description();
        chartDescription.setText("Rat sightings by borough over time");
        reportLineChart.setDescription(chartDescription);
        reportLineChart.invalidate();
    }
}
