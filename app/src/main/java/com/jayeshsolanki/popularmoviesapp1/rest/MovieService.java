package com.jayeshsolanki.popularmoviesapp1.rest;

import com.jayeshsolanki.popularmoviesapp1.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("movie/{sortby}")
    Call<MoviesResponse> getMovies(@Path("sortby") String sortBy, @Query("api_key") String apiKey);
}
