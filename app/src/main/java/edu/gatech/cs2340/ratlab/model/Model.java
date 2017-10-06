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
                String key = dataSnapshot.getKey();
                String createdDate = (String) dataSnapshot.child("createdDate").getValue();
                String locationType = (String) dataSnapshot.child("locationType").getValue();
                String address = (String) dataSnapshot.child("address").getValue();
                String zipCode = (String) dataSnapshot.child("zipCode").getValue();
                String city = (String) dataSnapshot.child("city").getValue();
                Borough borough = Borough.valueOf((String) dataSnapshot.child("borough").getValue());
                double latitude = (double) dataSnapshot.child("latitude").getValue();
                double longitude = (double) dataSnapshot.child("longitude").getValue();
                return new RatSighting(key, createdDate, locationType, address, zipCode, city,
                        borough, latitude, longitude);
            }
        };
        sightingsReference.addChildEventListener(sightingsListener);
    }

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
}
