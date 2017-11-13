package edu.gatech.cs2340.ratlab.model;

import java.io.Serializable;

/**
 * Enumerated type representing the boroughs of the city.
 */
public enum Borough implements Serializable{
    BRONX ("Bronx"),
    BROOKLYN ("Brooklyn"),
    MANHATTAN ("Manhattan"),
    QUEENS ("Queens"),
    STATEN_ISLAND ("Staten Island"),
    UNKNOWN ("Unknown");

    // The borough name as it should be printed in text
    private final String textName;

    Borough(String textName) {
        this.textName = textName;
    }

    public String toString() {
        return textName;
    }

    /**
     * Getter that gets the borough enum from its text name.
     * @param textName the borough name
     * @return the enumerated borough type
     */
    public static Borough getBoroughFromTextName(String textName) {
        switch (textName) {
            case "Bronx" : return BRONX;
            case "Brooklyn" : return BROOKLYN;
            case "Manhattan" : return MANHATTAN;
            case "Queens" : return QUEENS;
            case "Staten Island" : return STATEN_ISLAND;
            default : return UNKNOWN;
        }
    }
}
