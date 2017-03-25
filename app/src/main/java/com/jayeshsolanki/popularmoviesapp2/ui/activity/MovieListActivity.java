package com.jayeshsolanki.popularmoviesapp2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jayeshsolanki.popularmoviesapp2.R;
import com.jayeshsolanki.popularmoviesapp2.model.Movie;
import com.jayeshsolanki.popularmoviesapp2.ui.fragment.MovieFragment;
import com.jayeshsolanki.popularmoviesapp2.ui.fragment.MovieListFragment;

import timber.log.Timber;

public class MovieListActivity extends BaseActivity
        implements MovieListFragment.MovieSelectedListener {

    private boolean mDualPaneMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        setupToolbar();

        mDualPaneMode = findViewById(R.id.movie_container) != null;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_list_container, new MovieListFragment())
                    .commit();
        }
    }

    @Override
    public void onMovieSelected(Movie movie, View view) {
        Timber.d(String.format("Movie selected: '%s'", movie.getTitle()));

        if (mDualPaneMode) {
            replaceMovieFragment(movie);
        } else {
            startMovieActivity(movie);
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setTitle("Popular Movies");
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    private void replaceMovieFragment(Movie movie) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_container, MovieFragment.newInstance(movie))
                .commit();
    }

    private void startMovieActivity(Movie movie) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }

}
