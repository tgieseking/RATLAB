package edu.gatech.cs2340.ratlab.model;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RatSighting implements ClusterItem {
    private String key;
    private Date createdDate;  // This maybe should be a java.util.Date
    private Location location;

    public String getKey() {
        return key;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * Gets the created date in a printable string format
     * @return the date as a string
     */
    public String getCreatedDateString() {
        String format = "M/d/yy";
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        return dateFormat.format(createdDate);
    }

    /**
     * Gets the created date in the format it will be stored in the database
     * @return the date as a string
     */
    public String getCreatedDateDatabaseString() {
        String format = "M/d/yyyy H:mm";
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        return dateFormat.format(createdDate);
    }

    public LocationType getLocationType() {
        return location.getLocationType();
    }

    public Address getAddress() {
        return location.getAddress();
    }

    public Location getLocation() {
        return location;
    }

    public Borough getBorough() {
        return location.getBorough();
    }

    public RatSighting(String key, Date createdDate, LocationType locationType, String addressLine,
                       String zipCode, String city, String state, Borough borough, double latitude,
                       double longitude) {
        this.key = key;
        this.createdDate = createdDate;
        Address address = new Address(addressLine, city, state, zipCode);
        this.location = new Location(latitude, longitude, address, borough, locationType);
    }

    public RatSighting(String key, Date createdDate, LocationType locationType, String addressLine,
                       String zipCode, String city, Borough borough, double latitude,
                       double longitude) {
        this(key, createdDate, locationType, addressLine, zipCode, city, "NY", borough, latitude,
                longitude);
    }

    public RatSighting(String key, String createdDateString, LocationType locationType,
                       String address, String zipCode, String city, Borough borough,
                       double latitude, double longitude) {
        this(key, new Date(), locationType, address, zipCode, city, borough, latitude, longitude);
        String format = "M/d/yyyy H:mm";
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        try {
            this.createdDate = dateFormat.parse(createdDateString);
        } catch (Exception e) {
            Log.d("DateFormat", "Could not parse date " + createdDateString);
        }
    }

    /** Reads data in the format of a line of the historical data csv and creates the corresponding
     * rat sighting.
     *
     * @param csvLine a line of the historical data csv
     * @return a new rat sighting corresponding to the data in the csv line
     */
    static RatSighting createRatSightingFromCsvLine(String csvLine) {
        String[] splitLine = csvLine.split(",");
        if (splitLine.length == 9) {
            // Expected length of line
            Borough borough = null;
            double latitude = 0;
            double longitude = 0;
            try {
                borough = Borough.valueOf(splitLine[6]);
            } catch (Exception e) {
                borough = Borough.UNKNOWN;
            }
            LocationType locationType = LocationType.locationTypeFromTextName(splitLine[2]);
            try {
                latitude = Double.parseDouble(splitLine[7]);
                longitude = Double.parseDouble(splitLine[8]);
                return new RatSighting(splitLine[0], splitLine[1], locationType, splitLine[4],
                        splitLine[3], splitLine[5], borough, latitude, longitude);
            } catch (Exception e) {
                Log.d("historical_data", "Row with id " + splitLine[0] + " could not be parsed");
            }
        } else if (splitLine.length > 0){
            Log.d("historical_data", "Row with id " + splitLine[0] + " has too few rows");
        } else {
            Log.d("historical_data", "Encountered empty row");
        }
        return null;
    }

    /** Returns a hash code based on the key attribute
     *
     * @return the hash code
     */
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(getLocation().getLatitude(), getLocation().getLongitude());
    }

    @Override
    public String getTitle() {
        return key;
    }

    @Override
    public String getSnippet() {
        return getCreatedDateString();
    }


}
