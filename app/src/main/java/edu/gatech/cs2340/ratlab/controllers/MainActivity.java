package edu.gatech.cs2340.ratlab.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.gatech.cs2340.ratlab.R;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mUserReference;
    private ValueEventListener mUserListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Dear god, future Timothy please change this line of code once you figure out how
        mUserReference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @Override
    public void onStart() {
        super.onStart();
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = (String) dataSnapshot.child("username").getValue();
                TextView welcomeView = (TextView) findViewById(R.id.welcomeText);
                welcomeView.setText("Welcome, " + username);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Could not find username",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mUserListener = userListener;
        mUserReference.addValueEventListener(mUserListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mUserListener != null) {
            mUserReference.removeEventListener(mUserListener);
        }
    }

    public void logout(View view) {
        if (mUserListener != null) {
            mUserReference.removeEventListener(mUserListener);
        }
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
