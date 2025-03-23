package com.example.moviesearchassignment.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse {
    @SerializedName("Search")
    private List<Movie> movies;

    private List<MovieExtraInfo> moviesById;

    public List<Movie> getMovies() {
        return movies;
    }

    public List<MovieExtraInfo> getMovieById() {
        return moviesById;
    }
}

