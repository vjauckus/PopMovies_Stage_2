/*
 * Copyright (C) 2017 VJauckus
 *
 */
package com.veronika.android.popmovies.utilities;

import com.veronika.android.popmovies.models.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by veronika on 17.11.17.
 */

public class MovieReviewJsonUtils {

    public static ArrayList<Review> getReviewStringsFromJson(String reviewResponseFromAPI)
            throws JSONException {

       // final String TAG = MovieReviewJsonUtils.class.getSimpleName();

        final String MOVIE_ERROR_MESSAGE = "status_message";

        final String MOVIE_RESULTS = "results";


        JSONObject movieJsonO = new JSONObject(reviewResponseFromAPI);
        //if there any errors in HttpUrl Connection
        if(movieJsonO.has(MOVIE_ERROR_MESSAGE)){
          //  String errorMessage = movieJsonO.getString(MOVIE_ERROR_MESSAGE);
          //  Log.v(TAG, "Http NOT Found or Server probably down: " + errorMessage);
            return null;
        }
        JSONArray resultsArray = movieJsonO.getJSONArray(MOVIE_RESULTS);
      //  Log.v(TAG, "JSON Review array has Length: " + resultsArray.length());


        ArrayList<Review> parsedReviewList = new ArrayList<>();

        for (int i = 0; i <  resultsArray.length(); i++){

            Review currentReview = new Review();

           // String reviewElement = resultsArray.getString(i);

            JSONObject singleReviewObjectFromJsonArray = resultsArray.getJSONObject(i);

            currentReview.setAuthor(singleReviewObjectFromJsonArray.getString("author"));
            currentReview.setContent(singleReviewObjectFromJsonArray.getString("content"));


           // Log.v(TAG, "Whole review data: "+ reviewElement + "\n");
            parsedReviewList.add(currentReview);
        }


        return parsedReviewList;
    }

}
