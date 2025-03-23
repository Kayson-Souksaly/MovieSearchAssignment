package com.example.moviesearchassignment.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Full movie details retrieved using IMDb ID.
 */
public class MovieExtraInfo implements Serializable {

    @SerializedName("Title")
    private String title;

    @SerializedName("Year")
    private String year;

    @SerializedName("Type")
    private String type;

    @SerializedName("imdbID")
    private String imdbID;
    @SerializedName("Poster")
    private String poster;
    @SerializedName("imdbRating")
    private String imdbRating;
    @SerializedName("Production")
    private String studio;
    @SerializedName("Released")
    private String released;
    @SerializedName("Rated")
    private String rated;
    @SerializedName("Runtime")
    private String runtime;
    @SerializedName("Genre")
    private String genre;
    @SerializedName("Director")
    private String director;
    @SerializedName("Actors")
    private String actors;
    @SerializedName("Plot")
    private String plot;

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getType() {
        return type;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getPoster() {
        return poster;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public String getStudio() {
        return studio;
    }

    public String getReleased() {
        return released;
    }

    public String getRated() {
        return rated;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public String getActors() {
        return actors;
    }

    public String getPlot() {
        return plot;
    }
}
