package com.veronika.android.popmovies.loaders;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.veronika.android.popmovies.data.MoviesContract;

/**
 * The Async Loader for favourite movies
 */

public class FavouritesLoader extends AsyncTaskLoader<Cursor>{

    //Initialize a Cursor for holding all the data for favorites
    private Cursor mCursor = null;
    private String mSelectionArgs, mSelection;

    public FavouritesLoader(Context context, String selectionArgs){

        super(context);
        this.mSelectionArgs = selectionArgs;
        this.mSelection = null;
    }

    @Override
    protected void onStartLoading() {

        if (mCursor != null){
            deliverResult(mCursor);
        }
        else {
            //Force a new load
            forceLoad();
        }
    }

    @Override
    public Cursor loadInBackground() {

        try {
            if (mSelectionArgs != null){
                mSelection = MoviesContract.MoviesEntry.MOVIE_ID + "=?";
            }
            return getContext().getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI,
                    null,
                    mSelection,
                    (mSelectionArgs != null)? new String[]{mSelectionArgs} : null,
                    null);
        }
        catch (Exception e){
           // Log.e("FAVORITES LOADER", "Failed to asynchronously load data.");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(Cursor data) {
        mCursor = data;

        super.deliverResult(data);
    }
}
