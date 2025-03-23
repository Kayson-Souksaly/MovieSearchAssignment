package com.example.moviesearchassignment.Models;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit client to create API service instance.
 */
public class RetrofitClient {
    private static final String BASE_URL = "https://www.omdbapi.com/";
    private static Retrofit retrofit;

    public static ApiInterface getRetrofitClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiInterface.class);
    }

}
