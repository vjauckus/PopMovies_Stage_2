/*
 * Copyright (C) 2017 VJauckus
 *
 */
package com.veronika.android.popmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.stetho.Stetho;
import com.veronika.android.popmovies.R;
import com.veronika.android.popmovies.UserPreferences;
import com.veronika.android.popmovies.adapters.AndroidMovieAdapter;
import com.veronika.android.popmovies.interfaces.FetchDataInterface;
import com.veronika.android.popmovies.loaders.FetchMovieAsyncTask;
import com.veronika.android.popmovies.models.AndroidMovie;
import com.veronika.android.popmovies.utilities.NetworkUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FetchDataInterface, AndroidMovieAdapter.MovieAdapterOnClickHandler {

    private FetchMovieAsyncTask asyncTask = new FetchMovieAsyncTask();


   // private final String TAG = MainActivity.class.getSimpleName();
    private final String sortByPopularity = "popular";
    private final String sortByRate="top_rated";

    private GridLayoutManager mGridLayoutManager;

    private AndroidMovieAdapter movieAdapter;

    private ArrayList<AndroidMovie> movieList;

    private String initialSort = sortByPopularity;
    private String prefferedUserSort = "";

    private int intOrientation;

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private TextView mErrorTitleDisplay;
    // private ProgressBar mLoadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     //   Log.i(TAG, "MainActivity onCreate");
        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")){
            movieList = new ArrayList<>();
        }
        else {
            movieList = savedInstanceState.getParcelableArrayList("movies");
        }
        setContentView(R.layout.activity_main);

        //Initial sort setting
        initialSort = sortByPopularity;
        // movieList = null;

        mRecyclerView = findViewById(R.id.recycle_view_movie);

        if(!NetworkUtils.checkConnection(this)){
            //if Network connection is NOT available
            showErrorMessage();
        }
        //If Network connection is checked and OK

        //If orientation portrait, then is intOrientation = 2, else 3
        intOrientation = checkDeviceOrientation(this);
        //  intOrientationColumn = Utility.calculateNoOfColumns(getApplicationContext());

      //  Log.v(TAG, "Calculated Nr of columns: " + intOrientation);

        mGridLayoutManager  = new GridLayoutManager(this, intOrientation);

        mGridLayoutManager.scrollToPositionWithOffset(0,0);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        //load and get data

        //Initial sort setting is sort by popularity
        if(UserPreferences.getUserPreferredSort(this).equals("")){
            loadMoviesData(initialSort);
        }
        else {
            loadMoviesData(UserPreferences.getUserPreferredSort(this));
        }


        movieAdapter = new AndroidMovieAdapter(this);
        movieAdapter.setMovieList(movieList);

        mRecyclerView.setAdapter(movieAdapter);

        //Use debugger bridge: Stetho (Usage in Chrome: chrome://inspect/#devices)
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", movieList);
        super.onSaveInstanceState(outState);
    }

    private  void showMoviePoster(){

    //    Log.i(TAG, "I am trying to get response from HttpUrl");

        mRecyclerView.setVisibility(View.VISIBLE);

        //  Toast.makeText(this, "Loading of data takes a moment. PLease wait...", Toast.LENGTH_SHORT).show();

    }
    private   void showErrorMessage(){

      //  Log.d(TAG, "I am trying to get response from HttpUrl");

        mRecyclerView.setVisibility(View.INVISIBLE);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mErrorTitleDisplay = findViewById(R.id.tv_error_title_display);
        mErrorTitleDisplay.setVisibility(View.VISIBLE);

        mErrorMessageDisplay.setVisibility(View.VISIBLE);

       // Toast.makeText(this, "Loading of data does not work proper. PLease wait...", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        asyncTask = new FetchMovieAsyncTask();
        if(id == R.id.sort_by_popularity){

            prefferedUserSort = sortByPopularity;
            UserPreferences.setUserPrefferedSort(prefferedUserSort,this);

            loadMoviesData(prefferedUserSort);

         //   Toast.makeText(this, getResources().getString(R.string.most_popular_movie), Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(id == R.id.sort_by_top_rated){

            prefferedUserSort = sortByRate;
            UserPreferences.setUserPrefferedSort(prefferedUserSort,this);

            loadMoviesData(prefferedUserSort);

          //  Toast.makeText(this, getResources().getString(R.string.top_rated_movie), Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(id == R.id.show_favorites){

            Intent showFavoriteIntent = new Intent(this, FavouriteActivity.class);
           // Toast.makeText(this, getResources().getString(R.string.see_favourites), Toast.LENGTH_SHORT).show();
            startActivity(showFavoriteIntent);

            return true;
        }
        else if(id == R.id.show_about){

            Intent showAboutIntent = new Intent(this, AboutActivity.class);
            // Toast.makeText(this, getResources().getString(R.string.see_favourites), Toast.LENGTH_SHORT).show();
            startActivity(showAboutIntent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * This method will get the user's preferred sortBy setting for movies
     * and then tell some background method to get the movies data in the background
     * @param sortBy The initial sort or User's preferred sort by setting
     */
    private void loadMoviesData(String sortBy){

        //Must be changed, if user wants to change sort by category;
       //Log.i(TAG, "I am trying to load movies data from API");

        try {
            // new FetchMovieAsyncTask().execute(sortBy);
            asyncTask.delegate = this;

            asyncTask.execute(sortBy);

            showMoviePoster();
        }
        catch (Exception e){

            // Toast.makeText(this, "Loading of data does not work proper. PLease try again", Toast.LENGTH_SHORT).show();
            showErrorMessage();
            e.printStackTrace();
        }

    }
    private int checkDeviceOrientation(Context context) {
        intOrientation = context.getResources().getConfiguration().orientation;
        boolean tabletSize = context.getResources().getBoolean(R.bool.isTablet);

        if(intOrientation == Configuration.ORIENTATION_PORTRAIT){
          //  Log.v(TAG, "*******Orientation is portrait");
            if (tabletSize){
                //if it is Tablet
                return 3;
            }
            else{
                //if it is Phone
                return 2;
            }

        }
        else{
           // Log.v(TAG, "*******Orientation is landscape");
            //if it is Tablet
            if (tabletSize){
                //if it is Tablet
                return 4;
            }
            else{
                //if it is Phone
                return 3;
            }

        }
    }

    @Override
    public void fetchCallback(ArrayList<AndroidMovie> movieArrayList) {
        //Here we received the results form async class
        movieList = movieArrayList;
        movieAdapter.setMovieList(movieArrayList);
       // Log.v(TAG, "RESULTS got in MainActivity delivered from FetchMovieDataList: " + (movieArrayList).toString());
    }


    @Override
    public void onClick(AndroidMovie currentMovie) {

        Context context = this;
        //  Toast.makeText(context, currentMovie.getTitle() + ". Image was clicked: "+currentMovie.getMovieImage(), Toast.LENGTH_SHORT).show();
        //Start DetailActivity activity on Click

        Class destinationClass = DetailActivity.class;
        Intent intentStartDetailMovie = new Intent(context, destinationClass);
        // Toast.makeText(this.getApplication(), "Open movie details", Toast.LENGTH_SHORT).show();
        intentStartDetailMovie.putExtra(Intent.EXTRA_TEXT, currentMovie);
        startActivity(intentStartDetailMovie);

    }
    @Override
    protected void onStop() {
        super.onStop();
       // Log.i(TAG, "MainActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  Log.i(TAG, "MainActivity onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
      //  Log.i(TAG, "MainActivity onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  Log.i(TAG, "MainActivity onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
      //  Log.i(TAG, "MainActivity onStart");
    }
}
