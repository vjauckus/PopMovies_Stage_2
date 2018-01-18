/*
 * Copyright (C) 2017 VJauckus
 *
 */
package com.veronika.android.popmovies.interfaces;

import com.veronika.android.popmovies.models.AndroidMovie;

import java.util.ArrayList;

/**
 * This Interface will be used as callback for movie ArrayList
 */

public interface FetchDataInterface {

    void fetchCallback(ArrayList<AndroidMovie> movieArrayList);
}
