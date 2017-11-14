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
import static junit.framework.Assert.fail;

/**
 * Tests filterRatSightings() method that does not have a max sightings variable
 * This is the main filterRatSightings() method that is used. These tests
 * achieve branch overage by fitting all the criteria if statement criteria
 * and then each subsequent test fails on one category for every test data.
 */

public class FilterDefaultTest {

    /**
     * Loads the rat sightings that will be necessary for the tests. Uses
     * wait since it is making async calls to fire base.
     */
    @Before
    public void loadSightings() {
        int count = 0;
        SightingsManager sightingsManager = SightingsManager.getInstance();
        while (sightingsManager.numSightings() < 26) {
            android.os.SystemClock.sleep(100);
            count++;
        }
    }
    private SightingsManager sightingsManager = SightingsManager.getInstance();

    private String format = "M/d/yyyy H:mm";
    private DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
    private Date startDate = null;
    private Date endDate = null;

    /**
     * Filters the rat data where 20 rat sightings should fit all the criteria
     */
    @Test
    public void testResultsThatMatchAllFilters() {
        try {
            startDate = dateFormat.parse("11/10/2017 0:00");
            endDate = dateFormat.parse("11/11/2017 0:00");
        } catch (ParseException e) {
            fail("Failed to parse date");
        }

        Collection<Borough> boroughs = Arrays.asList(Borough.values());
        Collection<LocationType> locationTypes = Arrays.asList(LocationType.values());
        Collection<RatSighting> sightings = sightingsManager
                .filterRatSightings(startDate, endDate, boroughs, locationTypes);
        assertEquals(20, sightings.size());
    }

    /**
     * Filters the data to look for sightings in 2018, none of which exist.
     */
    @Test
    public void testNothingInDateRange() {
        try {
            startDate = dateFormat.parse("11/10/2018 0:00");
            endDate = dateFormat.parse("11/11/2018 0:00");
        } catch (ParseException e) {
            fail("Failed to parse date");
        }

        Collection<Borough> boroughs = Arrays.asList(Borough.values());
        Collection<LocationType> locationTypes = Arrays.asList(LocationType.values());
        Collection<RatSighting> sightings = sightingsManager
                .filterRatSightings(startDate, endDate, boroughs, locationTypes);
        assertEquals(0, sightings.size());
    }

    /**
     * Tests data within the date range but in a borough where none fit
     * both of the criteria
     */
    @Test
    public void testNothingFitsDataAndBorough() {
        try {
            startDate = dateFormat.parse("11/10/2017 0:00");
            endDate = dateFormat.parse("11/11/2017 0:00");
        } catch (ParseException e) {
            fail("Failed to parse date");
        }

        Collection<Borough> boroughs = new HashSet<>();
        boroughs.add(Borough.QUEENS);
        Collection<LocationType> locationTypes = Arrays.asList(LocationType.values());
        Collection<RatSighting> sightings = sightingsManager
                .filterRatSightings(startDate, endDate, boroughs, locationTypes);
        assertEquals(0, sightings.size());
    }

    /**
     * Tests data within the date range but of a location type where none
     * of the data will match
     */
    @Test
    public void testNothingFitsDateAndLocationType() {
        try {
            startDate = dateFormat.parse("11/9/2017 0:00");
            endDate = dateFormat.parse("11/10/2017 0:00");
        } catch (ParseException e) {
            fail("Failed to parse date");
        }

        Collection<Borough> boroughs = Arrays.asList(Borough.values());
        Collection<LocationType> locationTypes = new HashSet<>();
        locationTypes.add(LocationType.COMMERCIAL_BUILDING);
        Collection<RatSighting> sightings = sightingsManager
                .filterRatSightings(startDate, endDate, boroughs, locationTypes);
        assertEquals(0, sightings.size());
    }

}