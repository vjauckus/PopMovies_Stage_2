/*
 * Copyright (C) 2017 VJauckus
 *
 */
package com.veronika.android.popmovies.utilities;

import com.veronika.android.popmovies.models.AndroidMovie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * * Utility class to handle JSON data.
 * Created by veronika on 29.09.17.
 */

public class MovieBaseJsonUtils {

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * @param movieBaseJsonStr JSON response from the MovieDB API server
     * @return Array of Strings describing movie data
     * @throws JSONException If JSON data cannot be properly parsed
     */

    public static ArrayList<AndroidMovie> getMovieStringsFromJson(String movieBaseJsonStr)
            throws JSONException {

       // final String TAG = MovieBaseJsonUtils.class.getSimpleName();

        final String MOVIE_ERROR_MESSAGE = "status_message";

        final String MOVIE_RESULTS = "results";


        JSONObject movieJsonO = new JSONObject(movieBaseJsonStr);
        //if there any errors in HttpUrl Connection
        if(movieJsonO.has(MOVIE_ERROR_MESSAGE)){
            String errorMessage = movieJsonO.getString(MOVIE_ERROR_MESSAGE);
           // Log.v(TAG, "Http NOT Found or Server probably down: " + errorMessage);
            return null;
        }
        JSONArray resultsArray = movieJsonO.getJSONArray(MOVIE_RESULTS);
       // Log.v(TAG, "JSON Base data array has Length: " + resultsArray.length());


        ArrayList<AndroidMovie> parsedMovieList = new ArrayList<>();

        for (int i = 0; i <  resultsArray.length(); i++){

            AndroidMovie currentMovie = new AndroidMovie();

            String movieElement =resultsArray.getString(i);

            JSONObject singleMovieJson = resultsArray.getJSONObject(i);

            currentMovie.setId(singleMovieJson.getInt("id"));
            currentMovie.setTitle(singleMovieJson.getString("title"));
            currentMovie.setOverview(singleMovieJson.getString("overview"));
            currentMovie.setImage(singleMovieJson.getString("poster_path"));
            currentMovie.setAverage(singleMovieJson.getDouble("vote_average"));
            currentMovie.setReleaseDate(singleMovieJson.getString("release_date").split("-")[0]);

            //Log.v(TAG, parsedMovieData[i] + "\n");
          //  Log.v(TAG, "Based data: "+ movieElement + "\n");
            parsedMovieList.add(currentMovie);
        }


        return parsedMovieList;
    }

}
