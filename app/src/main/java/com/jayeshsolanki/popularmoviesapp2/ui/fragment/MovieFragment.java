package com.jayeshsolanki.popularmoviesapp2.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jayeshsolanki.popularmoviesapp2.R;
import com.jayeshsolanki.popularmoviesapp2.model.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieFragment extends Fragment {

    private Movie movie;

    View view;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("movie")) {
            movie = intent.getExtras().getParcelable("movie");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_movie, container, false);

        ButterKnife.bind(this, view);

        bindData(movie);

        return view;
    }

    public void bindData(Movie movie) {
        releaseYear.setText(movie.getReleaseDate().substring(0, 4));
        title.setText(movie.getTitle());
        overview.setText(movie.getOverview());
        voteAverage.setRating((float) movie.getVoteAverage().doubleValue());
    }

}
