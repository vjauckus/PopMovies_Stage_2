/*
 * Copyright (C) 2017 VJauckus
 *
 */
package com.veronika.android.popmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by veronika on 15.11.17.
 */

public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.veronika.android.popmovies";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class MoviesEntry implements BaseColumns{

        //db table name
        public static final String MOVIES_TABLE_NAME = "movies";
        // columns
        public static final String MOVIE_ID = "id";
        public static final String MOVIE_TITLE = "title";
        public static final String MOVIE_OVERVIEW = "overview";
        public static final String MOVIE_RELEASE_DATE = "release_date";
        public static final String MOVIE_RATING = "rating";
        public static final String MOVIE_POSTER_PATH = "poster_path";




        // create Content Uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(MOVIES_TABLE_NAME).build();

        //create Cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                MOVIES_TABLE_NAME;
        //create Cursor of base type directory for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIES_TABLE_NAME;
        //for building URIs on insertion
        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);

        }

    }
}
