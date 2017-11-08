package edu.gatech.cs2340.ratlab.model;

class User {
    private final String email;
    private final String username;
    private final String name;
    private final AccountType accountType;

    /**
     * Creates a user with a given username and password
     * @param email The email of the user
     * @param username The username of the user
     * @param name The name of the user
     */
    User(String email, String username, String name, AccountType accountType) {
        this.email = email;
        this.username = username;
        this.name = name;
        this.accountType = accountType;
    }

// --Commented out by Inspection START (11/8/2017 3:56 PM):
// --Commented out by Inspection START (11/8/2017 3:56 PM):
// --Commented out by Inspection START (11/8/2017 3:56 PM):
//////    /**
//////     * Returns the username of the current user
//////     * @return The user name of the current user.
//////     */
//////    public String getUsername() {
//////        return username;
//////    }
////// --Commented out by Inspection STOP (11/8/2017 3:56 PM)
////    public String getEmail() {
////        return email;
////    }
//// --Commented out by Inspection STOP (11/8/2017 3:56 PM)
//    public String getName() {
//        return name;
//    }
// --Commented out by Inspection STOP (11/8/2017 3:56 PM)
}
