package edu.gatech.cs2340.ratlab.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.gatech.cs2340.ratlab.R;

public class SightingsManager {
    private static final SightingsManager instance = new SightingsManager();
    public static SightingsManager getInstance() { return instance; }

    // The list of all rat sightings
    private Map<String,RatSighting> ratSightings;

    // Database variables for syncing the rat sighting list with firebase
    private DatabaseReference sightingsReference;
    private ChildEventListener sightingsListener;

    private boolean loadingHistoricalDataComplete;

    SightingsManager() {
        ratSightings = new HashMap<>();
        loadingHistoricalDataComplete = false;
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
                String locationTypeString = (String) dataSnapshot.child("locationType").getValue();
                LocationType locationType = LocationType.locationTypeFromTextName(locationTypeString);
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

        @Override
        protected void onPostExecute(Void result) {
            SightingsManager.this.loadingHistoricalDataComplete = true;
        }
    }

    public boolean isLoadingHistoricalDataComplete() {
        return loadingHistoricalDataComplete;
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
        newRat.put("createdDate", sighting.getCreatedDateDatabaseString());
        newRat.put("locationType", sighting.getLocationType());
        newRat.put("address", sighting.getAddress().getAddressLine());
        newRat.put("zipCode", sighting.getAddress().getZipCode());
        newRat.put("city", sighting.getAddress().getCity());
        newRat.put("borough", sighting.getBorough());
        newRat.put("latitude", sighting.getLocation().getLongitude());
        newRat.put("longitude", sighting.getLocation().getLatitude());
        newNode.setValue(newRat);
    }

    /** Converts the ratSightings HashMap into a list of sightings. The sightings are ordered
     * newest to oldest.
     *
     * @return a list  of the sightings
     */
    public List<RatSighting> createSightingsList() {
        List<RatSighting> sightingsList = new ArrayList<>(ratSightings.values());
        Collections.sort(sightingsList, new Comparator<RatSighting>() {
            @Override
            public int compare(RatSighting sighting1, RatSighting sighting2) {
                return sighting2.getCreatedDate().compareTo(sighting1.getCreatedDate());
            }
        });
        return sightingsList;
    }

    /** Gets the rat sighting with the given key.
     *
     * @param key a rat sighting key
     * @return the rat sighting with the given key
     */
    public RatSighting getSightingByKey(String key) {
        return ratSightings.get(key);
    }
}
