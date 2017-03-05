package com.jayeshsolanki.popularmoviesapp1.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jayeshsolanki.popularmoviesapp1.R;
import com.jayeshsolanki.popularmoviesapp1.model.Movie;
import com.jayeshsolanki.popularmoviesapp1.ui.activity.MovieActivity;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private Context context;
    private List<Movie> movies = Collections.emptyList();

    public MovieListAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
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
        holder.movie = movies.get(position);
        String posterUrl = "https://image.tmdb.org/t/p/w396" + movies.get(position).getPosterPath();
        Glide.with(context).load(posterUrl).into(holder.getMovieItemPoster());
        holder.getMovieItemTitle().setText(holder.movie.getTitle());
    }

    @Override
    public int getItemCount() {
        return movies.size();
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

        ImageView getMovieItemPoster() {
            return movieItemPoster;
        }

        TextView getMovieItemTitle() {
            return movieItemTitle;
        }

        @OnClick(R.id.movie_item_card)
        void onClick() {
            Intent intent = new Intent(context, MovieActivity.class);
            intent.putExtra("movie", movie);
            context.startActivity(intent);
        }

    }

}
