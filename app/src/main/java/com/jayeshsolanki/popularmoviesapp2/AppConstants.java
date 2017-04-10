package com.jayeshsolanki.popularmoviesapp2;

public class AppConstants {

    public static final String API_KEY = BuildConfig.API_KEY;

    public static final String API_URL = "https://api.themoviedb.org/3/";
    public static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w396";

    public static final String MOVIE_INTENT = "movie";

    public static final String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";

    public static final String YOUTUBE_VIDEO_BASE_URL = "https://www.youtube.com/watch?v=";

    private AppConstants() {
        throw new IllegalAccessError("Utility class");
    }

}
