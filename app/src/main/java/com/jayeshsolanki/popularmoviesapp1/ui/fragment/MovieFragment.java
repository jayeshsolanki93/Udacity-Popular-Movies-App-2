package com.jayeshsolanki.popularmoviesapp1.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jayeshsolanki.popularmoviesapp1.R;
import com.jayeshsolanki.popularmoviesapp1.model.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private Movie movie;

    View view;

    @BindView(R.id.release_year)
    TextView releaseYear;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.overview)
    TextView overview;

    @BindView(R.id.vote_average)
    TextView voteAverage;

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
        voteAverage.setText(String.valueOf(movie.getVoteAverage()) + "/10");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new IllegalStateException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
