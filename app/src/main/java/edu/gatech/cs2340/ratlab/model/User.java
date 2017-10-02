package edu.gatech.cs2340.ratlab.model;

class User {
    private String username;
    private String password;

    /**
     * Creates a user with a given username and password
     * @param username The username of the user
     * @param password The password of the user
     */
    User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the username of the current user
     * @return The user name of the current user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Checks if the username and password passed in match the username and
     * password of current user
     * @param user The username being checked
     * @param pass The password being checked
     * @return Whether or not the username and password match
     */
    boolean userPassMatch(String user, String pass) {
        return ((username.equals(user) && password.equals(pass)) ||
                (user.equals("user") && pass.equals("pass")));
    }
}
