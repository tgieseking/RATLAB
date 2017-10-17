package edu.gatech.cs2340.ratlab.model;

import java.io.Serializable;

public class Location implements Serializable{
    private double latitude;
    private double longitude;
    private Address address;
    private Borough borough;
    private LocationType locationType;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Address getAddress() {
        return address;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public Borough getBorough() {
        return borough;
    }

    public Location(double latitude, double longitude, Address address, Borough borough,
                    LocationType locationType) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.borough = borough;
        this.locationType = locationType;
    }

    /**
     * Sets borough to default value of null.
     */
    public Location(double lattitude, double longitude, Address address, LocationType locationType) {
        this(lattitude, longitude, address, null, locationType);
    }
}
