/*
 * Copyright (C) 2017 VJauckus
 *
 */
package com.veronika.android.popmovies.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.veronika.android.popmovies.R;
import com.veronika.android.popmovies.adapters.TrailerAdapter;
import com.veronika.android.popmovies.data.MoviesContract;
import com.veronika.android.popmovies.interfaces.FetchTrailerInterface;
import com.veronika.android.popmovies.loaders.FavouritesLoader;
import com.veronika.android.popmovies.loaders.TrailerLoader;
import com.veronika.android.popmovies.models.AndroidMovie;
import com.veronika.android.popmovies.models.Trailer;
import com.veronika.android.popmovies.utilities.NetworkUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This class used to define and present details of current movie
 */

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler, FetchTrailerInterface, View.OnClickListener{

  //  private static final String TAG = DetailActivity.class.getSimpleName();
   // private TrailerLoader trailerLoader;
    private TrailerAdapter mTrailerAdapter;
    private boolean isFavourite = false;

     @BindView(R.id.movie_title_detail)TextView mMovieTitle;
     @BindView(R.id.tv_release_date) TextView mReleaseDate;
     @BindView(R.id.tv_vote_average) TextView mVoteAverage;
     @BindView(R.id.tv_detail_overview) TextView mMovieOverview;
     @BindView(R.id.movie_image_detail) ImageView mMovieImage;
    @BindView(R.id.favorite_button) Button mFavoriteButton;


    @BindView(R.id.rv_trailers_recycler_view) RecyclerView recyclerViewTrailer;
    @BindView(R.id.trailer_label) TextView mTrailerLabel;
    private ArrayList<Trailer> mTrailers = new ArrayList<>();

    private AndroidMovie mCurrentMovie;
    private String mMovieId;
    private static final int FAVORITES_LOADER = 11;
    private LoaderManager.LoaderCallbacks<Cursor> checkFavoritesCallback;


    private LinearLayoutManager layoutManagerTrailer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
      //  Log.i(TAG, "DetailActivity onCreate");


        if(savedInstanceState == null || !savedInstanceState.containsKey("moviesDetailsData")){

            Intent intentThatStartThisActivity = getIntent();
            this.mCurrentMovie = intentThatStartThisActivity.getParcelableExtra(Intent.EXTRA_TEXT);

        }
        else {
            // Restore some state after rotation or leave the app
           this.mCurrentMovie = savedInstanceState.getParcelable("moviesDetailsData");

        }

        //recyclerViewTrailer = (RecyclerView) findViewById(R.id.rv_trailers_recycler_view);
        //for trailer
        layoutManagerTrailer = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerViewTrailer.setLayoutManager(layoutManagerTrailer);

        recyclerViewTrailer.setHasFixedSize(true);

        //Populating views with details of movie data, if movie is available

        if(mCurrentMovie != null && NetworkUtils.checkConnection(this)){

            String voteString = mCurrentMovie.getVoteAverage().toString()+"/10";


            mMovieTitle.setText(mCurrentMovie.getTitle());
            mReleaseDate.setText(mCurrentMovie.getReleaseDate());
            mVoteAverage.setText(voteString);
            mMovieOverview.setText(mCurrentMovie.getOverview());
            String picassoPathUrl = NetworkUtils.buildUrlPicasso(mCurrentMovie.getMovieImage());
            //Picasso.with(context).load(url).into(view);
            Picasso.with(mMovieImage.getContext()).load(picassoPathUrl)
                    .placeholder(R.drawable.user_placeholder)
                    .error(R.drawable.user_placeholder_error)
                    .into(mMovieImage);

            //get Movie data
            mMovieId = Integer.toString(mCurrentMovie.getMovieId());

           if (savedInstanceState == null || !savedInstanceState.containsKey("trailerList")  ||
                   !savedInstanceState.containsKey("favoriteState") ||
                   !savedInstanceState.containsKey("reviewsList")){

              // Log.i(TAG, "I am trying to get trailers from HttpUrl for MovieId: "+mMovieId);
               //get Trailer data
               loadTrailerData();

           }
          else {
               //restore the data after rotation
               mMovieId = savedInstanceState.getString("movieId");
              // mReviewsString = savedInstanceState.getString("reviewsString");
               mTrailers = savedInstanceState.getParcelableArrayList("trailerList");

               isFavourite = savedInstanceState.getBoolean("favoriteState");

               setTrailerData();

           }
                   //get favorite state and set text on FavoriteButton
            if(isFavourite){
                mFavoriteButton.setText(this.getResources().getString(R.string.remove_from_favourite));

            }
            else{
                mFavoriteButton.setText(this.getResources().getString(R.string.add_to_favourite));
            }
            mFavoriteButton.setOnClickListener(this);
            //Init Loader for Favorites

            startLoaderCallback();
            getSupportLoaderManager().initLoader(FAVORITES_LOADER, null, checkFavoritesCallback);

        }
       /* else {

           // Log.v(TAG, "No details data are available");
        } */

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable("moviesDetailsData", mCurrentMovie);
        //outState.putString(DATA, DATA);
        outState.putString("movieId", mMovieId);
        // outState.putString("reviewsString",  mReviewsString);
        outState.putBoolean("favoriteState", isFavourite);
        outState.putParcelableArrayList("trailerList", mTrailers);


        super.onSaveInstanceState(outState);
    }

    private  void startLoaderCallback(){

        checkFavoritesCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {

                return new FavouritesLoader(DetailActivity.this, String.valueOf(mCurrentMovie.getMovieId()));
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       //          updateFavoriteButtons();
                if (data.getCount() >0 ){
                    mFavoriteButton.setText(getResources().getString(R.string.remove_from_favourite));
                    isFavourite = true;
                }
                else{
                    mFavoriteButton.setText(getResources().getString(R.string.add_to_favourite));
                    isFavourite = false;
                }

            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };
    }

    //Remove data of movie from database
    private void removeFromFavoriteDB(){
        String mUri = MoviesContract.MoviesEntry.CONTENT_URI +"/"+mMovieId;
        Uri myUri = Uri.parse(mUri);
        try {
            getContentResolver().delete(myUri, null, null);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.remove_as_favourite_success), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.remove_as_favourite_error), Toast.LENGTH_SHORT).show();
         //   Log.d(TAG,"Error deleting movie. "+e.getMessage() );
        }



       // Log.v(TAG, "Remove data of movie from database");
    }

    //Insert data of movie in database
    private void addToFavoriteFavoriteDB(){

      //  Log.v(TAG, "Insert data of movie in database");
        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesContract.MoviesEntry.MOVIE_ID, mCurrentMovie.getMovieId());
        contentValues.put(MoviesContract.MoviesEntry.MOVIE_TITLE, mCurrentMovie.getTitle());
        contentValues.put(MoviesContract.MoviesEntry.MOVIE_OVERVIEW, mCurrentMovie.getOverview());
        contentValues.put(MoviesContract.MoviesEntry.MOVIE_RELEASE_DATE, mCurrentMovie.getReleaseDate());
        contentValues.put(MoviesContract.MoviesEntry.MOVIE_RATING, mCurrentMovie.getVoteAverage());
        contentValues.put(MoviesContract.MoviesEntry.MOVIE_POSTER_PATH, mCurrentMovie.getMovieImage());
        //Insert the content values using ContentResolver
        try{
            getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI,contentValues);
           Toast.makeText(getApplicationContext(), getResources().getString(R.string.insert_as_favourite_success), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
           Toast.makeText(getApplicationContext(), getResources().getString(R.string.insert_as_favourite_error), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This function will call, when Favorite button is clicked
     * @param view The View that was clicked
     */
    @Override
    public void onClick(View view) {
        updateFavoriteButtons();
    }

    private void updateFavoriteButtons() {

            if(!isFavourite){

                addToFavoriteFavoriteDB();
                mFavoriteButton.setText(this.getResources().getString(R.string.remove_from_favourite));
                isFavourite = true;
              //  Toast.makeText(DetailActivity.this, "Insert data of movie in database", Toast.LENGTH_SHORT).show();
             //   Log.d(TAG,"Insert data of movie in database");

            }
            else{
                removeFromFavoriteDB();
                mFavoriteButton.setText(this.getResources().getString(R.string.add_to_favourite));
                isFavourite = false;
              //  Toast.makeText(DetailActivity.this, "Remove data of movie from database", Toast.LENGTH_SHORT).show();
              //  Log.d(TAG, "Remove data of movie from database");
            }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

   //     recyclerViewReviews = (RecyclerView) findViewById(R.id.rv_reviews_recycler_view);
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void loadTrailerData(){

      //  Log.i(TAG, "I am trying to load trailer data from API");

       try {

       TrailerLoader trailerLoader = new TrailerLoader(this);
        trailerLoader.execute(mMovieId);

        }
        catch (Exception e){

            Toast.makeText(getApplicationContext(), getResources().getString(R.string.trailer_loading_error), Toast.LENGTH_SHORT).show();
         //   Log.d(TAG, "Loading of trailers does not work proper. PLease try again");
            e.printStackTrace();
        }

    }

    private void setTrailerData(){

        mTrailerAdapter = new TrailerAdapter(this, mTrailers, this);
     //   recyclerViewTrailer = (RecyclerView) findViewById(R.id.rv_trailers_recycler_view);
        recyclerViewTrailer.setAdapter(mTrailerAdapter);

    }

    /**
     * This function is called if a trailer is clicked
     * @param currentTrailer The current trailer for playing the video that is clicked
     */
    @Override
    public void onClick(Trailer currentTrailer) {

        //Toast.makeText(this, currentTrailer.getName() + ". Image was clicked: "+currentTrailer.getVideoKey(), Toast.LENGTH_SHORT).show();
      //  Log.v(TAG, "I am playing a video");
        String videoKey = currentTrailer.getVideoKey();

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+videoKey));
            //Check if youtube app exists on device
            if (intent.resolveActivity(getPackageManager() )== null){
                //If playing on youtube is not possible, use browser
                //http://www.youtube.com/watch?v=r9-DM9uBtVI
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+videoKey));
            }
            startActivity(intent);
        }
        catch (Exception e){

            e.printStackTrace();
           Toast.makeText(getApplicationContext(), getResources().getString(R.string.playing_video_error), Toast.LENGTH_SHORT).show();
          //  Log.d(TAG,"Playing a video is not possible. PLease check your internet connection and try again");
        }
    }

    @Override
    public void fetchCallbackTrailer(ArrayList<Trailer> trailersArrayList) {
        //Here we received the results form async class
        mTrailers = trailersArrayList;

       // movieAdapter.setMovieList(movieArrayList);
     //   Log.v(TAG, "RESULTS got in DetailActivity delivered from TrailerLoader: " + mTrailers.toString());
        if (mTrailers != null && mTrailers.size()!=0){

            setTrailerData();
         //   mTrailerAdaper.notifyDataSetChanged();

        }
        else {
            mTrailerLabel.setVisibility(View.GONE);
          //  Toast.makeText(this, "There are no trailers available", Toast.LENGTH_SHORT).show();
           // Log.d(TAG, "There are no trailers available");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

         /*
     * Normally, calling setDisplayHomeAsUpEnabled(true) (we do so in onCreate here) as well as
     * declaring the parent activity in the AndroidManifest is all that is required to get the
     * up button working properly. However, in this case, we want to navigate to the previous
     * screen the user came from when the up button was clicked, rather than a single
     * designated Activity in the Manifest.
     *
     * We use the up button's ID (android.R.id.home) to listen for when the up button is
     * clicked and then call onBackPressed to navigate to the previous Activity when this
     * happens.
     */
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else if(id == R.id.action_show_reviews){
            Class destinationClass = ReviewsActivity.class;
            Intent intentStartReviewsActivity = new Intent(this, destinationClass);
          //  Toast.makeText(this.getApplication(), "Open reviews activity", Toast.LENGTH_SHORT).show();
            intentStartReviewsActivity.putExtra(Intent.EXTRA_TEXT, mMovieId);
            startActivity(intentStartReviewsActivity);

        }
        else if(id == R.id.action_share){
            Intent shareIntent = createShareMovieIntent();
            if (shareIntent != null){
                startActivity(shareIntent);
                return true;
            }
            else{
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.sharing_error), Toast.LENGTH_SHORT).show();
                return false;
            }


        }

        return super.onOptionsItemSelected(item);
    }

    private Intent createShareMovieIntent(){

        String myMovieTip = "\n"+this.getApplicationContext().getResources().getString(R.string.my_movie_tip)+"\n\n"+mCurrentMovie.getTitle()+"\n\n";

       // sAux = "https://play.google.com/store/apps/details?id=Orion.Soft \n\n";
        try {
            if (mTrailers != null && mTrailers.size()!= 0){
                String myMovieTipVideo = myMovieTip + "http://www.youtube.com/watch?v="+mTrailers.get(0).getVideoKey();
                Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                        .setType("text/plain")
                        .setText(myMovieTipVideo)
                        .getIntent();
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, this.getApplicationContext().getResources().getString(R.string.my_movie_tip));
                return shareIntent;

            }
            else{
                Intent sendIntent = new Intent();
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, myMovieTip);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, this.getApplicationContext().getResources().getString(R.string.my_movie_tip));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return sendIntent;
            }


    }
    catch (Exception e){
        e.printStackTrace();
        return null;
    }

    }
    @Override
    protected void onStop() {
        super.onStop();
      //  Log.i(TAG, "DetailActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // Log.i(TAG, "DetailActivity onDestroy");
    }
   @Override
    protected void onPause()
    {
        super.onPause();
   //     recyclerViewReviews = (RecyclerView) findViewById(R.id.rv_reviews_recycler_view);
       // Log.i(TAG, "DetailActivity onPause");

    }

    @Override
    protected void onResume()
    {
        super.onResume();
      //  Log.i(TAG, "DetailActivity onResume");
        getSupportLoaderManager().restartLoader(FAVORITES_LOADER, null, checkFavoritesCallback);

        // restore RecyclerView state
        /*      if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);

            if (mReviews != null && mReviews.size()!=0){
                recyclerViewReviews.getLayoutManager().onRestoreInstanceState(listState);
            }

        }
        */
    }


    @Override
    protected void onStart() {
        super.onStart();
      //  Log.i(TAG, "DetailActivity onStart");
    }

}
