package edu.gatech.cs2340.ratlab.model;

import java.util.List;

public class Model {

    private static final Model instance = new Model();
    public static Model getInstance() { return instance; }

    // Stores information on the currently logged in user
    private User currentUser;

    private List<RatSighting> ratSightings;
}
