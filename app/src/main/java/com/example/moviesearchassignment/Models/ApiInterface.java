package com.example.moviesearchassignment.Models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit API interface to interact with the OMDb API.
 */
public interface ApiInterface {
    @GET("/")
    Call<MovieResponse> getMovies(
        @Query("apikey") String apiKey,
        @Query("s") String query
    );
    @GET("/")
    Call<MovieExtraInfo> getMovieById(
            @Query("apikey") String apiKey,
            @Query("i") String imdbId
    );

}
