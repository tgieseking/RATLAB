package edu.gatech.cs2340.ratlab.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import edu.gatech.cs2340.ratlab.R;
import edu.gatech.cs2340.ratlab.model.SightingsManager;
import edu.gatech.cs2340.ratlab.model.RatSighting;

import java.util.List;

/**
 * TOP LEVEL WINDOW THAT THE USER SEES WHEN CLICKING RAT SIGHTINGS
 *
 * An activity representing a list of Sightings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link SightingDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class SightingListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device. THIS IS EXTRA CREDIT.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sighting_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Setup the recycler view by getting it from our layout in the main window
        View recyclerView = findViewById(R.id.sighting_list);
        assert recyclerView != null;

        //Hook up the adapter to the view
        setupRecyclerView((RecyclerView) recyclerView);

        //For multiple displays
        if (findViewById(R.id.sighting_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    /**
     * Set up an adapter and hook it to the provided view
     * @param recyclerView the view that needs this adapter
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        SightingsManager sightingsManager = SightingsManager.getInstance();
        recyclerView.setAdapter(
                new SimpleSightingRecyclerViewAdapter(sightingsManager.createSightingsList()));
    }

    /**
     * This inner class is our custom adapter.  It takes our basic model information and
     * converts it to the correct layout for this view.
     *
     * In this case, we are just mapping the toString of the RatSighting object to a text field.
     */
    public class SimpleSightingRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleSightingRecyclerViewAdapter.ViewHolder> {

        /**
         * Collection of items to be shown in the list
         */
        private final List<RatSighting> mSightings;

        /**
         * set the items to be used by the adapter
         * @param items the list of items to be displayed in the recycler view
         */
        public SimpleSightingRecyclerViewAdapter(List<RatSighting> items) {
            mSightings = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            /*
              This sets up the view for each individual item in the recycler display
              To edit the actual layout, we would look at: res/layout/sighting_list_content.xml
              If you look at the example file, you will see it currently just 2 TextView elements
             */
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sighting_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            final SightingsManager sightingsManager = SightingsManager.getInstance();

            /*
            This is where we have to bind each data element in the list (given by position
            parameter) to an element in the view (which is one of our two TextView widgets
             */
            //start by getting the element at the correct position
            holder.mSighting = mSightings.get(position);
            /*
              Now we bind the data to the widgets.
             */
            holder.mIdView.setText(mSightings.get(position).getCreatedDateString());
            holder.mContentView.setText(mSightings.get(position).getAddress().getAddressLine());

            /*
             * set up a listener to handle if the user clicks on this list item, what should happen?
             */
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        //if a two pane window, we change the contents on the main screen
                        Bundle arguments = new Bundle();
                        arguments.putString(SightingDetailFragment.ARG_SIGHTING_ID,
                                holder.mSighting.getCreatedDateString());
                        SightingDetailFragment fragment = new SightingDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.sighting_detail_container, fragment)
                                .commit();
                    } else {
                        //on a phone, we need to change windows to the detail view
                        Context context = v.getContext();
                        //create our new intent with the new screen (activity)
                        Intent intent = new Intent(context, SightingDetailActivity.class);
                        /*
                            pass along the date of the sighting so we can retrieve the correct data
                            in the next window
                         */
                        intent.putExtra(SightingDetailFragment.ARG_SIGHTING_ID,
                                holder.mSighting.getKey());

                        //now just display the new window
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mSightings.size();
        }

        /**
         * This inner class represents a ViewHolder which provides us a way to cache information
         * about the binding between the model element (in this case a RatSighting) and the widgets
         * in the list view (in this case the two TextView)
         */
        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public RatSighting mSighting;

            /**
             * Constructor that creates a ViewHolder object with attributes
             * mView, mIdView, mContentView, and mSighting.
             *
             * @param view the view to display on the sightingList
             */
            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = view.findViewById(R.id.id);
                mContentView = view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
