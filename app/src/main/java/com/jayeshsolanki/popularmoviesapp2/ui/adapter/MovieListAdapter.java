package com.jayeshsolanki.popularmoviesapp2.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jayeshsolanki.popularmoviesapp2.AppConstants;
import com.jayeshsolanki.popularmoviesapp2.R;
import com.jayeshsolanki.popularmoviesapp2.model.Movie;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private Context context;
    private MovieClickListener listener;
    private List<Movie> movies = Collections.emptyList();

    public MovieListAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    public void setListener(MovieClickListener listener) {
        this.listener = listener;
    }

    public void setAdapterData(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        holder.setMovie(movies.get(position));
        String posterUrl = AppConstants.BASE_IMAGE_URL + movies.get(position).getPosterPath();
        Glide.with(context).load(posterUrl).into(holder.getMovieItemPoster());
        holder.getMovieItemTitle().setText(holder.movie.getTitle());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public interface MovieClickListener {
        void onMovieClick(final Movie movie, View view, int position);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        Movie movie;

        @BindView(R.id.movie_item_poster)
        ImageView movieItemPoster;

        @BindView(R.id.movie_item_title)
        TextView movieItemTitle;

        MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void setMovie(Movie movie) {
            this.movie = movie;
        }

        ImageView getMovieItemPoster() {
            return movieItemPoster;
        }

        TextView getMovieItemTitle() {
            return movieItemTitle;
        }

        @OnClick(R.id.movie_item_card)
        void onClick(View v) {
            listener.onMovieClick(movie, v, getAdapterPosition());
        }

    }

}
