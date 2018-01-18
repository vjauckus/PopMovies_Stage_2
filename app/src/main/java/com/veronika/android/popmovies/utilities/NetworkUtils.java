/*
 * Copyright (C) 2017 VJauckus
 *
 */
package com.veronika.android.popmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.veronika.android.popmovies.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by veronika on 25.09.17.
 * These utilities will be used to communicate with Internet
 */

public class NetworkUtils {


    /**The format that we want the API to return**/
    //private static String format = "json";

    private final static String BASE_URL_MOVIE_DB_API = "https://api.themoviedb.org/3/movie";


    private final static String API_KEY_PARAM = "api_key";

    private static String API_KEY_VALUE;
    
    private final static  String BASE_URL_PICASSO = "http://image.tmdb.org/t/p/";

    private final static String POSTER_SIZE ="w185/";

    private final static String VIDEOS ="videos";

    private final static String REVIEWS ="reviews";


    /**
     * Builds the URL for MovieDB API for based data.
     * @param sortByChoice The keyword for sorting the data
     * @return URL to use to query the Movie API from api.themoviedb.org
     */
    public static URL buildUrlMovieDbApi(String sortByChoice){



        Uri builtUri = Uri.parse(BASE_URL_MOVIE_DB_API).buildUpon()
                .appendEncodedPath(sortByChoice)
                .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
                .build();
        URL url = null;

        try {
            url = new URL(builtUri.toString());
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        //https://api.themoviedb.org/3/movie/popular?api_key=
       // Log.v(NetworkUtils.class.getSimpleName(), "Building URL for based data of MovieDB API: "+url);
        return url;
    }

    /**
     *  * Builds the URL for reviews of a single movie.
     * @param movieId is unique movie Id
     * @return the URL for loading reviews of movie
     */

    public static URL buildUrlForReviews(String movieId){

        Uri builtUri = Uri.parse(BASE_URL_MOVIE_DB_API+"/"+movieId).buildUpon()
                .appendEncodedPath(REVIEWS)
                .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
                .build();
        URL url = null;

        try {
            url = new URL(builtUri.toString());
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        //https://api.themoviedb.org/3/movie/346364/reviews?api_key=
       // Log.v(NetworkUtils.class.getSimpleName(), "Building Reviews'URL for MovieDB API: "+url);
        return url;
    }

    /**
     *   *  * Builds the URL for trailers / videos of a single movie.
     * @param movieId the unique movie Id
     * @return the URL for loading of trailers for movie
     */
    public static URL buildUrlForTrailer(String movieId){

        Uri builtUri = Uri.parse(BASE_URL_MOVIE_DB_API+"/"+movieId).buildUpon()
                .appendEncodedPath(VIDEOS)
                .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
                .build();
        URL url = null;

        try {
            url = new URL(builtUri.toString());
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        //https://api.themoviedb.org/3/movie/346364/videos?api_key=
       // Log.v(NetworkUtils.class.getSimpleName(), "Building Trailers'URL for MovieDB API: "+url);
        return url;
    }

    /**
     * Builds the URL for Picasso caching library
     * @param mPosterPath The path of poster got from JSON
     * @return URL for binding an image library
     */
    public static String buildUrlPicasso(String mPosterPath){

        String url = BASE_URL_PICASSO + POSTER_SIZE +"/"+ mPosterPath;
      //  Log.v(NetworkUtils.class.getSimpleName(), "Building URL for Picasso: "+url);
        return url;
    }

    public static boolean checkConnection(Context context) {

        API_KEY_VALUE = context.getString(R.string.THE_MOVIE_DB_API_TOKEN);

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {

            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
        catch (NullPointerException ex){
            ex.printStackTrace();
            return false;
        }

    }

    /**
     * This method returns the results from HTTP response-
     * @param url The URL to fetch the HTTP response from
     * @return The contents of the HTTP response
     * @throws IOException The exception in case of missing response from HttpUrl
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInputData = scanner.hasNext();

            if(hasInputData){
                return scanner.next();
            }
            else{
                return null;
            }

        }
        finally {
            urlConnection.disconnect();
        }
    }


}
