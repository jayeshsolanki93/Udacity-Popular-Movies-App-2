package com.jayeshsolanki.popularmoviesapp2.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jayeshsolanki.popularmoviesapp2.R;
import com.jayeshsolanki.popularmoviesapp2.model.Review;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsListAdapter extends RecyclerView.Adapter<ReviewsListAdapter.ReviewViewHolder> {

    private List<Review> reviews = Collections.emptyList();

    public ReviewsListAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setAdapter(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.setReview(reviews.get(position));
        holder.getReviewItemAuthor().setText(holder.review.getAuthor());
        holder.getReviewItemContent().setText(holder.review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        Review review;

        @BindView(R.id.review_author)
        TextView reviewItemAuthor;

        @BindView(R.id.review_content)
        TextView reviewItemContent;

        ReviewViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setReview(Review review) {
            this.review = review;
        }

        public TextView getReviewItemAuthor() {
            return reviewItemAuthor;
        }

        public TextView getReviewItemContent() {
            return reviewItemContent;
        }

    }

}
