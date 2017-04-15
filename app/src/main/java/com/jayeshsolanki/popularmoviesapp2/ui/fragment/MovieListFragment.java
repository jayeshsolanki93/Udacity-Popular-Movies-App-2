package com.jayeshsolanki.popularmoviesapp2.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jayeshsolanki.popularmoviesapp2.PopularMoviesApp;
import com.jayeshsolanki.popularmoviesapp2.R;
import com.jayeshsolanki.popularmoviesapp2.data.MoviesContract.MoviesEntry;
import com.jayeshsolanki.popularmoviesapp2.model.Movie;
import com.jayeshsolanki.popularmoviesapp2.model.MoviesResponse;
import com.jayeshsolanki.popularmoviesapp2.rest.MovieService;
import com.jayeshsolanki.popularmoviesapp2.ui.adapter.MovieListAdapter;
import com.jayeshsolanki.popularmoviesapp2.util.ContentProviderHelper;

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

import static com.jayeshsolanki.popularmoviesapp2.AppConstants.API_KEY;

public class MovieListFragment extends Fragment implements MovieListAdapter.MovieClickListener,
        LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final int LOADER_ID = 500;

    private ArrayList<Movie> mMovies = new ArrayList<>();
    private MovieSelectedListener listener;

    @BindView(R.id.recyclerView_movies)
    protected RecyclerView mRecyclerView;

    protected MovieListAdapter mAdapter;

    @Inject
    Retrofit retrofit;

    @Inject
    SharedPreferences prefs;

    GridLayoutManager mLayoutManager;

    private int pageCount = 1;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem;
    int visibleItemCount;
    int totalItemCount;

    SharedPreferences.OnSharedPreferenceChangeListener prefListener;

    public MovieListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((PopularMoviesApp) getActivity().getApplication())
                .getDataComponent().inject(MovieListFragment.this);
        setHasOptionsMenu(true);

        prefListener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                        clearAdapterData();
                        resetScroller();
                        updateMovies(pageCount);
                    }
                };
        if (savedInstanceState != null) {
            mMovies = savedInstanceState.getParcelableArrayList("mMovies");
            pageCount = savedInstanceState.getInt("pageCount");
            previousTotal = savedInstanceState.getInt("previousTotal");
            loading = savedInstanceState.getBoolean("loading");
        }

        String sort = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));
        if (sort.equals(getString(R.string.pref_sort_favorites))) {
            getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        prefs.unregisterOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("mMovies", mMovies);
        outState.putInt("pageCount", pageCount);
        outState.putInt("previousTotal", previousTotal);
        outState.putBoolean("loading", loading);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MovieSelectedListener) {
            listener = (MovieSelectedListener) context;
        } else {
            throw new IllegalStateException(context.toString()
                    + " must implement MovieSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
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
        mAdapter.setListener(this);
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
                    String sort = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));
                    if (!sort.equals(getString(R.string.pref_sort_favorites))) {
                        updateMovies(pageCount);
                        loading = true;
                    }
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

        if (sort.equals(getString(R.string.pref_sort_favorites))) {
            if (getActivity().getSupportLoaderManager().getLoader(LOADER_ID) == null) {
                getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
            } else {
                getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this).forceLoad();
            }
        } else {
            Call<MoviesResponse> moviesResponseCall = movieService.getMovies(sort, page, API_KEY);
            moviesResponseCall.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    if (response != null && response.body() != null) {
                        mMovies.addAll(response.body().getResults());
                        final int currSize = mAdapter.getItemCount();
                        if (currSize == 0) {
                            mAdapter = new MovieListAdapter(getContext(), mMovies);
                            mAdapter.setListener(MovieListFragment.this);
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
                    Timber.d("Error code " + t.toString());
                }
            });
        }
    }

    void showSnackBar(String msg) {
        Snackbar.make(mRecyclerView, msg, Snackbar.LENGTH_LONG).show();
    }

    public void resetScroller() {
        pageCount = 1;
        previousTotal = 0;
        loading = true;
        visibleThreshold = 5;
    }

    @Override
    public void onMovieClick(Movie movie, View view, int position) {
        listener.onMovieSelected(movie, view);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this.getContext()) {
            List<Movie> movies = null;

            @Override
            protected void onStartLoading() {
                if (movies != null) {
                    deliverResult(movies);
                } else {
                    forceLoad();
                }
            }

            @Override
            public List<Movie> loadInBackground() {
                return ContentProviderHelper.getMovieListFromDb(getActivity(), MoviesEntry.CONTENT_URI);
            }

            @Override
            public void deliverResult(List<Movie> data) {
                movies = data;
                super.deliverResult(movies);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        if (movies != null && mMovies.isEmpty()) {
            mMovies.addAll(movies);
            mAdapter = new MovieListAdapter(getContext(), mMovies);
            mAdapter.setListener(MovieListFragment.this);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mAdapter.setAdapterData(new ArrayList<Movie>());
    }

    public interface MovieSelectedListener {
        void onMovieSelected(Movie movie, View view);
    }

}
