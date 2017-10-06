package edu.gatech.cs2340.ratlab.model;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import edu.gatech.cs2340.ratlab.R;

public class Model {

    private static final Model instance = new Model();
    public static Model getInstance() { return instance; }

    // Stores information on the currently logged in user
    private User currentUser;

    // The list of all rat sightings
    private Set<RatSighting> ratSightings;

    Model() {
        ratSightings = new HashSet<>();
    }

    public void readHistoricalData(Context context) {
        InputStream historicalDataStream = context.getResources().openRawResource(R.raw.rat_sightings);
        Scanner historicalDataScanner = new Scanner(historicalDataStream);
        historicalDataScanner.nextLine(); // Don't read the header

        RatSighting currentSighting;
        String nextLine;
        while (historicalDataScanner.hasNextLine()) {
            nextLine = historicalDataScanner.nextLine();
            currentSighting = RatSighting.createRatSightingFromCsvLine(nextLine);
            if (currentSighting != null) {
                ratSightings.add(currentSighting);
            }
        }
    }
}
