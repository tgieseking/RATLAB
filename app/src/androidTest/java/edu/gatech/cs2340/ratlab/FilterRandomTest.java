package edu.gatech.cs2340.ratlab;

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

/**
 * Testing of the FilterRatSightings method in SightingsManager with the maxSightings parameter
 */
public class FilterRandomTest {
    /**
     * Loads the rat sightings from firebase before tests are run. If the sightings take more than
     * 5000ms to load, the test fails because of suspected connection issue.
     */
    @Before
    public void loadSightings() {
        final int totalSightings = 26;
        final int timeoutCycles = 50;
        int count = 0;

        SightingsManager sightingsManager = SightingsManager.getInstance();
        while (sightingsManager.numSightings() < totalSightings) {
            android.os.SystemClock.sleep(100);
            count++;
            assertTrue("Sightings failed to load", count < timeoutCycles);
        }
    }

    /**
     * Tests whether boroughs are included/excluded from the filtered list correctly based on the
     * boroughs list.
     */
    @Test
    public void testBoroughs() {
        SightingsManager sightingsManager = SightingsManager.getInstance();

        String format = "M/d/yyyy H:mm";
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = dateFormat.parse("11/9/2017 0:00");
            endDate = dateFormat.parse("11/10/2017 0:00");
        } catch (ParseException e) {
            fail("Failed to parse date");
        }

        Collection<Borough> boroughs = new HashSet<>();
        Collection<LocationType> locationTypes = Arrays.asList(LocationType.values());

        Collection<RatSighting> sightings;

        for (Borough borough : Borough.values()) {
            boroughs.add(borough);
            sightings = sightingsManager.filterRatSightings(startDate, endDate, boroughs,
                    locationTypes, 10);
            assertEquals("There were " + sightings.size() + " sightings with borough " + borough,
                    sightings.size(), 1);
            for (RatSighting sighting : sightings) {
                assertEquals(sighting.getBorough(), borough);
            }
            boroughs.clear();
        }
    }

    /**
     * Tests whether location types are included/excluded from the filtered list correctly based on
     * the location types list.
     */
    @Test
    public void testLocationTypes() {
        SightingsManager sightingsManager = SightingsManager.getInstance();

        String format = "M/d/yyyy H:mm";
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = dateFormat.parse("11/10/2017 0:00");
            endDate = dateFormat.parse("11/11/2017 0:00");
        } catch (ParseException e) {
            fail("Failed to parse date");
        }

        Collection<Borough> boroughs = Arrays.asList(Borough.values());
        Collection<LocationType> locationTypes = new HashSet<>();

        Collection<RatSighting> sightings;

        for (LocationType locationType : LocationType.values()) {
            locationTypes.add(locationType);
            sightings = sightingsManager.filterRatSightings(startDate, endDate, boroughs,
                    locationTypes, 10);
            assertEquals("There were " + locationTypes.size() + " sightings with location type "
                            + locationType, locationTypes.size(), 1);
            for (RatSighting sighting : sightings) {
                assertEquals(sighting.getLocationType(), locationType);
            }
            locationTypes.clear();
        }
    }

    /**
     * Tests whether a subset of the correct size is taken from the filtered list and whether the
     * subset is the same every time.
     */
    @Test
    public void testSubset() {
        SightingsManager sightingsManager = SightingsManager.getInstance();

        String format = "M/d/yyyy H:mm";
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = dateFormat.parse("11/10/2017 0:00");
            endDate = dateFormat.parse("11/11/2017 0:00");
        } catch (ParseException e) {
            fail("Failed to parse date");
        }

        Collection<Borough> boroughs = Arrays.asList(Borough.values());
        Collection<LocationType> locationTypes = Arrays.asList(LocationType.values());

        final int maxSightings = 5;
        Collection<RatSighting> sightings;
        Collection<RatSighting> previousSightings = sightingsManager.filterRatSightings(startDate,
                endDate, boroughs, locationTypes, maxSightings);


        boolean allSame = true;

        for (int i = 0; i < 10; i++) {
            sightings = sightingsManager.filterRatSightings(startDate, endDate, boroughs,
                    locationTypes, maxSightings);
            assertEquals("Sampled subset wrong size", sightings.size(), maxSightings);
            allSame = allSame && sightings.equals(previousSightings);
            previousSightings = sightings;
        }
        assertFalse("All the sampled subsets were the same. This is possible, but happens with" +
                " probability less than 10^-37 if subset chosen is truly random.", allSame);
    }
}
