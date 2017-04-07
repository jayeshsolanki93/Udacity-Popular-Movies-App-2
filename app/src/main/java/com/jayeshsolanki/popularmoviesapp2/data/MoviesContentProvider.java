package com.jayeshsolanki.popularmoviesapp2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jayeshsolanki.popularmoviesapp2.data.MoviesContract.*;

public class MoviesContentProvider extends ContentProvider {

    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    private MoviesDbHelper mMoviesDbHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMoviesDbHelper = new MoviesDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mMoviesDbHelper.getReadableDatabase();
        final SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        qb.setTables(MoviesEntry.TABLE_NAME);
        switch (match) {
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                if (id != null) {
                    qb.appendWhere(MoviesEntry.COLUMN_ID + " = " + id);
                }
                break;
            case MOVIES:
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES:
                long id = db.insert(MoviesEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MoviesEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int moviesDeleted = 0;

        switch (match) {
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                if (id != null) {
                    moviesDeleted = db.delete(MoviesEntry.TABLE_NAME, "id=?", new String[]{id});
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (moviesDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
