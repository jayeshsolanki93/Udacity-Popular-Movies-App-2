package com.jayeshsolanki.popularmoviesapp2.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jayeshsolanki.popularmoviesapp2.AppConstants;
import com.jayeshsolanki.popularmoviesapp2.R;
import com.jayeshsolanki.popularmoviesapp2.model.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieFragment extends Fragment {

    private Movie movie;

    @BindView(R.id.toolbar_movie)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.backdrop_movie)
    ImageView backdrop;

    @BindView(R.id.release_year)
    TextView releaseYear;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.overview)
    TextView overview;

    @BindView(R.id.vote_average)
    RatingBar voteAverage;

    public MovieFragment() {
        // Required empty public constructor
    }

    public static MovieFragment newInstance(Movie movie) {
        MovieFragment fragment = new MovieFragment();
        fragment.movie = movie;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            movie = savedInstanceState.getParcelable("movie");
        }

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("movie")) {
            movie = intent.getExtras().getParcelable("movie");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        setupToolbar(toolbar);

        bindData(movie);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("movie", movie);
    }

    public void setupToolbar(Toolbar toolbar) {
        toolbar.setTitle(movie.getTitle());

        collapsingToolbar.setTitle(movie.getTitle());

        String backDropUrl = AppConstants.BASE_IMAGE_URL + movie.getBackdropPath();
        Glide.with(this).load(backDropUrl).into(backdrop);
    }

    public void bindData(Movie movie) {
        releaseYear.setText(movie.getReleaseDate().substring(0, 4));
        title.setText(movie.getTitle());
        overview.setText(movie.getOverview());
        voteAverage.setRating((float) movie.getVoteAverage().doubleValue());
    }

}
