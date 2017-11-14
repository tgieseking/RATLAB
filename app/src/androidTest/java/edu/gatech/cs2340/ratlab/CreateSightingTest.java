package edu.gatech.cs2340.ratlab;

import org.junit.Test;

import edu.gatech.cs2340.ratlab.model.Borough;
import edu.gatech.cs2340.ratlab.model.RatSighting;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;


/**
 * Testing of the createRatSightingFromCsvLine method in the RatSighting class
 */
public class CreateSightingTest {

    private RatSighting sighting;

    /**
     * Tests whether an empty line is caught
     */
    @Test
    public void testLengthZero() {
        try {
            sighting = RatSighting.createRatSightingFromCsvLine("");
        } catch (Exception e) {
            fail("Failed to account for empty line.");
        }
        assertEquals(sighting, null);
    }

    /**
     * Tests the functionality of a normal CSV line input
     */
    @Test
    public void testNormalCsvLine() {
        try {
            sighting = RatSighting.createRatSightingFromCsvLine("31464024,9/4/2015 0:00," +
                    "Commercial Building,10306,2270 HYLAN BOULEVARD,STATEN ISLAND,STATEN_ISLAND," +
                    "40.57520924,-74.10454652");
        } catch (Exception e) {
            fail("Improper processing of information.");
        }
        final double latitude = 40.57520924;
        final double longitude = -74.10454652;
        assertEquals(sighting.getAddressLine(), "2270 HYLAN BOULEVARD");
        assertEquals(sighting.getLatitude(), latitude);
        assertEquals(sighting.getLongitude(), longitude);
        assertEquals(sighting.getTitle(), "31464024");
        assertEquals(sighting.getCreatedDateString(), "9/4/15");
        assertEquals(sighting.getBorough(), Borough.STATEN_ISLAND);

    }

    /**
     * A test to check for a missing borough attribute
     */
    @Test
    public void testMissingBorough() {
        try {
            sighting = RatSighting.createRatSightingFromCsvLine("31464024,9/4/2015 0:00," +
                    "Commercial Building,10306,2270 HYLAN BOULEVARD,STATEN ISLAND,," +
                    "40.57520924,-74.10454652");
        } catch (Exception e) {
            fail("Improper processing of information.");
        }
        final double latitude = 40.57520924;
        final double longitude = -74.10454652;
        assertEquals(sighting.getAddressLine(), "2270 HYLAN BOULEVARD");
        assertEquals(sighting.getLatitude(), latitude);
        assertEquals(sighting.getLongitude(), longitude);
        assertEquals(sighting.getTitle(), "31464024");
        assertEquals(sighting.getCreatedDateString(), "9/4/15");
        assertEquals(sighting.getBorough(), Borough.UNKNOWN);
    }

    /**
     * Tests the functionality of a CSV line missing the location attribute
     */
    @Test
    public void testStrangeCsvLine() {
        try {
            sighting = RatSighting.createRatSightingFromCsvLine("\n" +
                    "31464024,9/4/2015 0:00,Commercial Building,10306,2270 HYLAN BOULEVARD," +
                    "STATEN ISLAND,STATEN_ISLAND,40.57520924,-74.10454652");
        } catch (Exception e) {
            fail("Improper processing of information.");
        }
        assertEquals(sighting, sighting);
    }
}
