package com.jayeshsolanki.popularmoviesapp2.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.jayeshsolanki.popularmoviesapp2.R;
import com.jayeshsolanki.popularmoviesapp2.ui.fragment.MovieFragment;

public class MovieActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_container, new MovieFragment())
                    .commit();
        }
    }

}
