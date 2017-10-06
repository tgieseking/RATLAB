package edu.gatech.cs2340.ratlab.model;

public enum Borough {
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
}
