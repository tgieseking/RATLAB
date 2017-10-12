package edu.gatech.cs2340.ratlab.model;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.gatech.cs2340.ratlab.controllers.LoginActivity;
import edu.gatech.cs2340.ratlab.controllers.MainActivity;

public class UserManager {
    private static final UserManager instance = new UserManager();
    public static UserManager getInstance() { return instance; }

    // Stores information on the currently logged in user
    private User currentUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    public User getCurrentUser() {
        return currentUser;
    }

    private UserManager() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void addUserToDatabase(String username, String name, String accountType) {
        String uid = firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase.getReference().child("users").child(uid)
                .child("username").setValue(username);
        firebaseDatabase.getReference().child("users").child(uid)
                .child("name").setValue(name);
        firebaseDatabase.getReference().child("users").child(uid)
                .child("account_type").setValue(accountType);
    }

    public int login() {
        String uid = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference userNode = firebaseDatabase.getReference().child("users").child(uid);
        final String email = firebaseAuth.getCurrentUser().getEmail();
        userNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = (String) dataSnapshot.child("username").getValue();
                String name = (String) dataSnapshot.child("name").getValue();
                String accountTypeString = (String) dataSnapshot.child("account_type").getValue();
                AccountType accountType = AccountType.accountTypeFromString(accountTypeString);
                if (accountType != null) {
                    User user = new User(email, username, name, accountType);
                    UserManager.this.currentUser = user;
                    Log.d("Login", "Successfully logged in " + username);
                } else {
                    UserManager.this.currentUser = null;
                    Log.d("Login", "Login error");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (currentUser != null) {
            return 1;
        } else {
            return 0;
        }
    }

    public void logout() {
        firebaseAuth.signOut();
        currentUser = null;
    }
}
