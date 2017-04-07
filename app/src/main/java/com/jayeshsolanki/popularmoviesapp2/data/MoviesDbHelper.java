package com.jayeshsolanki.popularmoviesapp2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jayeshsolanki.popularmoviesapp2.data.MoviesContract.*;

public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_MOVIE_TABLE =
            "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +
            MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MoviesEntry.COLUMN_ID + " INTEGER NOT NULL, " +
            MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
            MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
            MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
            MoviesEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
            MoviesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
            MoviesEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL" +
            "); ";

    private static final String SQL_DROP_MOVIE_TABLE =
            "DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_MOVIE_TABLE);
        onCreate(db);
    }
}