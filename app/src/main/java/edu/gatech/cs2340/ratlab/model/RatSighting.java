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

    RatSighting(String key, String createdDate, String locationType, String address, String zipCode,
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

    static RatSighting createRatSightingFromCsvLine(String csvLine) {
        String[] splitLine = csvLine.split(",");
        if (splitLine.length == 9) {
            // Expected length of line
            Borough borough = null;
            double latitude = 0;
            double longitude = 0;
            try {
                borough = Borough.valueOf(splitLine[6]);
                latitude = Double.parseDouble(splitLine[7]);
                longitude = Double.parseDouble(splitLine[8]);
            } catch (Exception e) {
                Log.d("historical_data", "Row with id " + splitLine[0] + " could not be parsed");
            }
            return new RatSighting(splitLine[0], splitLine[1], splitLine[2], splitLine[4],
                    splitLine[3], splitLine[5], borough, latitude, longitude);
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
}