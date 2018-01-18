/*
 * Copyright (C) 2017 VJauckus
 *
 */
package com.veronika.android.popmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.stetho.Stetho;
import com.veronika.android.popmovies.R;
import com.veronika.android.popmovies.adapters.CustomCursorAdapter;
import com.veronika.android.popmovies.data.MoviesContract;
import com.veronika.android.popmovies.models.AndroidMovie;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The class show favourites movies saved in database by user
 */

public class FavouriteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
CustomCursorAdapter.CustomCursorOnClickHandler{

    private static final String TAG = FavouriteActivity.class.getSimpleName();

    //private LoaderManager.LoaderCallbacks<Cursor> favoritesLoaderResultsCallback;
    private final static int FAVOURITE_LOADER = 14;

    private CustomCursorAdapter mCustomCursorAdapter;
    private GridLayoutManager mGridLayoutManager;
    @BindView(R.id.favorite_recycle_view_movie) RecyclerView mRecyclerView;
    @BindView(R.id.tv_favourites_not_here_label) TextView mFavouritesMissingLabel;
    @BindView(R.id.tv_favourites_not_here) TextView mFavouritesMissing;
    private int intOrientation;
  //  private Parcelable state;
   // private Cursor mCursorData = null;
   // private int mPosition = RecyclerView.NO_POSITION;
  //  private boolean isCursorLoaded = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_main);
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

      // Log.i(TAG, "Favourite Activity onCreate");

        //If orientation portrait, then is intOrientation = 2, else 3
        intOrientation = checkDeviceOrientation(this);
        mGridLayoutManager = new GridLayoutManager(this, intOrientation);

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        /*
         * The Adapter is responsible for linking our movie data with the Views that
         * will end up displaying our data.
         *
         * Although passing in "this" twice may seem strange, it is actually a sign of separation
         * of concerns, which is best programming practice. The Adapter requires an
         * Android Context (which all Activities are) as well as an onClickHandler. Since our
         * FavouriteActivity implements the Adapter OnClickHandler interface, "this"
         * is also an instance of that type of handler.
         */

        //Initialize Adapter

        mCustomCursorAdapter = new CustomCursorAdapter(FavouriteActivity.this, this);

        mRecyclerView.setAdapter(mCustomCursorAdapter);
         /* This connects our Activity into the loader lifecycle. */
        getSupportLoaderManager().initLoader(FAVOURITE_LOADER, null, this);

       // initCallback();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());

    }

    private int checkDeviceOrientation(Context context) {
        intOrientation = context.getResources().getConfiguration().orientation;

        if(intOrientation == Configuration.ORIENTATION_PORTRAIT){
           // Log.v(TAG, "*******Orientation is portrait");
            return 2;
        }
        else{
          //  Log.v(TAG, "*******Orientation is landshape");
            return 3;
        }
    }

/*    private void initFavouriteLoader(int loaderId){

        if (mCursorData == null){
            getSupportLoaderManager().initLoader(FAVOURITE_LOADER, null, this);

        }
        else {
            getSupportLoaderManager().restartLoader(FAVOURITE_LOADER, null, this);
            mCustomCursorAdapter.swapCursor(mCursorData);
        }
    }
*/
    @Override
    protected void onStop() {
        super.onStop();
     //   Log.i(TAG, "FavouriteActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    //    mCursorData.close();
       // Log.i(TAG, "FavouriteActivity onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
      //  state = mGridLayoutManager.onSaveInstanceState();
       // Log.i(TAG, "FavouriteActivity onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  Log.i(TAG, "FavouriteActivity onResume");

        getSupportLoaderManager().restartLoader(FAVOURITE_LOADER, null, this);


    }

    @Override
    protected void onStart() {
        super.onStart();
       // Log.i(TAG, "FavouriteActivity onStart");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle loaderArgs) {


        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mCursorData = null;

            @Override
            protected void onStartLoading() {
                // Delivers any previously loaded data immediately
                if (mCursorData != null){
                    deliverResult(mCursorData);
                }
                else {
                    // Force a new load
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {

                try {
                    return getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                }
                catch (Exception e){
                    e.printStackTrace();
                  //  Log.e(TAG, "Failed to asynchronously load data");
                      /* close the Cursor to avoid memory leaks! */
                    mCursorData.close();
                    return null;
                }

            }

            @Override
            public void deliverResult(Cursor data) {
                mCursorData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mCustomCursorAdapter.swapCursor(data);

        if (data == null || data.getCount() <=0){
          //  Toast.makeText(FavouriteActivity.this, getString(R.string.favourites_not_here), Toast.LENGTH_SHORT).show();
          //  mCursorData.close();
            showNoFavouritesView();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mCustomCursorAdapter.swapCursor(null);
    }

    private void showNoFavouritesView(){

       // Log.v(TAG, "I am in showNoFavouritesView, mCursorData: ");
        mRecyclerView.setVisibility(View.INVISIBLE);

        mFavouritesMissing.setVisibility(View.VISIBLE);
        mFavouritesMissingLabel.setVisibility(View.VISIBLE);


        mFavouritesMissingLabel.setText(getString(R.string.favorite_movies));
        mFavouritesMissing.setText(getString(R.string.favourites_not_here));

    }
    @Override
    public void onMovieClick(int cursorPosition, Cursor cursor) {

      //  Toast.makeText(this, "The movie was clicked: "+ cursorPosition, Toast.LENGTH_SHORT).show();
      //  Log.d(TAG,"The movie was clicked: "+ cursorPosition );
        cursor.moveToPosition(cursorPosition);

        try {

            AndroidMovie currentMovie = new AndroidMovie();
            int movieId = cursor.getInt(cursor.getColumnIndex(MoviesContract.MoviesEntry.MOVIE_ID));
            currentMovie.setId(movieId);
            String movieImage = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.MOVIE_POSTER_PATH));
            currentMovie.setImage(movieImage);
            String movieTitle = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.MOVIE_TITLE));
            currentMovie.setTitle(movieTitle);
            String release_date = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.MOVIE_RELEASE_DATE));
            currentMovie.setReleaseDate(release_date);
            String overview = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.MOVIE_OVERVIEW));
            currentMovie.setOverview(overview);
            Double rating = cursor.getDouble(cursor.getColumnIndex(MoviesContract.MoviesEntry.MOVIE_RATING));
            currentMovie.setAverage(rating);

            //Start DetailActivity
            Intent startDetailActivityIntent = new Intent(this, DetailActivity.class);
            startDetailActivityIntent.putExtra(Intent.EXTRA_TEXT, currentMovie);

            startActivity(startDetailActivityIntent);

        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

}
