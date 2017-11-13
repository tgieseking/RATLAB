package edu.gatech.cs2340.ratlab.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import edu.gatech.cs2340.ratlab.R;
import edu.gatech.cs2340.ratlab.model.SightingsManager;
import edu.gatech.cs2340.ratlab.model.RatSighting;
import edu.gatech.cs2340.ratlab.model.UserManager;

public class MainActivity extends AppCompatActivity {
    private UserManager userManager;
    private SightingsManager sightingsManager;

    private static final int REPORT_ACTIVITY_RESULT_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userManager = UserManager.getInstance();
        sightingsManager = SightingsManager.getInstance();
    }

    /**
     * Logs the current user out using firebase, and returns to the welcome screen. Note that the
     * welcome activity should always be on the bottom of the back stack so that this method closes
     * all other activities.
     *
     * @param view the logout button
     */
    public void logout(View view) {
        userManager.logout();
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * Changes the current activity to ReportSightingActivity when
     * the button reportSightingButton is clicked.
     *
     * @param view button clicked to activate this method
     */
    public void onClickReportSightings(View view) {
        Intent intent = new Intent(this, ReportSightingActivity.class);
        startActivityForResult(intent, REPORT_ACTIVITY_RESULT_CODE);
    }

    /**
     * Pass in rat sighting data when ReportSightingActivity is finished
     * @param requestCode the status of the request
     * @param resultCode the status of the result
     * @param data the data containing the rat sighting
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the ReportSightingActivity with an OK result
        if (requestCode == REPORT_ACTIVITY_RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                RatSighting newSighting =
                        (RatSighting) getIntent().getSerializableExtra("ratSighting");
                sightingsManager.addRatSightingToDatabase(newSighting);
            }
        }
    }

    /**
     * Changes the current activity to SightingListActivity when
     * the button sightingsButton is clicked.
     *
     * @param view sightingsButton on registration activity
     */
    public void onClickSightings(View view) {
        if (sightingsManager.isLoadingHistoricalDataComplete()) {
            Intent intent = new Intent(this, SightingListActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Historical data is still loading",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Changes the current activity to display the map filter display
     * when the map display button is pressed.
     *
     * @param view map display button
     */
    public void onClickMapDisplay(View view) {
        if (sightingsManager.isLoadingHistoricalDataComplete()) {
            Intent intent = new Intent(this, MapFilterActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Historical data is still loading",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickHistoricalGraph(View view) {
        Intent intent = new Intent(this, HistogramFilterActivity.class);
        startActivity(intent);
    }

}
