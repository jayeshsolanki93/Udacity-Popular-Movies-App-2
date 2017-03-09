package com.jayeshsolanki.popularmoviesapp1.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.jayeshsolanki.popularmoviesapp1.PopularMoviesApp;
import com.jayeshsolanki.popularmoviesapp1.R;
import com.jayeshsolanki.popularmoviesapp1.model.Movie;
import com.jayeshsolanki.popularmoviesapp1.model.MoviesResponse;
import com.jayeshsolanki.popularmoviesapp1.rest.MovieService;
import com.jayeshsolanki.popularmoviesapp1.ui.adapter.MovieListAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class MovieListFragment extends Fragment {

    private ArrayList<Movie> mMovies = new ArrayList<>();

    @BindView(R.id.recyclerView_movies)
    protected RecyclerView mRecyclerView;

    protected MovieListAdapter mAdapter;

    @Inject
    Retrofit retrofit;

    @Inject
    SharedPreferences prefs;

    GridLayoutManager mLayoutManager;

    static final String API_KEY = BuildConfig.API_KEY;

    private int pageCount = 1;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem;
    int visibleItemCount;
    int totalItemCount;

    public MovieListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((PopularMoviesApp) getActivity().getApplication())
                .getDataComponent().inject(MovieListFragment.this);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            mMovies = savedInstanceState.getParcelableArrayList("mMovies");
            pageCount = savedInstanceState.getInt("pageCount");
            previousTotal = savedInstanceState.getInt("previousTotal");
            firstVisibleItem = savedInstanceState.getInt("firstVisibleItem");
            totalItemCount = savedInstanceState.getInt("totalItemCount");
            loading = savedInstanceState.getBoolean("loading");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("mMovies", mMovies);
        outState.putInt("pageCount", pageCount);
        outState.putInt("previousTotal", previousTotal);
        outState.putInt("firstVisibleItem", firstVisibleItem);
        outState.putInt("totalItemCount", totalItemCount);
        outState.putBoolean("loading", loading);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        ButterKnife.bind(this, view);

        setupRecyclerView();

        if (savedInstanceState == null) {
            updateMovies(pageCount);
        }
        return view;
    }

    void setupRecyclerView() {
        mAdapter =  new MovieListAdapter(getActivity(), mMovies);
        mRecyclerView.setAdapter(mAdapter);

        int columnsCount = calculateNoOfColumns(getContext());
        mLayoutManager = new GridLayoutManager(getContext(), columnsCount);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (loading && totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    pageCount++;
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    updateMovies(pageCount);
                    loading = true;
                }
            }
        });
    }

    public void clearAdapterData() {
        mMovies.clear();
        mAdapter.setAdapterData(mMovies);
    }

    public int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    private void updateMovies(int page) {
        MovieService movieService = retrofit.create(MovieService.class);
        String sort = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));

        Call<MoviesResponse> moviesResponseCall = movieService.getMovies(sort, page, API_KEY);
        moviesResponseCall.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response != null && response.body() != null) {
                    mMovies.addAll(response.body().getResults());
                    final int currSize = mAdapter.getItemCount();
                    if (currSize == 0) {
                        mAdapter = new MovieListAdapter(getContext(), mMovies);
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyItemRangeInserted(currSize, mMovies.size() - 1);
                            }
                        });
                    }
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sort_menu, menu);

        String sort = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular));
        if (sort.equals(getString(R.string.pref_sort_popular))) {
            menu.findItem(R.id.sort_popular).setChecked(true);
        } else if (sort.equals(getString(R.string.pref_sort_top_rated))) {
            menu.findItem(R.id.sort_top_rated).setChecked(true);
        }
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

            clearAdapterData();
            pageCount = 1;
            updateMovies(pageCount);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
