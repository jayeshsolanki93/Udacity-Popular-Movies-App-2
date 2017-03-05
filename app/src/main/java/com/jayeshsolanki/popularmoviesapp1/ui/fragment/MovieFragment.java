package com.jayeshsolanki.popularmoviesapp1.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jayeshsolanki.popularmoviesapp1.R;
import com.jayeshsolanki.popularmoviesapp1.model.Movie;
import com.jayeshsolanki.popularmoviesapp1.ui.adapter.MovieAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private Movie movie;

    View view;

    @BindView(R.id.toolbar_movie)
    Toolbar mToolbar;

    @BindView(R.id.recyclerview_movie)
    RecyclerView mRecyclerView;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getActivity().getIntent().getExtras();
        movie = args.getParcelable("movie");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_movie, container, false);

        ButterKnife.bind(this, view);

        setupToolbar();

        setupRecyclerView();

        return view;
    }

    public void setupToolbar() {
        mToolbar.setNavigationIcon(R.drawable.back_arrow);
        mToolbar.setTitle(movie.getTitle());

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    public void setupRecyclerView() {
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        MovieAdapter adapter = new MovieAdapter(getContext(), movie, view);
        mRecyclerView.setAdapter(adapter);

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
