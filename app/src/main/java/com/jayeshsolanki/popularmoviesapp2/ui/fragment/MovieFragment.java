package com.jayeshsolanki.popularmoviesapp2.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.jayeshsolanki.popularmoviesapp2.AppConstants;
import com.jayeshsolanki.popularmoviesapp2.PopularMoviesApp;
import com.jayeshsolanki.popularmoviesapp2.R;
import com.jayeshsolanki.popularmoviesapp2.model.Movie;
import com.jayeshsolanki.popularmoviesapp2.model.MovieReviewsResponse;
import com.jayeshsolanki.popularmoviesapp2.model.MovieVideosResponse;
import com.jayeshsolanki.popularmoviesapp2.model.Review;
import com.jayeshsolanki.popularmoviesapp2.model.Video;
import com.jayeshsolanki.popularmoviesapp2.rest.MovieService;
import com.jayeshsolanki.popularmoviesapp2.ui.activity.MovieActivity;
import com.jayeshsolanki.popularmoviesapp2.ui.adapter.ReviewsListAdapter;
import com.jayeshsolanki.popularmoviesapp2.ui.adapter.VideosListAdapter;
import com.jayeshsolanki.popularmoviesapp2.util.ContentProviderHelper;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

import static com.jayeshsolanki.popularmoviesapp2.AppConstants.API_KEY;
import static com.jayeshsolanki.popularmoviesapp2.AppConstants.MOVIE_INTENT;

