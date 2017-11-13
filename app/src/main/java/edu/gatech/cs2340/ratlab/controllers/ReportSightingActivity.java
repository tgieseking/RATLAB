package edu.gatech.cs2340.ratlab.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import edu.gatech.cs2340.ratlab.model.LocationType;
import edu.gatech.cs2340.ratlab.model.RatSighting;
import edu.gatech.cs2340.ratlab.model.SightingsManager;


public class ReportSightingActivity extends AppCompatActivity{
    private Spinner locationSpinner;
    private EditText cityEditText;
    private Spinner boroughSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_sighting);

        locationSpinner = (Spinner) findViewById(R.id.locationTypeSpinner);
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this,
                R.array.location_types, android.R.layout.simple_spinner_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        cityEditText = (EditText) findViewById(R.id.cityEditText);

        boroughSpinner = (Spinner) findViewById(R.id.boroughSpinner);
        ArrayAdapter<CharSequence> boroughAdapter = ArrayAdapter.createFromResource(this,
                R.array.borough_names, android.R.layout.simple_spinner_item);
        boroughAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boroughSpinner.setAdapter(boroughAdapter);

        EditText addressEditText = (EditText) findViewById(R.id.addressEditText);
        EditText zipEditText = (EditText) findViewById(R.id.zipEditText);
        EditText latitudeEditText = (EditText) findViewById(R.id.latitudeEditText);
        EditText longitudeEditText = (EditText) findViewById(R.id.longitudeEditText);
        EditText stateEditText = (EditText) findViewById(R.id.stateEditText);

        Button createReportButton = (Button) findViewById(R.id.reportSubmitButton);

        Intent intent = getIntent();
        if (intent.hasExtra("address")) {
            android.location.Address address = intent.getParcelableExtra("address");

            String street = address.getThoroughfare();
            String streetNumber = address.getSubThoroughfare();
            if ((street != null) && (streetNumber != null)) {
                addressEditText.setText(streetNumber + " " + street);
            }

            String zipCode = address.getPostalCode();
            if (zipCode != null) {
                zipEditText.setText(zipCode);
            }

            String state = address.getAdminArea();
            if (state != null) {
                stateEditText.setText(state);
            }

            String city = address.getLocality();
            if (city != null) {
                cityEditText.setText(city);
            }

            String subLocality = address.getSubLocality();
            if (subLocality != null) {
                Log.d("address_parse", subLocality);
                Borough borough = Borough.getBoroughFromTextName(subLocality);
                Log.d("address_parse", borough.toString());
                int selection = 5;
                switch (borough) {
                    case BRONX: selection = 0;
                        break;
                    case BROOKLYN: selection = 1;
                        break;
                    case MANHATTAN: selection = 2;
                        break;
                    case QUEENS: selection = 3;
                        break;
                    case STATEN_ISLAND: selection = 4;
                        break;
                    case UNKNOWN: selection = 5;
                        break;
                }
                final int selectionFinal = selection;
                Log.d("address_parse", "" + selectionFinal);
                boroughSpinner.post(new Runnable() {
                    @Override
                    public void run() {
                        boroughSpinner.setSelection(selectionFinal);
                    }
                });
                if (borough != Borough.UNKNOWN) {
                    // We do this because some of new york city has its borough as a sub-locality
                    // and no locality
                    cityEditText.setText("New York");
                }
            } else {
                boroughSpinner.post(new Runnable() {
                    @Override
                    public void run() {
                        boroughSpinner.setSelection(5);
                    }
                });
            }



            if (address.hasLatitude() && address.hasLongitude()) {
                latitudeEditText.setText("" + address.getLatitude());
                longitudeEditText.setText("" + address.getLongitude());
            }
        }
    }

    /**
     * Creates a new rat sighting from the input
     * @param view the current view
     */
    public void createSighting(View view) {
        locationSpinner = (Spinner) findViewById(R.id.locationTypeSpinner);
        EditText cityEditText = (EditText) findViewById(R.id.cityEditText);
        boroughSpinner = (Spinner) findViewById(R.id.boroughSpinner);
        EditText addressView = (EditText) findViewById(R.id.addressEditText);
        EditText zipView = (EditText) findViewById(R.id.zipEditText);
        EditText stateView = (EditText) findViewById(R.id.stateEditText);
        EditText longitudeView = (EditText) findViewById(R.id.longitudeEditText);
        EditText latitudeView = (EditText) findViewById(R.id.latitudeEditText);

        Date currentDate = Calendar.getInstance().getTime();
        LocationType locationType = LocationType.locationTypeFromTextName(
                locationSpinner.getSelectedItem().toString());
        String addressLine = addressView.getText().toString();
        if (addressLine.isEmpty()) {
            Toast.makeText(this, "Address field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String zipCode = zipView.getText().toString();
        if (zipCode.isEmpty()) {
            Toast.makeText(this, "Zip code field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String city = cityEditText.getText().toString();
        String state = stateView.getText().toString();
        Borough borough = Borough.getBoroughFromTextName(
                boroughSpinner.getSelectedItem().toString());
        Double latitude;
        Double longitude;
        String latitudeString = latitudeView.getText().toString();
        if (latitudeString.isEmpty()) {
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
        final double minLat = -90.0;
        final double maxLat = 90.0;
        if ((latitude <  minLat) || (latitude > maxLat)) {
            Toast.makeText(this, "latitude must be between -90 and 90",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String longitudeString = longitudeView.getText().toString();
        if (longitudeString.isEmpty()) {
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
        final double minLng = -180.0;
        final double maxLng = 180.0;
        if ((longitude <  minLng) || (longitude > maxLng)) {
            Toast.makeText(this, "latitude must be between -90 and 90",
                    Toast.LENGTH_SHORT).show();
            return;
        }


        RatSighting newSighting = new RatSighting(null, currentDate, locationType, addressLine,
                zipCode, city, state, borough, latitude, longitude);
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
