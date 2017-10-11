package edu.gatech.cs2340.ratlab.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import edu.gatech.cs2340.ratlab.R;
import edu.gatech.cs2340.ratlab.controllers.MainActivity;

public class Model {

    private static final Model instance = new Model();
    public static Model getInstance() { return instance; }

    // Stores information on the currently logged in user
    private User currentUser;

    // The list of all rat sightings
    private Map<String,RatSighting> ratSightings;

    //The currently selected rat sighting, defaults to first rat sighting
    private RatSighting currentSighting;

    // Database variables for syncing the rat sighting list with firebase
    private DatabaseReference sightingsReference;
    private ChildEventListener sightingsListener;

    Model() {
        ratSightings = new HashMap<>();
        sightingsReference = FirebaseDatabase.getInstance().getReference().child("sightings");

        sightingsListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                RatSighting sighting = createRatSighting(dataSnapshot);
                ratSightings.put(sighting.getKey(), sighting);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                RatSighting sighting = createRatSighting(dataSnapshot);
                ratSightings.put(sighting.getKey(), sighting);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ratSightings.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                RatSighting sighting = createRatSighting(dataSnapshot);
                ratSightings.put(sighting.getKey(), sighting);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("sighting_database", databaseError.toString());
            }

            private RatSighting createRatSighting(DataSnapshot dataSnapshot) {
                Log.d("sightings_database", "Starting to create RatSighting");
                String key = dataSnapshot.getKey();
                String createdDate = (String) dataSnapshot.child("createdDate").getValue();
                String locationType = (String) dataSnapshot.child("locationType").getValue();
                String address = (String) dataSnapshot.child("address").getValue();
                String zipCode = (String) dataSnapshot.child("zipCode").getValue();
                String city = (String) dataSnapshot.child("city").getValue();
                Borough borough = Borough.valueOf((String) dataSnapshot.child("borough").getValue());
                double latitude = (double) dataSnapshot.child("latitude").getValue();
                double longitude = (double) dataSnapshot.child("longitude").getValue();
                RatSighting sighting = new RatSighting(key, createdDate, locationType, address, zipCode, city,
                        borough, latitude, longitude);
                Log.d("sightings_database", "Added new Ratsighting from database to model: " + sighting);
                return sighting;

            }
        };
        sightingsReference.addChildEventListener(sightingsListener);
    }

    /** Asynchronously reads the historical data csv and adds the sightings to the model
     *
     * @param context the current activity
     */
    public void readHistoricalData(Context context) {
        new ReadHistoricalDataTask().execute(context);
    }

    private class ReadHistoricalDataTask extends AsyncTask<Context, Void, Void> {
        @Override
        protected Void doInBackground(Context... contexts) {
            Log.d("async_data", "Starting to read data");
            InputStream historicalDataStream = contexts[0].getResources().openRawResource(R.raw.rat_sightings);
            Scanner historicalDataScanner = new Scanner(historicalDataStream);
            historicalDataScanner.nextLine(); // Don't read the header

            RatSighting currentSighting;
            String nextLine;
            while (historicalDataScanner.hasNextLine()) {
                nextLine = historicalDataScanner.nextLine();
                currentSighting = RatSighting.createRatSightingFromCsvLine(nextLine);
                if (currentSighting != null) {
                    ratSightings.put(currentSighting.getKey(), currentSighting);
                }
            }
            Log.d("async_data", "Finished reading data. There are " + ratSightings.size()
                    + "sightings.");
            return (null);
        }
    }

    /** Adds a rat sighting to thd database. The sighting should not have a key, since the key is
     * generated when the sighting is added to the database. Note that the sighting is added as a
     * Map so that all fields update at the same time and the sightingsListener does not try to add
     * an incomplete sighting.
     *
     * @param sighting the sighting to add to the database
     */
    public void addRatSightingToDatabase(RatSighting sighting) {
        DatabaseReference newNode = sightingsReference.push();
        Map<String, Object> newRat = new HashMap<>();
        newRat.put("createdDate", sighting.getCreatedDate());
        newRat.put("locationType", sighting.getLocationType());
        newRat.put("address", sighting.getAddress());
        newRat.put("zipCode", sighting.getZipCode());
        newRat.put("city", sighting.getCity());
        newRat.put("borough", sighting.getBorough());
        newRat.put("latitude", sighting.getLongitude());
        newRat.put("longitude", sighting.getLatitude());
        newNode.setValue(newRat);
    }
    public List<RatSighting> createSightingsList() {
        List<RatSighting> sightingsList = new ArrayList<>(ratSightings.values());
        Collections.sort(sightingsList, new Comparator<RatSighting>() {
            @Override
            public int compare(RatSighting sighting1, RatSighting sighting2) {
                return sighting1.getCreatedDate().compareTo(sighting2.getCreatedDate());
            }
        });
        return sightingsList;
    }

    //I'm scared I messed up, bryan edits below hehe

    /**
     *
     * @return  the currently selected sighting
     */
    public RatSighting getCurrentSighting() { return currentSighting;}

    public void setCurrentSighting(RatSighting sighting) { currentSighting = sighting; }

}
