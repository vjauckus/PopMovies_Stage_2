/*
 * Copyright (C) 2017 VJauckus
 *
 */
package com.veronika.android.popmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by veronika on 15.11.17.
 */

public class MoviesDBHelper extends SQLiteOpenHelper {

  //  private static final String LOG_TAG = MoviesDBHelper.class.getSimpleName();
    //DB name and version
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public static final String[] MAIN_PROJECTION_LIST = {
            MoviesContract.MoviesEntry._ID,
            MoviesContract.MoviesEntry.MOVIE_ID,
            MoviesContract.MoviesEntry.MOVIE_TITLE
    };

    //Constructor
    public MoviesDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    //Create the Database table to hold movies data while offline
    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MoviesContract.MoviesEntry.MOVIES_TABLE_NAME + "(" +
                MoviesContract.MoviesEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.MoviesEntry.MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                MoviesContract.MoviesEntry.MOVIE_TITLE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.MOVIE_RATING + " REAL NOT NULL, " +
                MoviesContract.MoviesEntry.MOVIE_POSTER_PATH + " TEXT NOT NULL);";

                db.execSQL(SQL_CREATE_MOVIE_TABLE);
       // Log.v(LOG_TAG, "Create Movie DB: "+ SQL_CREATE_MOVIE_TABLE);

    }
  //Upgrading the database when version is changing
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        Log.v(LOG_TAG, "Upgrading database from version " + oldVersion + "to "+ newVersion+ "old data will be destroyed");

        //Drop the table
        db.execSQL("DROP TABLE IF EXISTS "+ MoviesContract.MoviesEntry.MOVIES_TABLE_NAME);

        //delete
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"+ MoviesContract.MoviesEntry.MOVIES_TABLE_NAME +"'");

        //re-create db
        onCreate(db);
    }
}
