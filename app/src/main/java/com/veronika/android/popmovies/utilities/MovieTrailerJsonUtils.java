/*
 * Copyright (C) 2017 VJauckus
 *
 */
package com.veronika.android.popmovies.utilities;

import com.veronika.android.popmovies.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by veronika on 17.11.17.
 */

public class MovieTrailerJsonUtils {

    public static ArrayList<Trailer> getTrailerStringsFromJson(String trailerResponseFromAPI)
            throws JSONException {

       // final String TAG = MovieTrailerJsonUtils.class.getSimpleName();

        final String MOVIE_ERROR_MESSAGE = "status_message";

        final String MOVIE_RESULTS = "results";


        JSONObject movieJsonO = new JSONObject(trailerResponseFromAPI);
        //if there any errors in HttpUrl Connection
        if(movieJsonO.has(MOVIE_ERROR_MESSAGE)){
          //  String errorMessage = movieJsonO.getString(MOVIE_ERROR_MESSAGE);
         //   Log.v(TAG, "Http NOT Found or Server probably down: " + errorMessage);
            return null;
        }
        JSONArray resultsArray = movieJsonO.getJSONArray(MOVIE_RESULTS);
       // Log.v(TAG, "JSON Trailer array has Length: " + resultsArray.length());


        ArrayList<Trailer> parsedTrailerList = new ArrayList<>();

        for (int i = 0; i <  resultsArray.length(); i++){

            Trailer currentTrailer = new Trailer();

           // String trailerElement = resultsArray.getString(i);

            JSONObject singleTrailerObjectFromJsonArray = resultsArray.getJSONObject(i);

            currentTrailer.setVideoKey(singleTrailerObjectFromJsonArray.getString("key"));


          //  Log.v(TAG, "Whole trailer data: "+ trailerElement + "\n");
            parsedTrailerList.add(currentTrailer);
        }


        return parsedTrailerList;
    }
}
