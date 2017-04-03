package com.jayeshsolanki.popularmoviesapp2.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jayeshsolanki.popularmoviesapp2.PopularMoviesApp;
import com.jayeshsolanki.popularmoviesapp2.R;
import com.jayeshsolanki.popularmoviesapp2.model.Movie;
import com.jayeshsolanki.popularmoviesapp2.ui.fragment.MovieFragment;
import com.jayeshsolanki.popularmoviesapp2.ui.fragment.MovieListFragment;

import javax.inject.Inject;

import timber.log.Timber;

public class MovieListActivity extends BaseActivity
        implements MovieListFragment.MovieSelectedListener {

    private boolean mDualPaneMode;

    @Inject
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        ((PopularMoviesApp) this.getApplication())
                .getDataComponent().inject(MovieListActivity.this);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);

        String sort = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular));
        if (sort.equals(getString(R.string.pref_sort_popular))) {
            menu.findItem(R.id.sort_popular).setChecked(true);
        } else if (sort.equals(getString(R.string.pref_sort_top_rated))) {
            menu.findItem(R.id.sort_top_rated).setChecked(true);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getGroupId() == R.id.sort) {
            SharedPreferences.Editor editor = prefs.edit();
            int itemId = item.getItemId();
            if (itemId == R.id.sort_popular) {
                editor.putString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));
            } else if (itemId == R.id.sort_top_rated) {
                editor.putString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_top_rated));
            }
            editor.apply();
            item.setChecked(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            getSupportActionBar().setTitle(R.string.app_name);
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
