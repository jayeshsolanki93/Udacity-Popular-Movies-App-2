package com.jayeshsolanki.popularmoviesapp2.rest;

import com.jayeshsolanki.popularmoviesapp2.model.MovieReviewsResponse;
import com.jayeshsolanki.popularmoviesapp2.model.MovieVideosResponse;
import com.jayeshsolanki.popularmoviesapp2.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("movie/{sortby}")
    Call<MoviesResponse> getMovies(
            @Path("sortby") String sortBy,
            @Query("page") int page,
            @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<MovieVideosResponse> getVideos(
            @Path("id") int id,
            @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<MovieReviewsResponse> getReviews(
            @Path("id") int id,
            @Query("api_key") String apiKey);

}
