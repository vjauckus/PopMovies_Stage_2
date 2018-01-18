/*
 * Copyright (C) 2017 VJauckus
 *
 */
package com.veronika.android.popmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**The Class stored the user setting of sort method
 */

public class UserPreferences {

    private static String mUserSortBy="";

    public static void setUserPrefferedSort(String value, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("SORT_USER_PREF", value);
        mUserSortBy = value;
        editor.apply();

    }

    public static String getUserPreferredSort(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("SORT_USER_PREF", mUserSortBy);
    }
}