public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Boolean> {

    private static final int LOADER_ID = 600;

    private static final String MOVIE_KEY = MOVIE_INTENT;
    private static final String OPERATION_KEY = "operation";

    private Movie movie;

    private boolean isFavMovie;

    private ArrayList<Video> mVideos = new ArrayList<>();

    private ArrayList<Review> mReviews = new ArrayList<>();

    @Inject
    Retrofit retrofit;

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

    @BindView(R.id.fab_favorite)
    FloatingActionButton favoriteFab;

    @BindView(R.id.recyclerView_videos)
    RecyclerView mVideosListRecyclerView;

    @BindView(R.id.recyclerView_reviews)
    RecyclerView mReviewsListRecyclerView;

    protected VideosListAdapter mVideosListAdapter;

    protected ReviewsListAdapter mReviewListAdapter;

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

        ((PopularMoviesApp) getActivity().getApplication())
                .getDataComponent().inject(MovieFragment.this);

        if (savedInstanceState != null) {
            movie = savedInstanceState.getParcelable(MOVIE_INTENT);
        }

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(MOVIE_INTENT)) {
            movie = intent.getExtras().getParcelable(MOVIE_INTENT);
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

        Bundle args = new Bundle();
        args.putParcelable(MOVIE_INTENT, movie);
        args.putString(OPERATION_KEY, "query");
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, args, this).forceLoad();

        bindData(movie);

        setupVideosRecyclerView();
        setupReviewsRecyclerView();

        loadMoreContent();
    }

    public void setupVideosRecyclerView() {
        mVideosListAdapter = new VideosListAdapter(getActivity(), mVideos);
        mVideosListRecyclerView.setAdapter(mVideosListAdapter);
        mVideosListRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    public void setupReviewsRecyclerView() {
        mReviewListAdapter = new ReviewsListAdapter(mReviews);
        mReviewsListRecyclerView.setAdapter(mReviewListAdapter);
        mReviewsListRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
    }

    public void loadMoreContent() {
        if (movie != null) {
            MovieService movieService = retrofit.create(MovieService.class);

            Call<MovieVideosResponse> moviesVideosResponseCall = movieService.getVideos(movie.getId(), API_KEY);
            moviesVideosResponseCall.enqueue(new Callback<MovieVideosResponse>() {
                @Override
                public void onResponse(Call<MovieVideosResponse> call, Response<MovieVideosResponse> response) {
                    if (response != null && response.body() != null) {
                        mVideos.addAll(response.body().getResults());
                        mVideosListAdapter = new VideosListAdapter(getContext(), mVideos);
                        mVideosListRecyclerView.setAdapter(mVideosListAdapter);
                    }
                }

                @Override
                public void onFailure(Call<MovieVideosResponse> call, Throwable t) {
                    showSnackBar(getString(R.string.internet_err_msg));
                    Timber.d("Error code " + t.toString());
                }
            });

            Call<MovieReviewsResponse> movieReviewsResponseCall = movieService.getReviews(movie.getId(), API_KEY);
            movieReviewsResponseCall.enqueue(new Callback<MovieReviewsResponse>() {
                @Override
                public void onResponse(Call<MovieReviewsResponse> call, Response<MovieReviewsResponse> response) {
                    if (response != null && response.body() != null) {
                        mReviews.addAll(response.body().getResults());
                        mReviewListAdapter = new ReviewsListAdapter(mReviews);
                        mReviewsListRecyclerView.setAdapter(mReviewListAdapter);
                    }
                }

                @Override
                public void onFailure(Call<MovieReviewsResponse> call, Throwable t) {
                    showSnackBar(getString(R.string.internet_err_msg));
                    Timber.d("Error code " + t.toString());
                }
            });
        }
    }

    void showSnackBar(String msg) {
        Snackbar.make(getView(), msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE_INTENT, movie);
    }

    public void setupToolbar(Toolbar toolbar) {
        toolbar.setTitle(movie.getTitle());

        collapsingToolbar.setTitle(movie.getTitle());

        String backDropUrl = AppConstants.BASE_IMAGE_URL + movie.getBackdropPath();
        Glide.with(this).load(backDropUrl).into(backdrop);

        if (getActivity() instanceof MovieActivity) {
            toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });
        }

        toolbar.inflateMenu(R.menu.movie_menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_share) {
                    String data = String.format("%s%n%s", movie.getTitle(), movie.getOverview());
                    Intent sharingIntent = shareMovieIntent(data);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
                return true;
            }
        });
    }

    public void bindData(Movie movie) {
        releaseYear.setText(movie.getReleaseDate().substring(0, 4));
        title.setText(movie.getTitle());
        overview.setText(movie.getOverview());
        voteAverage.setRating((float) movie.getVoteAverage().doubleValue());
    }

    public Intent shareMovieIntent(String data) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT,
                getResources().getString(R.string.extra_movie_subject));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, data);
        return sharingIntent;
    }

    @OnClick(R.id.fab_favorite)
    public void onClick(View v) {
        Bundle args = new Bundle();
        args.putParcelable(MOVIE_KEY, movie);
        if (isFavMovie) {
            args.putString(OPERATION_KEY, "delete");
            getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, args, MovieFragment.this).forceLoad();
        } else {
            args.putString(OPERATION_KEY, "insert");
            getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, args, MovieFragment.this).forceLoad();
        }
    }

    @Override
    public Loader<Boolean> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Boolean>(this.getContext()) {
            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public Boolean loadInBackground() {
                if (args != null) {
                    String operation = args.getString(OPERATION_KEY);
                    Movie movieToSave = args.getParcelable(MOVIE_KEY);
                    String movieId = String.valueOf(movie.getId());
                    switch (operation) {
                        case "query":
                            return ContentProviderHelper.isMovieInDb(getContext(), movieId);
                        case "insert":
                            Uri uri = ContentProviderHelper.insertMoviesInDb(getContext(), movieToSave);
                            Timber.d(uri.toString());
                            return uri != null;
                        case "delete":
                            int deletedMoviesCount = ContentProviderHelper.deleteMoviesFromDb(getContext(), movieId);
                            return deletedMoviesCount <= 0;
                        default:
                            throw new UnsupportedOperationException("This operation is not supported.");
                    }
                }
                return false;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Boolean> loader, Boolean isFav) {
        isFavMovie = isFav;
        if (isFav) {
            favoriteFab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_fav));
        } else {
            favoriteFab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_fav_border));
        }
    }

    @Override
    public void onLoaderReset(Loader<Boolean> loader) {
        isFavMovie = false;
    }
}
