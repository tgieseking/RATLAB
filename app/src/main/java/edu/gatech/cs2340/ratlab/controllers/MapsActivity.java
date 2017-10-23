package edu.gatech.cs2340.ratlab.controllers;

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


        Set<Borough> boroughs = new HashSet<>(Arrays.asList(Borough.values()));
        Set<LocationType> locationTypes = new HashSet<>(Arrays.asList(LocationType.values()));
        String format = "M/d/yyyy H:mm";
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date startDate = new Date();
        Date endDate = new Date();
        try {
            startDate = dateFormat.parse("8/23/2016 0:00");
            endDate = dateFormat.parse("8/25/2017 0:00");
        } catch (Exception e) {
            Log.e("filter_test", "parse error", e);
        }

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
