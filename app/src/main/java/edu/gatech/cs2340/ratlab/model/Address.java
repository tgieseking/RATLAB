package edu.gatech.cs2340.ratlab.model;

import java.io.Serializable;

public class Address implements Serializable{
    private String addressLine;
    private String city;
    private String state;
    private String zipCode;

    public String getAddressLine() {
        return addressLine;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

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
