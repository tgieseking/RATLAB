package edu.gatech.cs2340.ratlab.controllers;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.gatech.cs2340.ratlab.R;
import edu.gatech.cs2340.ratlab.model.RatSighting;
import edu.gatech.cs2340.ratlab.model.SightingsManager;


/**
 * A fragment representing a single Sighting detail screen.
 * This fragment is either contained in a {@link SightingListActivity}
 * in two-pane mode (on tablets) or a {@link SightingDetailActivity}
 * on handsets.
 */
public class SightingDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_SIGHTING_ID = "sighting_id";

    /**
     * The rat sighting that this detail view is for.
     */
    private RatSighting mSighting;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SightingDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check if we got a valid sighting passed to us
        if (getArguments().containsKey(ARG_SIGHTING_ID)) {
            // Get the id from the intent arguments (bundle) and
            //ask the sightingsManager to give us the course object
            SightingsManager sightingsManager = SightingsManager.getInstance();
            // mCourse = sightingsManager.getCourseById(getArguments().getInt(ARG_COURSE_ID));
            mSighting = sightingsManager.getSightingByKey(
                    getArguments().getString(ARG_SIGHTING_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle("Sighting Details");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sighting_detail, container, false);

        // Show the dummy content as text in a TextView. CHANGE THIS STUFF !!!!!!!!!!!!!!!!!!!!!!!!!
        if (mSighting != null) {
            ((TextView) rootView.findViewById(R.id.sighting_detail)).setText(
                    ratSightingsDetails(mSighting));
        }

        return rootView;
    }

    private String ratSightingsDetails(RatSighting sighting) {
        if (sighting.getBorough() == null) {
            return "Key: " + sighting.getKey()
                    + "\nDate: " + sighting.getCreatedDateString()
                    + "\nLocation Type: " + sighting.getLocationType().getTextName()
                    + "\nAddress: " + sighting.getAddress()
                    + "\nLongitude: " + sighting.getLocation().getLongitude()
                    + "\nLatitude: " + sighting.getLocation().getLatitude();
        } else {
            return "Key: " + sighting.getKey()
                    + "\nDate: " + sighting.getCreatedDateString()
                    + "\nLocation Type: " + sighting.getLocationType().getTextName()
                    + "\nAddress: " + sighting.getAddress()
                    + "\nBorough: " + sighting.getBorough()
                    + "\nLatitude: " + sighting.getLocation().getLatitude()
                    + "\nLongitude: " + sighting.getLocation().getLongitude();
        }
    }
}
