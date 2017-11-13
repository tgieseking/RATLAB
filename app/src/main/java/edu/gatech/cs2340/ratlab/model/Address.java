package edu.gatech.cs2340.ratlab.model;

import java.io.Serializable;

/**
 * Class that represents an address of where rat sighting occurs
 */
public class Address implements Serializable{
    private final String addressLine;
    private final String city;
    private final String state;
    private final String zipCode;

    /**
     * Getter method for the address line.
     *
     * @return street address of the address
     */
    public String getAddressLine() {
        return addressLine;
    }

    /**
     * Getter that returns the city.
     *
     * @return the city of the address
     */
    public String getCity() {
        return city;
    }

    /**
     * Getter that returns the state.
     *
     * @return the state of the address
     */
    public String getState() {
        return state;
    }

    /**
     * Getter that returns the zip code.
     *
     * @return the zip code of the address
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Constructor for the Address class.
     *
     * @param addressLine street address
     * @param city city of the address
     * @param state state of the address
     * @param zipCode zip code corresponding to the city
     */
    public Address(String addressLine, String city, String state, String zipCode) {
        this.addressLine = addressLine;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return addressLine + "\n"
                + city + ", "
                + state + " "
                + zipCode;
    }
}
