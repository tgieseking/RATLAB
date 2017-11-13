package edu.gatech.cs2340.ratlab.model;

import java.io.Serializable;


/**
 * Enumerated type that represents location type.
 */
public enum LocationType implements Serializable{
    THREE_PLUS_FAMILY_MIXED_USE_BUILDING("3+ Family Mixed Use Building"),
    COMMERCIAL_BUILDING("Commercial Building"),
    ONE_TO_TWO_FAMILY_DWELLING("1-2 Family Dwelling"),
    THREE_PLUS_FAMILY_APT_BUILDING("3+ Family Apt. Building"),
    PUBLIC_STAIRS("Public Stairs"),
    VACANT_LOT("Vacant Lot"),
    CONSTRUCTION_SITE("Construction Site"),
    HOSPITAL("Hospital"),
    PARKING_LOT_GARAGE("Parking Lot/Garage"),
    CATCH_BASIN_SEWER("Catch Basin/Sewer"),
    VACANT_BUILDING("Vacant Building"),
    ONE_TO_TWO_FAMILY_MIXED_USE_BUILDING("1-2 Family Mixed Use Building"),
    PUBLIC_GARDEN("Public Garden"),
    GOVERNMENT_BUILDING("Government Building"),
    OFFICE_BUILDING("Office Building"),
    SCHOOL_PRESCHOOL("School/Pre-School"),
    DAY_CARE_NURSERY("Day Care/Nursery"),
    SINGLE_ROOM_OCCUPANCY("Single Room Occupancy (SRO)"),
    SUMMER_CAMP("Summer Camp"),
    OTHER("Other");

    private final String textName;

    /**
     * Getter method that returns the text name of the location type.
     *
     * @return string representation of the location type
     */
    public String getTextName() {
        return textName;
    }

    LocationType(String textName) {
        this.textName = textName;
    }

    /**
     * Creates the LocationType object with the given textName or LocationType.OTHER if invalid.
     * @param textName the text name of a LocationType object
     * @return the corresponding LocationType object
     */
    public static LocationType locationTypeFromTextName(String textName) {
        for (LocationType locationType : LocationType.values()) {
            if (locationType.getTextName().equals(textName)) {
                return locationType;
            }
        }
        return LocationType.OTHER;
    }

    @Override
    public String toString() {
        return textName;
    }
}
