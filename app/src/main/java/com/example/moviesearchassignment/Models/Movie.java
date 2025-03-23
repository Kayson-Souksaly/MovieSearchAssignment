package com.example.moviesearchassignment.Models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Basic movie info used for initial search result.
 */
public class Movie implements Serializable {
    @SerializedName("Title")
    private String title;

    @SerializedName("imdbID")
    private String imdbID;

    public String getImdbID() {
        return imdbID;
    }

    public String getTitle() {
        return title;
    }
}
