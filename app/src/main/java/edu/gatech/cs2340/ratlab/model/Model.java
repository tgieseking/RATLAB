package edu.gatech.cs2340.ratlab.model;

public class Model {

    private static final Model instance = new Model();
    public static Model getInstance() { return instance; }

    boolean loggedIn;

    //I didn't know where to put the dummy user but I guess here works for now
    User dummy = new User("user", "pass");

    /**
     * Tries to login a user with a given username and password
     * @param user The username being passed in to check
     * @param pass The password being passed in to check
     * @return Whether or not the user was logged in
     */
    public boolean loginAttempt(String user, String pass) {
        if (dummy.userPassMatch(user, pass)) {
            loggedIn = true;
            return true;
        }
        return false;
    }

    /**
     * Sets the user to logged out
     */
    public void logout() {
        loggedIn = false;
    }
}
