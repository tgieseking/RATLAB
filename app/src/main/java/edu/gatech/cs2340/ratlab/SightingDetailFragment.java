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
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_RATSIGHTING_ID = "ratSighting_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private RatSighting mRatSighting;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SightingDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mRatSighting = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mRatSighting.content);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sighting_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mRatSighting != null) {
            ((TextView) rootView.findViewById(R.id.sighting_detail)).setText(mRatSighting.details);
        }

        return rootView;
    }

}
