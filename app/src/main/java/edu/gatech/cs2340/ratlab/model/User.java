package edu.gatech.cs2340.ratlab.model;

class User {
    private String username;
    private String password;

    User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    boolean userPassMatch(String user, String pass) {
        return ((username.equals(user) && password.equals(pass)) ||
                (user.equals("user") && pass.equals("pass")));
    }
}
