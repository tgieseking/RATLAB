package edu.gatech.cs2340.ratlab.model;

import java.io.Serializable;

/**
 * Location class that represents a location of rat sighting.
 */
public class Location implements Serializable{
    private final double latitude;
    private final double longitude;
    private final Address address;
    private final Borough borough;
    private final LocationType locationType;

    /**
     * Getter method for location's latitude.
     *
     * @return double representing the latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Getter method for location's longitude.
     *
     * @return double representing the longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Getter method for location's address.
     *
     * @return Address representing the address.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Getter method for location's location type.
     *
     * @return LocationType of the location
     */
    public LocationType getLocationType() {
        return locationType;
    }

    /**
     * Getter method for location's borough.
     *
     * @return Borough enum of the location's borough.
     */
    public Borough getBorough() {
        return borough;
    }
    /**
     * Getter method for location's address line.
     *
     * @return String address line
     */
    public String getAddressLine() {
        return address.getAddressLine();
    }
    /**
     * Getter method for location's city
     *
     * @return String city
     */
    public String getCity() {
        return address.getCity();
    }
    /**
     * Getter method for location's state
     *
     * @return String state
     */
    public String getState() {
        return address.getState();
    }
    /**
     * Getter method for location's zip code.
     *
     * @return String zip code
     */
    public String getZipCode() {
        return address.getZipCode();
    }
    /**
     * Constructor for the location.
     *
     * @param latitude latitude of the location
     * @param longitude longitude of the location
     * @param address address of the location
     * @param borough borough of the location
     * @param locationType location type of the location
     */
    public Location(double latitude, double longitude, Address address, Borough borough,
                    LocationType locationType) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.borough = borough;
        this.locationType = locationType;
    }
}
