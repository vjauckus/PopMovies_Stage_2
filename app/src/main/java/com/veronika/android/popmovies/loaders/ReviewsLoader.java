package com.veronika.android.popmovies.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.veronika.android.popmovies.models.Review;
import com.veronika.android.popmovies.utilities.MovieReviewJsonUtils;
import com.veronika.android.popmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by veronika on 02.12.17.
 */

public class ReviewsLoader extends AsyncTaskLoader<ArrayList<Review>> {

   // private static final String TAG = ReviewsLoader.class.getSimpleName();
    private String mMovieId;
    private ArrayList<Review> mReviewList;


    //Constructor
    public ReviewsLoader(Context context, String movieId){

        super(context);
        mMovieId = movieId;

    }

    @Override
    protected void onStartLoading() {

        if (mReviewList != null){
            deliverResult(mReviewList);
        }
        else {
            forceLoad();
        }

    }

    @Override
    public ArrayList<Review> loadInBackground() {

        if (mMovieId == null || mMovieId.length() == 0){
            return null;
        }
        mReviewList = new ArrayList<>();

        URL reviewRequestURL;
        String  reviewSearchResults;

        reviewRequestURL = NetworkUtils.buildUrlForReviews(mMovieId);

        if (reviewRequestURL != null){

          //  Log.v(TAG, "I am trying to load reviews from API");

            try{
                reviewSearchResults = NetworkUtils.getResponseFromHttpUrl(reviewRequestURL);

                try {
                    mReviewList = MovieReviewJsonUtils.getReviewStringsFromJson(reviewSearchResults);

                  //  Log.v(TAG, mReviewList.toString());
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
            catch (IOException e){

                e.printStackTrace();
            }

        }

        return mReviewList;
    }

    @Override
    public void deliverResult(ArrayList<Review> data) {
        mReviewList = data;
        super.deliverResult(data);
    }
}
