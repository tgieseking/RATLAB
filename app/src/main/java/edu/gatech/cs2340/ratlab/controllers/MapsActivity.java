package edu.gatech.cs2340.ratlab.controllers;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import edu.gatech.cs2340.ratlab.R;
import edu.gatech.cs2340.ratlab.model.Borough;
import edu.gatech.cs2340.ratlab.model.LocationType;
import edu.gatech.cs2340.ratlab.model.RatSighting;
import edu.gatech.cs2340.ratlab.model.SightingsManager;

/**
 * Activity that displays the map view of the rat sightings.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback ,
        ClusterManager.OnClusterItemInfoWindowClickListener<RatSighting>,
        GoogleMap.OnMapLongClickListener {

    private Collection<RatSighting> sightingsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent startIntent = getIntent();

        Log.d("parcel_test", "1");
        Collection<Borough> boroughs = new HashSet<>();
        if (startIntent.getBooleanExtra("bronx", false)) {
            boroughs.add(Borough.BRONX);
            Log.d("filters", "bronx");
        }
        if (startIntent.getBooleanExtra("brooklyn", false)) {
            boroughs.add(Borough.BROOKLYN);
            Log.d("filters", "brooklyn");
        }
        if (startIntent.getBooleanExtra("queens", false)) {
            boroughs.add(Borough.QUEENS);
            Log.d("filters", "queens");
        }
        if (startIntent.getBooleanExtra("manhattan", false)) {
            boroughs.add(Borough.MANHATTAN);
            Log.d("filters", "manhattan");
        }
        if (startIntent.getBooleanExtra("staten_island", false)) {
            boroughs.add(Borough.STATEN_ISLAND);
            Log.d("filters", "staten_island");
        }
        if (startIntent.getBooleanExtra("unknown_borough", false)) {
            boroughs.add(Borough.UNKNOWN);
            Log.d("filters", "unknown");
        }
        Log.d("parcel_test", "2");

        Date startDate = new Date(startIntent.getLongExtra("start_date", 0));
        Date endDate = new Date(startIntent.getLongExtra("end_date", 0));

        Log.d("parcel_test", "3");

        Collection<LocationType> locationTypes =
                new HashSet<>(Arrays.asList(LocationType.values()));

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
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);

        ClusterManager<RatSighting> clusterManager = new ClusterManager<>(this, googleMap);
        clusterManager.setAnimation(false);
        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);

        for(RatSighting sighting : sightingsList) {
            if (sighting.getLatitude() != 0) {
                clusterManager.addItem(sighting);
            }
        }

        final double newYorkLat = 40.730610;
        final double newYorkLng = -73.935242;

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(newYorkLat, newYorkLng),
                10));
        googleMap.setOnInfoWindowClickListener(clusterManager);
        clusterManager.setOnClusterItemInfoWindowClickListener(this);
        googleMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onClusterItemInfoWindowClick(RatSighting sighting) {
        String key = sighting.getKey();
        Intent intent = new Intent(this, SightingDetailActivity.class);
        intent.putExtra(SightingDetailFragment.ARG_SIGHTING_ID, key);
        startActivity(intent);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.d("map_click", "map long click");
        Geocoder geocoder = new Geocoder(this);
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(latLng.latitude,
                    latLng.longitude, 1);
            if (addresses.isEmpty()) {
                Log.d("map_click", "no matches");
            } else {
                android.location.Address firstAddress = addresses.get(0);
                Log.d("map_click", "" + firstAddress.getLatitude());
                Log.d("map_click", "" + firstAddress.getLongitude());
                Log.d("map_click", "" + firstAddress.getAddressLine(0));
                Log.d("map_click", "" + firstAddress.getSubThoroughfare());
                Log.d("map_click", "" + firstAddress.getThoroughfare());
                Intent intent = new Intent(this, ReportSightingActivity.class);
                intent.putExtra("address", firstAddress);
                startActivity(intent);
            }
        } catch (IOException e) {
            Log.e("map_click", "was an IO error when clicking on map", e);
        }

    }
}
