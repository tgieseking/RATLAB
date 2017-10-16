package edu.gatech.cs2340.ratlab.controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import edu.gatech.cs2340.ratlab.R;
import edu.gatech.cs2340.ratlab.model.Location;
import edu.gatech.cs2340.ratlab.model.LocationType;

public class ReportSightingActivity extends AppCompatActivity{
    Spinner locationSpinner;
    Spinner citySpinner;
    Spinner boroughSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_sighting);

        locationSpinner = (Spinner) findViewById(R.id.locationTypeSpinner);
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this,
                R.array.location_types, android.R.layout.simple_spinner_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        citySpinner = (Spinner) findViewById(R.id.citySpinner);
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this,
                R.array.city_names, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        boroughSpinner = (Spinner) findViewById(R.id.boroughSpinner);
        ArrayAdapter<CharSequence> boroughAdapter = ArrayAdapter.createFromResource(this,
                R.array.borough_names, android.R.layout.simple_spinner_item);
        boroughAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boroughSpinner.setAdapter(boroughAdapter);

        Button createReportButton = (Button) findViewById(R.id.reportSubmitButton);
        createReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                createSighting(getCurrentFocus());
            }
        });

    }

    public void createSighting(View view) {
        locationSpinner = (Spinner) findViewById(R.id.locationTypeSpinner);
        citySpinner = (Spinner) findViewById(R.id.citySpinner);
        boroughSpinner = (Spinner) findViewById(R.id.boroughSpinner);
        EditText addressEditText = (EditText) findViewById(R.id.addressEditText);
        EditText zipEditText = (EditText) findViewById(R.id.zipEditText);
        EditText longitudeEditText = (EditText) findViewById(R.id.longitudeEditText);
        EditText latitudeEditText = (EditText) findViewById(R.id.latitudeEditText);


    }
}
