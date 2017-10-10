package edu.gatech.cs2340.ratlab.model;

import android.util.Log;

import java.util.Date;

public class RatSighting {
    private String key;
    private String createdDate;  // This maybe should be a java.util.Date
    private String locationType;
    private String address;
    private String zipCode;
    private String city;
    private Borough borough;
    private double latitude;
    private double longitude;

    public String getKey() {
        return key;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getLocationType() {
        return locationType;
    }

    public String getAddress() {
        return address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public Borough getBorough() {
        return borough;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public RatSighting(String key, String createdDate, String locationType, String address, String zipCode,
                String city, Borough borough, double latitude, double longitude) {
        this.key = key;
        this.createdDate = createdDate;
        this.locationType = locationType;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
        this.borough = borough;
        this.latitude = latitude;
        this.longitude = longitude;
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
            try {
                latitude = Double.parseDouble(splitLine[7]);
                longitude = Double.parseDouble(splitLine[8]);
                return new RatSighting(splitLine[0], splitLine[1], splitLine[2], splitLine[4],
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

    public String toString() {
        return key + createdDate + locationType + address + zipCode + city + borough + latitude + longitude;
    }

    /** Returns a hash code based on the key attribute
     *
     * @return the hash code
     */
    public int hashCode() {
        return key.hashCode();
    }
}
