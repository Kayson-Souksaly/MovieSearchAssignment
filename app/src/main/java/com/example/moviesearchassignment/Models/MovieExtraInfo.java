package com.example.moviesearchassignment.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

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


    public MovieExtraInfo(String title, String year, String type, String imdbID, String poster, String imdbRating, String studio, String released) {
        this.title = title;
        this.year = year;
        this.type = type;
        this.imdbID = imdbID;
        this.poster = poster;
        this.imdbRating = imdbRating;
        this.studio = studio;
        this.released = released;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }
}
