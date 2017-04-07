package com.jayeshsolanki.popularmoviesapp2.util;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.jayeshsolanki.popularmoviesapp2.data.MoviesContentProvider;
import com.jayeshsolanki.popularmoviesapp2.data.MoviesContract;
import com.jayeshsolanki.popularmoviesapp2.data.MoviesContract.*;
import com.jayeshsolanki.popularmoviesapp2.model.Movie;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ContentProviderHelper {

    private ContentProviderHelper() {}

    public static List<Movie> getMovieListFromDb(Activity activity, Uri uri) {
        List<Movie> movies = new ArrayList<>();

        Cursor data = null;
        try {
            data = activity.getContentResolver().query(uri, null, null, null, null);
            if (data != null && data.moveToFirst()) {
                do {
                    int id = data.getInt(data.getColumnIndex(MoviesEntry.COLUMN_ID));
                    String title = data.getString(data.getColumnIndex(MoviesEntry.COLUMN_TITLE));
                    String overview = data.getString(data.getColumnIndex(MoviesEntry.COLUMN_OVERVIEW));
                    String releaseDate = data.getString(data.getColumnIndex(MoviesEntry.COLUMN_RELEASE_DATE));
                    Double voteAverage = data.getDouble(data.getColumnIndex(MoviesEntry.COLUMN_VOTE_AVERAGE));
                    String posterPath = data.getString(data.getColumnIndex(MoviesEntry.COLUMN_POSTER_PATH));
                    String backDropPath = data.getString(data.getColumnIndex(MoviesEntry.COLUMN_BACKDROP_PATH));

                    Movie movie = new Movie(id, title, overview, releaseDate, voteAverage, posterPath, backDropPath);
                    movies.add(movie);
                } while (data.moveToNext());
            }
        } catch (Exception e) {
            Timber.e("Failed to asynchronously load data.");
            e.printStackTrace();
        } finally {
            if (data != null) {
                data.close();
            }
        }
        return movies;
    }

    public static boolean isMovieInDb(Activity activity, String id) {
        Uri uri = Uri.parse(String.format("%s/%s", MoviesContract.MoviesEntry.CONTENT_URI, id));
        List<Movie> movies = getMovieListFromDb(activity, uri);
        for (Movie movie : movies) {
            if (movie.getId().equals(Integer.parseInt(id))) {
                return true;
            }
        }
        return false;
    }

    public static Movie getMovieFromDb(Activity activity, String id) {
        Uri uri = Uri.parse(String.format("%s/%s", MoviesContract.MoviesEntry.CONTENT_URI, id));
        List<Movie> movies = getMovieListFromDb(activity, uri);
        for (Movie movie : movies) {
            if (movie.getId().equals(Integer.parseInt(id))) {
                return movie;
            }
        }
        return null;
    }

    public static Uri insertMoviesInDb(Activity activity, Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(MoviesContract.MoviesEntry.COLUMN_ID, movie.getId());
        cv.put(MoviesContract.MoviesEntry.COLUMN_TITLE, movie.getTitle());
        cv.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, movie.getOverview());
        cv.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        cv.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        cv.put(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        cv.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        return activity.getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, cv);
    }
    public static int deleteMoviesFromDb(Activity activity, String id) {
        Uri uri = Uri.parse(String.format("%s/%s", MoviesContract.MoviesEntry.CONTENT_URI, id));
        return activity.getContentResolver().delete(uri, null, null);

    }
}
