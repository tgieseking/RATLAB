package edu.gatech.cs2340.ratlab.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import edu.gatech.cs2340.ratlab.R;
import edu.gatech.cs2340.ratlab.SightingListActivity;
import edu.gatech.cs2340.ratlab.model.Borough;
import edu.gatech.cs2340.ratlab.model.Model;
import edu.gatech.cs2340.ratlab.model.RatSighting;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Logs the current user out using firebase, and returns to the welcome screen. Note that the
     * welcome activity should always be on the bottom of the back stack so that this method closes
     * all other activities.
     *
     * @param view the logout button
     */
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /** This is a method to test adding rat sightings to the database
     *
     * @param view button clicked to activate this method
     */
    public void pushSightingTest(View view) {
        RatSighting testSighting = new RatSighting(null, "testDate", "testLT", "testAddress",
                "testZip", "testCity", Borough.QUEENS, 10.1, 11.3);
        Model.getInstance().addRatSightingToDatabase(testSighting);
        Log.d("sightings_database", "Pushed sighting");
    }

    /**
     * Changes the current activity to SightingListActivity when
     * the button sightingsButton is clicked.
     *
     * @param view sightingsButton on registration activity
     */
    public void onClickSightings(View view) {
        Intent intent = new Intent(this, SightingListActivity.class);
        startActivity(intent);
    }
}
