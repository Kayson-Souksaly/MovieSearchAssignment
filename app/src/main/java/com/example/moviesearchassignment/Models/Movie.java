package com.example.moviesearchassignment.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Movie {
    @SerializedName("Title")
    private String title;

    @SerializedName("Year")
    private String year;

    @SerializedName("Poster")
    private String poster;

    @SerializedName("imdbID")
    private String imdbID;

    // Getters
    public String getTitle() { return title; }
    public String getYear() { return year; }
    public String getPoster() { return poster; }
    public String getImdbID() { return imdbID; }
}

