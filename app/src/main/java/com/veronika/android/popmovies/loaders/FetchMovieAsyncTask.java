package com.veronika.android.popmovies.loaders;

import android.os.AsyncTask;

import com.veronika.android.popmovies.interfaces.FetchDataInterface;
import com.veronika.android.popmovies.models.AndroidMovie;
import com.veronika.android.popmovies.utilities.MovieBaseJsonUtils;
import com.veronika.android.popmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * AsyncTask enables proper and easy use of the UI thread.
 * This class allows us to perform background operations and publish results on the UI thread without having to manipulate threads and/or handlers.
 *<String, Void, ArrayList<AndroidMovie>>
 * 1. String: Params, the type of the parameters sent to the task upon execution.
 * 2. Void: Progress, the type of the progress units published during the background computation.
 * 3. ArrayList<AndroidMovie>: Result, the type of the result of the background computation.

 */


public class FetchMovieAsyncTask  extends AsyncTask<String, Void, ArrayList<AndroidMovie>> {

    public FetchDataInterface delegate = null;

    @Override
    protected void onPreExecute() {
        //  View.mLoadingIndicator.setVisibility(View.VISIBLE);
        super.onPreExecute();

        //   new FetchMovieDataTask().execute(mSortBy);
    }

    @Override
    protected ArrayList<AndroidMovie> doInBackground(String... params) {
        // if there is no data, then it is nothing to do
        if (params.length == 0 ){
            return null;
        }
        String preferredSortCategory = params[0];

        String movieSearchResults;
        ArrayList<AndroidMovie> movieJSONList;

        URL movieRequestUrl = NetworkUtils.buildUrlMovieDbApi(preferredSortCategory);

        try {

          //  Log.i(TAG, "I am trying to get response from HttpUrl");
            movieSearchResults = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
           // Log.v(TAG, "MovieSearchResults: "+movieSearchResults);
            //Give results data to JSON
            movieJSONList= MovieBaseJsonUtils.getMovieStringsFromJson(movieSearchResults);
            return   movieJSONList;
        }
        catch (Exception e){

            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPostExecute(ArrayList<AndroidMovie> movieJSONList) {
        // mLoadingIndicator.setVisibility(View.INVISIBLE);

        if(movieJSONList != null){
            //if we have valid results

            //mMovieList = movieJSONList;
           // Log.v(TAG, "RESULTS: " + movieJSONList);
            delegate.fetchCallback(movieJSONList);
            //setMovieList(movieList);
           // mFetchListener.fetchCallback();
           // mMovieAdapter.setMovieList(mMovieList);

        }
        else {
            //showErrorMessage();
           // Log.v(TAG, "Didn't get data from JSONUtils");

        }

    }
}
