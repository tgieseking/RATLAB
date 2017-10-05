package edu.gatech.cs2340.ratlab.model;

class User {
    private String email;
    private String username;
    private String name;

    /**
     * Creates a user with a given username and password
     * @param email The email of the user
     * @param username The username of the user
     * @param name The name of the user
     */
    User(String email, String username, String name) {
        this.email = email;
        this.username = username;
        this.name = name;
    }

    /**
     * Returns the username of the current user
     * @return The user name of the current user.
     */
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
}
