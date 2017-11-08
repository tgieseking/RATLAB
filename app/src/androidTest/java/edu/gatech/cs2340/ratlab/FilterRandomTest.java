package edu.gatech.cs2340.ratlab;

import android.util.Log;

import org.junit.Test;

import edu.gatech.cs2340.ratlab.model.SightingsManager;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Timothy on 11/8/2017.
 */

public class FilterRandomTest {
    @Test
    public void initialTest() {
        SightingsManager sightingsManager = SightingsManager.getInstance();
        while(!sightingsManager.isLoadingHistoricalDataComplete()) {
            Log.d("filterTest","waiting: " + sightingsManager.numSightings());
            android.os.SystemClock.sleep(1000);
        }
        assertTrue(sightingsManager.isLoadingHistoricalDataComplete());
    }
}
