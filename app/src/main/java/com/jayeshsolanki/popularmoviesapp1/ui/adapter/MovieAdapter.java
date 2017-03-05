package com.jayeshsolanki.popularmoviesapp1.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jayeshsolanki.popularmoviesapp1.R;
import com.jayeshsolanki.popularmoviesapp1.model.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieDetailsViewHolder> {

    private Context context;
    private Movie movie;
    private LayoutInflater inflater;

    private View fragmentView;

    public MovieAdapter(Context context, Movie movie, View fragmentView) {
        this.context = context;
        this.movie = movie;
        this.fragmentView = fragmentView;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MovieDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View v = inflater.inflate(R.layout.layout_holder_movie, parent, false);
            return new MovieDetailsViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(MovieDetailsViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            String backDropUrl = "https://image.tmdb.org/t/p/w396" + movie.getBackdropPath();
            ImageView backDrop = (ImageView) fragmentView.findViewById(R.id.backdrop_movie);
            Glide.with(context).load(backDropUrl).into(backDrop);

            String posterUrl = "https://image.tmdb.org/t/p/w396" + movie.getPosterPath();
            Glide.with(context).load(posterUrl).into(holder.getPoster());

            holder.getTitle().setText(movie.getTitle());
            holder.getOverview().setText(movie.getOverview());
            holder.getReleaseYear().setText(movie.getReleaseDate().substring(0,4));
        }
    }

    @Override
    public int getItemCount() {
        return (movie == null) ? 0 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return -1;
    }

    class MovieDetailsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.overview)
        TextView overview;
        @BindView(R.id.release_year)
        TextView releaseYear;

        @BindView(R.id.poster)
        ImageView poster;

        MovieDetailsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        TextView getTitle() {
            return title;
        }

        TextView getOverview() {
            return overview;
        }

        TextView getReleaseYear() {
            return releaseYear;
        }

        ImageView getPoster() {
            return poster;
        }

    }

}
