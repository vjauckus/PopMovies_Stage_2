/*
 * Copyright (C) 2017 VJauckus
 *
 */
package com.veronika.android.popmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class MoviesProvider extends ContentProvider {

  //  public static final String LOG_TAG = ContentProvider.class.getSimpleName();
    //Create a private member variable to hold an instance of Movie Database which is used to access
    // the database itself
    private MoviesDBHelper mMoviesDBHelper;

    //Codes for the URI Matcher that represents different queries
    private static final int MOVIE_CODE = 100;
    private static final int MOVIE_CODE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    //static buildUriMatcher Method that associetes URI's with their int match
    public static UriMatcher buildUriMatcher()

    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        // All paths to the UriMatcher have a corresponding code to return
        // when a match is found (the Codes above)
        // This URI is content://com.veronika.android.popmovies/movies
        matcher.addURI(authority, MoviesContract.MoviesEntry.MOVIES_TABLE_NAME, MOVIE_CODE);
        //The "/# " signifies to the UriMatcher that if ../movies followed by ANY number, in our case MovieID
        //This Uri will look like this content://com.veronika.android.popmovies/movies/306
        matcher.addURI(authority,MoviesContract.MoviesEntry.MOVIES_TABLE_NAME + "/#", MOVIE_CODE_WITH_ID);
        //return matcher
        return matcher;
    }

    @Override
    public boolean onCreate() {

        mMoviesDBHelper = new MoviesDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor retCursor;

        switch (sUriMatcher.match(uri)){
            case MOVIE_CODE:{

                retCursor = mMoviesDBHelper.getReadableDatabase().query(
                        MoviesContract.MoviesEntry.MOVIES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                return retCursor;
            }

            case MOVIE_CODE_WITH_ID:{

                retCursor = mMoviesDBHelper.getReadableDatabase().query(
                        MoviesContract.MoviesEntry.MOVIES_TABLE_NAME,
                        projection,
                        MoviesContract.MoviesEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                return retCursor;
            }
            default:{
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
            }
        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        //get access to the movie database to insert values
        final SQLiteDatabase db = mMoviesDBHelper.getWritableDatabase();

        int numDeleted;

        switch (sUriMatcher.match(uri)){

            case MOVIE_CODE:
                numDeleted = db.delete(MoviesContract.MoviesEntry.MOVIES_TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_CODE_WITH_ID:
                numDeleted = db.delete(MoviesContract.MoviesEntry.MOVIES_TABLE_NAME,
                        MoviesContract.MoviesEntry.MOVIE_ID + " = ?",
                        new String[]{
                                String.valueOf(ContentUris.parseId(uri))
                        });
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: "+ uri);

        }
        if(numDeleted > 0){
            try {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            catch (NullPointerException ex){
                ex.printStackTrace();
            }

        }

        return numDeleted;
    }


    @Nullable
    @Override
    //Insert a single new row of data
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        //get access to the movie database to insert values
        final SQLiteDatabase db = mMoviesDBHelper.getWritableDatabase();

        Uri returnUri; // Uri to be returned
        long _id;

        switch (sUriMatcher.match(uri)){

            case MOVIE_CODE:
                //inserting values into movie table unless it is already contained in the db
                _id = db.insertWithOnConflict(MoviesContract.MoviesEntry.MOVIES_TABLE_NAME, null, values,
                        SQLiteDatabase.CONFLICT_ABORT);
                if (_id > 0){
                    returnUri = MoviesContract.MoviesEntry.buildMovieUri(_id);
                }
                else {
                    throw new android.database.SQLException("Failed to insert data uri: "+ uri+ "in database: "+db+", the movie is already exists in your favorites");
                }
                break;


            default:

                throw new UnsupportedOperationException("Unknown Uri: "+ uri);

        }
        try {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        catch (NullPointerException ex){
            ex.printStackTrace();
        }


        return returnUri;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        //get access to the movie database to update values
     return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match){
            case MOVIE_CODE:{
                return MoviesContract.MoviesEntry.CONTENT_DIR_TYPE;

            }
            case MOVIE_CODE_WITH_ID:{
                return MoviesContract.MoviesEntry.CONTENT_ITEM_TYPE;
            }
            default:{
                throw new UnsupportedOperationException("Unknown Uri: " + uri);

            }

        }

    }
}
