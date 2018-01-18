package com.veronika.android.popmovies.loaders;

import android.content.Context;
import android.os.AsyncTask;

import com.veronika.android.popmovies.interfaces.FetchTrailerInterface;
import com.veronika.android.popmovies.models.Trailer;
import com.veronika.android.popmovies.utilities.MovieTrailerJsonUtils;
import com.veronika.android.popmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * The class for Async Loading of trailers for a single movie
 */

public class TrailerLoader extends AsyncTask<String, Void, ArrayList<Trailer>> {

    private final FetchTrailerInterface mCallback;

    //private static final String TAG = TrailerLoader.class.getSimpleName();


    private ArrayList<Trailer> mTrailerList;

    //Constructor

    public TrailerLoader(Context context){

       this.mCallback = (FetchTrailerInterface) context;
       // mMovieId = movieId;
    }

    @Override
    protected ArrayList<Trailer> doInBackground(String... strings) {

        String movieId = strings[0];
        if(movieId == null || movieId.length() == 0){

            return null;
        }

        mTrailerList = new ArrayList<>();

        URL trailerRequestURL = null;
        String  trailerSearchResults = "";

        trailerRequestURL = NetworkUtils.buildUrlForTrailer(movieId);

        if (trailerRequestURL != null){

           // Log.v(TAG, "I am trying to load trailers from API");

            try{
                trailerSearchResults = NetworkUtils.getResponseFromHttpUrl(trailerRequestURL);

                try {
                    mTrailerList = MovieTrailerJsonUtils.getTrailerStringsFromJson(trailerSearchResults);
                    if (mTrailerList.size() !=0 ){
                     //   Log.v(TAG, mTrailerList.toString());
                    }
                    else {
                      //  Log.v(TAG, "No Trailers are here");
                    }

                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
            catch (IOException e){

                e.printStackTrace();
            }

        }

        return mTrailerList;
    }


    @Override
    protected void onPostExecute(ArrayList<Trailer> trailers) {
        super.onPostExecute(trailers);

        mTrailerList = trailers;
       //  String mTrailerVideoKeyString ="";
        StringBuilder mTrailerVideoKeyString = new StringBuilder("");

        if(mTrailerList.size() != 0){

            for (Trailer trailer: mTrailerList){

                if (mTrailerVideoKeyString.equals("")){
                    mTrailerVideoKeyString.append(trailer.getVideoKey());

                }
                mTrailerVideoKeyString.append(", ");
                mTrailerVideoKeyString.append(trailer.getVideoKey());
            }
        }
      //  Log.v(TAG, "Trailers video-key found: "+mTrailerVideoKeyString);
        mCallback.fetchCallbackTrailer(trailers);
    }

}

