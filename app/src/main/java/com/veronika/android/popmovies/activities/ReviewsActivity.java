/*
 * Copyright (C) 2017 VJauckus
 *
 */
package com.veronika.android.popmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.veronika.android.popmovies.R;
import com.veronika.android.popmovies.adapters.ReviewAdapter;
import com.veronika.android.popmovies.loaders.ReviewsLoader;
import com.veronika.android.popmovies.models.AndroidMovie;
import com.veronika.android.popmovies.models.Review;
import com.veronika.android.popmovies.utilities.NetworkUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This Class load reviews from server and visualise them, if they are available
 */

public class ReviewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Review>> {

   // private static final String TAG = ReviewsActivity.class.getSimpleName();

    private ArrayList<Review> mReviews;
    private ReviewAdapter mReviewAdapter;
    private LinearLayoutManager layoutManagerReviews;
    private static final int REVIEWS_LOADER = 10;

    private AndroidMovie mCurrentMovie;
    private String mMovieId;

  //  private RecyclerView recyclerViewReviews;
    @BindView(R.id.rv_reviews_recycler_view) RecyclerView recyclerViewReviews;
    @BindView(R.id.tv_reviews_not_here_label) TextView mMissingReviewsLabel;
    @BindView(R.id.tv_reviews_not_here) TextView mMissingReviewsInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviews_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

       // Log.i(TAG, "ReviewsActivity onCreate");
        Intent intentThatStartThisActivity = getIntent();

        layoutManagerReviews = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewReviews.setLayoutManager(layoutManagerReviews);

        if(savedInstanceState == null || !savedInstanceState.containsKey("movieId")){

            mReviews = new ArrayList<>();

            this.mMovieId = intentThatStartThisActivity.getStringExtra(Intent.EXTRA_TEXT);

        }
        else {
            // Restore some state after rotation or leave the app
            this.mMovieId = savedInstanceState.getString("movieId");
            mReviews = savedInstanceState.getParcelableArrayList("reviewsList");
          //  getSupportLoaderManager().restartLoader(REVIEWS_LOADER, null, this);

        }
        if(mMovieId != null){

          //  Log.i(TAG, "I am trying to get review from HttpUrl for MovieId: "+mMovieId);

            if(NetworkUtils.checkConnection(this)){

                mReviewAdapter = new ReviewAdapter(mReviews);
                recyclerViewReviews.setAdapter(mReviewAdapter);
                getSupportLoaderManager().initLoader(REVIEWS_LOADER, null, this);
            }

            else {
                //No Internet connection
                recyclerViewReviews.setVisibility(View.INVISIBLE);
                mMissingReviewsInfo.setText(getString(R.string.error_title));
                mMissingReviewsLabel.setText(getString(R.string.error_message));
                mMissingReviewsLabel.setVisibility(View.VISIBLE);
                mMissingReviewsInfo.setVisibility(View.VISIBLE);
            }


        }
     /*  else {
            //Log.v(TAG, "No details data are available");
        }
        */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

       // outState.putParcelable("moviesDetailsData", mCurrentMovie);
        //outState.putString(DATA, DATA);
        outState.putString("movieId", mMovieId);

        outState.putParcelableArrayList("reviewsList", mReviews);
      //  Log.v(TAG, "I am in onSavedInstanceState, mReviews: "+mReviews);

        recyclerViewReviews = findViewById(R.id.rv_reviews_recycler_view);

        super.onSaveInstanceState(outState);
    }


    @Override
    public void onLoadFinished(android.support.v4.content.Loader<ArrayList<Review>> loader, ArrayList<Review> data) {
        if (NetworkUtils.checkConnection(this)){

            mReviews.addAll(data);

            if (mReviews == null || mReviews.size() == 0){
                recyclerViewReviews.setVisibility(View.INVISIBLE);
                mMissingReviewsLabel.setVisibility(View.VISIBLE);
                mMissingReviewsInfo.setVisibility(View.VISIBLE);

                mMissingReviewsInfo.setText(getString(R.string.reviews_not_available));
                mMissingReviewsLabel.setText(getString(R.string.review_label));
              //  Toast.makeText(this, getString(R.string.reviews_not_available), Toast.LENGTH_SHORT).show();
            }
            else {
                //Reviews are here
              //  mReviews.addAll(data);
                mReviewAdapter.notifyDataSetChanged();
                mMissingReviewsLabel.setVisibility(View.GONE);
                mMissingReviewsInfo.setVisibility(View.GONE);
            }
        }
        else {
            Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public android.support.v4.content.Loader<ArrayList<Review>> onCreateLoader(int id, Bundle args) {

        if (NetworkUtils.checkConnection(this)){

            return new ReviewsLoader(this, mMovieId);
        }
        else {
            return null;
        }

    }


    @Override
    public void onLoaderReset(android.support.v4.content.Loader<ArrayList<Review>> loader) {

    }
}
