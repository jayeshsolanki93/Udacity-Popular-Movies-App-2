package com.jayeshsolanki.popularmoviesapp1.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jayeshsolanki.popularmoviesapp1.BuildConfig;
import com.jayeshsolanki.popularmoviesapp1.PopularMoviesApplication;
import com.jayeshsolanki.popularmoviesapp1.R;
import com.jayeshsolanki.popularmoviesapp1.model.Movie;
import com.jayeshsolanki.popularmoviesapp1.model.MoviesResponse;
import com.jayeshsolanki.popularmoviesapp1.rest.MovieService;
import com.jayeshsolanki.popularmoviesapp1.ui.adapter.MovieListAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class MovieListFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;

    private List<Movie> mMovies = new ArrayList<>();

    @BindView(R.id.recyclerView_movies)
    protected RecyclerView mRecyclerView;

    protected MovieListAdapter mAdapter;

    @Inject
    Retrofit retrofit;

    @Inject
    SharedPreferences prefs;

    public MovieListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((PopularMoviesApplication) getActivity().getApplication())
                .getDataComponent().inject(MovieListFragment.this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        ButterKnife.bind(this, view);

        setupRecyclerView();

        return view;
    }

    void setupRecyclerView() {
        int columnsCount = calculateNoOfColumns(getContext());
        GridLayoutManager glm = new GridLayoutManager(getContext(), columnsCount);
        mRecyclerView.setLayoutManager(glm);

        mAdapter =  new MovieListAdapter(getActivity(), mMovies);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    public int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    private void updateMovies() {
        MovieService movieService = retrofit.create(MovieService.class);
        String apiKey = BuildConfig.API_KEY;

        String sort = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));
        Timber.d(sort + " ");
        Call<MoviesResponse> moviesResponseCall = movieService.getMovies(sort, apiKey);
        moviesResponseCall.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response != null && response.body() != null) {
                    List<Movie> movies = response.body().getResults();
                    mAdapter.setAdapterData(movies);
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                showSnackBar(getString(R.string.internet_err_msg));
                Timber.d("Error code " +  t.toString());
            }
        });
    }

    void showSnackBar(String msg) {
        Snackbar.make(mRecyclerView, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new IllegalStateException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Timber.d("onCreateOptionsMenu called");
        inflater.inflate(R.menu.sort_menu, menu);

        String sort = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular));
        Timber.d(sort + "");
        if ("popular".equals(sort)) {
            menu.findItem(R.id.sort_popular).setChecked(true);
        } else if ("top_rated".equals(sort)) {
            menu.findItem(R.id.sort_top_rated).setChecked(true);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Timber.d("on prepare options menu called");
        super.onPrepareOptionsMenu(menu);
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

            updateMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Uri uri);
    }

}
