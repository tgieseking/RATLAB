package edu.gatech.cs2340.ratlab.controllers;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import edu.gatech.cs2340.ratlab.R;
import edu.gatech.cs2340.ratlab.model.Borough;
import edu.gatech.cs2340.ratlab.model.LocationType;
import edu.gatech.cs2340.ratlab.model.RatSighting;
import edu.gatech.cs2340.ratlab.model.SightingsManager;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private Set<RatSighting> sightingsList;
    private ClusterManager<RatSighting> clusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent startIntent = getIntent();

        Log.d("parcel_test", "1");
        Set<Borough> boroughs = new HashSet<>();
        if (startIntent.getBooleanExtra("bronx", false)) {
            boroughs.add(Borough.BRONX);
        }
        if (startIntent.getBooleanExtra("brooklyn", false)) {
            boroughs.add(Borough.BROOKLYN);
        }
        if (startIntent.getBooleanExtra("queens", false)) {
            boroughs.add(Borough.QUEENS);
        }
        if (startIntent.getBooleanExtra("manhattan", false)) {
            boroughs.add(Borough.MANHATTAN);
        }
        if (startIntent.getBooleanExtra("staten_island", false)) {
            boroughs.add(Borough.STATEN_ISLAND);
        }
        Log.d("parcel_test", "2");

        Date startDate = new Date(startIntent.getLongExtra("start_date", 0));
        Date endDate = new Date(startIntent.getLongExtra("end_date", 0));

        Log.d("parcel_test", "3");

        Set<LocationType> locationTypes = new HashSet<>(Arrays.asList(LocationType.values()));

        sightingsList = SightingsManager.getInstance()
                .filterRatSightings(startDate, endDate, boroughs, locationTypes, 1000);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     *
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);

        clusterManager = new ClusterManager<RatSighting>(this, map);
        clusterManager.setAnimation(false);
        map.setOnCameraIdleListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);

        for(RatSighting sighting : sightingsList) {
            if (sighting.getLocation().getLatitude() != 0) {
                clusterManager.addItem(sighting);
            }
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.730610, -73.935242), 10));

    }
}
