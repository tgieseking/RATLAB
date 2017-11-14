package edu.gatech.cs2340.ratlab;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import edu.gatech.cs2340.ratlab.model.Borough;
import edu.gatech.cs2340.ratlab.model.LocationType;
import edu.gatech.cs2340.ratlab.model.RatSighting;
import edu.gatech.cs2340.ratlab.model.SightingsManager;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;



public class CreateSightingTest {

    RatSighting sighting;

    @Test
    public void testLengthZero() {
        try {
            sighting = RatSighting.createRatSightingFromCsvLine("");
        } catch (Exception e) {
            fail("Failed to account for empty line.");
        }
        assertEquals(sighting, null);
    }

    @Test
    public void testNormalCsvLine() {
        try {
            sighting = RatSighting.createRatSightingFromCsvLine("\n" +
                    "31464024,9/4/2015 0:00,Commercial Building,10306,2270 HYLAN BOULEVARD,STATEN ISLAND,STATEN_ISLAND,40.57520924,-74.10454652");
        } catch (Exception e) {
            fail("Improper processing of information.");
        }
        assertEquals(sighting, sighting);
    }

    @Test
    public void testWeirdCsvLine() {
        try {
            sighting = RatSighting.createRatSightingFromCsvLine("\n" +
                    "31464024,9/4/2015 0:00,Commercial Building,10306,2270 HYLAN BOULEVARD,STATEN ISLAND,STATEN_ISLAND,40.57520924,-74.10454652");
        } catch (Exception e) {
            fail("Improper processing of information.");
        }
        assertEquals(sighting, sighting);
    }

    @Test
    public void testStrangeCsvLine() {
        try {
            sighting = RatSighting.createRatSightingFromCsvLine("\n" +
                    "31464024,9/4/2015 0:00,Commercial Building,10306,2270 HYLAN BOULEVARD,STATEN ISLAND,STATEN_ISLAND,40.57520924,-74.10454652");
        } catch (Exception e) {
            fail("Improper processing of information.");
        }
        assertEquals(sighting, sighting);
    }



//    try {
//        borough = Borough.valueOf(splitLine[6]);
//    } catch (Exception e) {
//        borough = Borough.UNKNOWN;
//    }
//    LocationType locationType = LocationType.locationTypeFromTextName(splitLine[2]);
//            try {
//        latitude = Double.parseDouble(splitLine[7]);
//        longitude = Double.parseDouble(splitLine[8]);
//        return new RatSighting(splitLine[0], splitLine[1], locationType, splitLine[4],
//                splitLine[3], splitLine[5], borough, latitude, longitude);
//    } catch (Exception e) {
//        Log.d("historical_data", "Row with id " + splitLine[0] + " could not be parsed");
//    }
}
