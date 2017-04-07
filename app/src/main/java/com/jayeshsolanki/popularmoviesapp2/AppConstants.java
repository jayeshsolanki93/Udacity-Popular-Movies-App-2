package com.jayeshsolanki.popularmoviesapp2;

public class AppConstants {

    public static final String API_KEY = BuildConfig.API_KEY;

    public static final String API_URL = "https://api.themoviedb.org/3/";
    public static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w396";

    public static final String MOVIE_INTENT = "movie";

    private AppConstants() {
        throw new IllegalAccessError("Utility class");
    }

}
