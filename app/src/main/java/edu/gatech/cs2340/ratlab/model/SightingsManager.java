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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.gatech.cs2340.ratlab.R;

public final class SightingsManager {
    private static final SightingsManager instance = new SightingsManager();
    public static SightingsManager getInstance() { return instance; }

    // The list of all rat sightings
    private final Map<String,RatSighting> ratSightings;

    // Database variables for syncing the rat sighting list with firebase
    private final DatabaseReference sightingsReference;

    private boolean loadingHistoricalDataComplete;

    private SightingsManager() {
        ratSightings = new HashMap<>();
        loadingHistoricalDataComplete = false;
        sightingsReference = FirebaseDatabase.getInstance().getReference().child("sightings");

        ChildEventListener sightingsListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                try {
                    RatSighting sighting = createRatSighting(dataSnapshot);
                    ratSightings.put(sighting.getKey(), sighting);
                } catch (Exception e) {
                    Log.e("sightings_database", "Sighting add failed", e);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                try {
                    RatSighting sighting = createRatSighting(dataSnapshot);
                    ratSightings.put(sighting.getKey(), sighting);
                } catch (Exception e) {
                    Log.e("sightings_database", "Sighting change failed", e);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                try {
                    ratSightings.remove(dataSnapshot.getKey());
                } catch (Exception e) {
                    Log.e("sightings_database", "Sighting removal failed", e);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                try {
                    RatSighting sighting = createRatSighting(dataSnapshot);
                    ratSightings.put(sighting.getKey(), sighting);
                } catch (Exception e) {
                    Log.e("sightings_database", "Sighting move failed", e);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("sighting_database", databaseError.toString());
            }

            private RatSighting createRatSighting(DataSnapshot dataSnapshot) {
                Log.d("sightings_database", "Starting to create RatSighting");
                String key = dataSnapshot.getKey();
                String createdDate = dataSnapshot.child("createdDate").getValue(String.class);
                String locationTypeString = dataSnapshot.child("locationType").getValue(String.class);
                LocationType locationType = LocationType.locationTypeFromTextName(locationTypeString);
                String address = dataSnapshot.child("address").getValue(String.class);
                String zipCode = dataSnapshot.child("zipCode").getValue(String.class);
                String city = dataSnapshot.child("city").getValue(String.class);
                String boroughString = dataSnapshot.child("borough").getValue(String.class);
                Borough borough = Borough.valueOf(boroughString);
                double latitude;
                double longitude;
                latitude = dataSnapshot.child("latitude").getValue(Double.class);
                longitude = dataSnapshot.child("longitude").getValue(Double.class);
                RatSighting sighting = new RatSighting(key, createdDate, locationType, address, zipCode, city,
                        borough, latitude, longitude);
                Log.d("sightings_database", "Added new rat sighting from database to model: " + sighting);
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

    /** Adds a rat sighting to the database. The sighting should not have a key, since the key is
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
        newRat.put("state", sighting.getLocation().getAddress().getState());
        newRat.put("borough", sighting.getBorough());
        newRat.put("latitude", sighting.getLocation().getLatitude());
        newRat.put("longitude", sighting.getLocation().getLongitude());
        newNode.setValue(newRat);
    }

    /** Converts the ratSightings HashMap into a list of sightings. The sightings are ordered
     * newest to oldest. The historical data needs to be loaded before this method is called.
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

    /**
     * Gets all rat sightings filtered by a date range, borough, and location type. The historical
     * data needs to be loaded before this method is called.
     *
     * @param startDate the start of the date range
     * @param endDate tne end of the date range
     * @param boroughs the list of boroughs which will be included
     * @param locationTypes the list of location types which will be included
     * @return a set of rat sightings which meet all criteria
     */
    public Collection<RatSighting> filterRatSightings(Date startDate, Date endDate, Collection<Borough> boroughs,
                                                      Collection<LocationType> locationTypes) {
        Collection<RatSighting> filteredSightings = new HashSet<>();
        for (RatSighting sighting : ratSightings.values()) {
            if ((sighting.getCreatedDate().compareTo(startDate) >= 0)
                    && (sighting.getCreatedDate().compareTo(endDate) <= 0)
                    && (boroughs.contains(sighting.getBorough()))
                    && (locationTypes.contains(sighting.getLocationType()))) {
                filteredSightings.add(sighting);
            }
        }
        return filteredSightings;
    }

    /**
     * Gets a set of rat sightings filtered by a date range, borough, and location type. If the
     * total number of sightings that meet all criteria is at most maxSightings, all sightings are
     * included. Otherwise, a random subset of size maxSightings is returned. The historical data
     * needs to be loaded before this method is called.
     *
     * @param startDate the start of the date range
     * @param endDate tne end of the date range
     * @param boroughs the list of boroughs which will be included
     * @param maxSightings the maximum size of the returned set
     * @return a set of rat sightings which meet all criteria of size at most maxSightings
     */
    public Collection<RatSighting> filterRatSightings(Date startDate, Date endDate, Collection<Borough> boroughs,
                                               Collection<LocationType> locationTypes, int maxSightings) {
        Collection<RatSighting> filteredSightings = filterRatSightings(startDate, endDate, boroughs,
                locationTypes);
        if (filteredSightings.size() <= maxSightings) {
            return filteredSightings;
        } else {
            List<RatSighting> filteredSightingsList = new LinkedList<>(filteredSightings);
            Collections.shuffle(filteredSightingsList);
            return new HashSet<>(filteredSightingsList.subList(0, maxSightings));
        }
    }

    public int numSightings() {
        return ratSightings.size();
    }
}
