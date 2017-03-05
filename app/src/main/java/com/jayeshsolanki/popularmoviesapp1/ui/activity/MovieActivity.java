package com.jayeshsolanki.popularmoviesapp1.ui.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jayeshsolanki.popularmoviesapp1.R;
import com.jayeshsolanki.popularmoviesapp1.ui.fragment.MovieFragment;

public class MovieActivity extends AppCompatActivity
        implements MovieFragment.OnFragmentInteractionListener {

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

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
