/*
 * Copyright (C) 2017 VJauckus
 *
 */
package com.veronika.android.popmovies.interfaces;

import com.veronika.android.popmovies.models.Trailer;

import java.util.ArrayList;

/**
 * That Interface will be used as callback for trailers
 */

public interface FetchTrailerInterface {

    void fetchCallbackTrailer(ArrayList<Trailer> trailerArrayList);
}
