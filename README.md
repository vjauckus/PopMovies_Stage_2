# PopMovies_Stage_2
Project for the Udactity Android Developer Nanodegree (https://www.udacity.com) by Google.

This is the final stage of Popular Movies project with additional functionality. The app allows users

- to fetch movie data from the Internet with theMovieDB API.
- change the sort order via settings: popular or by highst-rated.
- to see detailes of movie:
  original title,
  a plot synopsis,
  user rating.
- view and play trailes.
- read reviews for that movie in the movie details screen by clicking on the options menu on the top right.
- to share favourite movie.
- adds (removes) movie to (from) users favourites list and create a database for favourite movies, that will be updated.

Internet is required for first launch of the application and when the filter type is changed.

Maintaining state even after rotation. After the loss of the Internet, the favourites remain on the same status.

Please see here what the screens look like:

![Main View](https://github.com/vjauckus/PopMovies_Stage_2/blob/master/Main_View.png)


![Movies Details](https://github.com/vjauckus/PopMovies_Stage_2/blob/master/Movie_Details.png)

To run the app you need an api key. You can create one here: The Movie Database (TMDb) API (https://developers.themoviedb.org/3/getting-started/introduction).

Save the key as THE_MOVIE_DB_API_TOKEN in api_key.xml file (https://github.com/vjauckus/PopMovies_Stage_2/blob/master/app/src/main/res/values/api_key.xml).