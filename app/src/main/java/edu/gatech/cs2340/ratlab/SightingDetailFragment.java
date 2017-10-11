package edu.gatech.cs2340.ratlab;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.gatech.cs2340.ratlab.model.RatSighting;
import edu.gatech.cs2340.ratlab.model.Model;


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
     * The ratsighting that this detail view is for.
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

        if (getArguments().containsKey(ARG_SIGHTING_ID)) {
            // Get the id from the intent arguments (bundle) and
            //ask the model to give us the course object
            Model model = Model.getInstance();
            // mCourse = model.getCourseById(getArguments().getInt(ARG_COURSE_ID));
            mSighting = model.getCurrentSighting();

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle("Rat Sighting " + mSighting.getKey());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sighting_detail, container, false);

        // Show the dummy content as text in a TextView. CHANGE THIS STUFF !!!!!!!!!!!!!!!!!!!!!!!!!
        if (mSighting != null) {
            ((TextView) rootView.findViewById(R.id.sighting_detail)).setText(mSighting.getAddress());
        }

        return rootView;
    }

}
