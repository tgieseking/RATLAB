package edu.gatech.cs2340.ratlab.model;

/**
 * Created by Timothy on 10/11/2017.
 */

public enum AccountType {
    USER,
    ADMINISTRATOR;

    public static AccountType accountTypeFromString(String accountTypeString) {
        switch (accountTypeString) {
            case "User" : return AccountType.USER;
            case "Administrator" : return AccountType.ADMINISTRATOR;
            default : return null;
        }
    }
}
