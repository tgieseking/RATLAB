package edu.gatech.cs2340.ratlab.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import edu.gatech.cs2340.ratlab.R;
import edu.gatech.cs2340.ratlab.model.Borough;
import edu.gatech.cs2340.ratlab.model.Location;
import edu.gatech.cs2340.ratlab.model.LocationType;
import edu.gatech.cs2340.ratlab.model.RatSighting;
import edu.gatech.cs2340.ratlab.model.SightingsManager;


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
    }

    /**
     * Creates a new rat sighting from the input
     * @param view the current view
     */
    public void createSighting(View view) {
        locationSpinner = (Spinner) findViewById(R.id.locationTypeSpinner);
        citySpinner = (Spinner) findViewById(R.id.citySpinner);
        boroughSpinner = (Spinner) findViewById(R.id.boroughSpinner);
        EditText addressView = (EditText) findViewById(R.id.addressEditText);
        EditText zipView = (EditText) findViewById(R.id.zipEditText);
        EditText longitudeView = (EditText) findViewById(R.id.longitudeEditText);
        EditText latitudeView = (EditText) findViewById(R.id.latitudeEditText);

        Date currentDate = Calendar.getInstance().getTime();
        LocationType locationType = LocationType.locationTypeFromTextName(locationSpinner.getSelectedItem().toString());
        String addressLine = addressView.getText().toString();
        if (addressLine.length() == 0) {
            Toast.makeText(this, "Address field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String zipCode = zipView.getText().toString();
        if (zipCode.length() == 0) {
            Toast.makeText(this, "Zip code field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String city = citySpinner.getSelectedItem().toString();
        Borough borough = Borough.valueOf(boroughSpinner.getSelectedItem().toString());
        Double latitude,longitude;
        String latitudeString = latitudeView.getText().toString();
        if (latitudeString.length() == 0) {
            Toast.makeText(this, "latitude field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            latitude = Double.parseDouble(latitudeString);
        } catch (Exception e) {
            Toast.makeText(this, "Could not parse latitude field",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (latitude <  -90.0 || latitude > 90.0) {
            Toast.makeText(this, "latitude must be between -90 and 90",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String longitudeString = longitudeView.getText().toString();
        if (longitudeString.length() == 0) {
            Toast.makeText(this, "longitude field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            longitude = Double.parseDouble(longitudeString);
        } catch (Exception e) {
            Toast.makeText(this, "Could not parse longitude field",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (longitude <  -180.0 || longitude > 180.0) {
            Toast.makeText(this, "latitude must be between -90 and 90",
                    Toast.LENGTH_SHORT).show();
            return;
        }


        RatSighting newSighting = new RatSighting(null, currentDate, locationType, addressLine,
                zipCode, city, borough, latitude, longitude);
        SightingsManager.getInstance().addRatSightingToDatabase(newSighting);

        finish();
    }

    /**
     * Closes current activity for most recent activity on stack.
     *
     * @param view cancel button clicked
     */
    public void onClickCancel(View view) {
        finish();
    }
}
