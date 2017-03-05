package com.jayeshsolanki.popularmoviesapp1.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jayeshsolanki.popularmoviesapp1.R;
import com.jayeshsolanki.popularmoviesapp1.model.Movie;
import com.jayeshsolanki.popularmoviesapp1.ui.fragment.MovieFragment;
import com.jayeshsolanki.popularmoviesapp1.utils.TmdbUrls;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieActivity extends AppCompatActivity
        implements MovieFragment.OnFragmentInteractionListener {

    Movie movie;

    @BindView(R.id.toolbar_movie)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.backdrop_movie)
    ImageView backdrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("movie")) {
            movie = intent.getExtras().getParcelable("movie");
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, new MovieFragment())
                    .commit();
        }

        setupToolbar(toolbar);
    }

    public void setupToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        collapsingToolbar.setTitle(movie.getTitle());
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));
        setTitle("");
        getSupportActionBar().setTitle(movie.getTitle());

        String backDropUrl = TmdbUrls.BASE_IMAGE_URL + movie.getBackdropPath();
        Glide.with(this).load(backDropUrl).into(backdrop);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

}
