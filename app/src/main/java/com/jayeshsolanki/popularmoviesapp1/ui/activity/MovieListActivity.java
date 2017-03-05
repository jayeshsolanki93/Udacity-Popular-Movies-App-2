package com.jayeshsolanki.popularmoviesapp1.ui.activity;

import android.net.Uri;
import android.os.Bundle;

import com.jayeshsolanki.popularmoviesapp1.R;
import com.jayeshsolanki.popularmoviesapp1.ui.fragment.MovieListFragment;

public class MovieListActivity extends BaseActivity
        implements MovieListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_list_container, new MovieListFragment())
                    .commit();
        }
    }

    @Override
    public void onListFragmentInteraction(Uri uri) {
    }
}
