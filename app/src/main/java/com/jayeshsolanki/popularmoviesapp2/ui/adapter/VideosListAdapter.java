package com.jayeshsolanki.popularmoviesapp2.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jayeshsolanki.popularmoviesapp2.model.Video;

import com.jayeshsolanki.popularmoviesapp2.R;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jayeshsolanki.popularmoviesapp2.AppConstants.YOUTUBE_THUMBNAIL_URL;
import static com.jayeshsolanki.popularmoviesapp2.AppConstants.YOUTUBE_VIDEO_BASE_URL;


public class VideosListAdapter extends RecyclerView.Adapter<VideosListAdapter.VideoViewHolder> {

    private Context context;
    private List<Video> videos = Collections.emptyList();

    public VideosListAdapter(Context context, List<Video> videos) {
        this.context = context;
        this.videos =videos;
    }

    public void setAdapter(List<Video> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VideoViewHolder holder, int position) {
        holder.setVideo(videos.get(position));
        String thumbnailUrl = String.format(YOUTUBE_THUMBNAIL_URL, holder.video.getKey());
        Glide.with(context).load(thumbnailUrl).into(holder.getVideoItemThumbnail());
        holder.getVideoItemTitle().setText(holder.video.getName());
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        Video video;

        @BindView(R.id.video_thumbnail)
        ImageView videoItemThumbnail;

        @BindView(R.id.video_title)
        TextView videoItemTitle;

        VideoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void setVideo(Video video) {
            this.video = video;
        }

        TextView getVideoItemTitle() {
            return videoItemTitle;
        }

        ImageView getVideoItemThumbnail() {
            return videoItemThumbnail;
        }

        @OnClick(R.id.video_item_card)
        void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String url = YOUTUBE_VIDEO_BASE_URL + video.getKey();
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        }

    }

}
