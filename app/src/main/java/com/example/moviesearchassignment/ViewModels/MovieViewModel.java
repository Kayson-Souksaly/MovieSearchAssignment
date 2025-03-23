package com.example.moviesearchassignment.ViewModels;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesearchassignment.Models.ApiInterface;
import com.example.moviesearchassignment.Models.Movie;
import com.example.moviesearchassignment.Models.MovieAdapter;
import com.example.moviesearchassignment.Models.MovieExtraInfo;
import com.example.moviesearchassignment.Models.MovieResponse;
import com.example.moviesearchassignment.Models.RetrofitClient;
import com.example.moviesearchassignment.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieViewModel extends ViewModel {
    private final MutableLiveData<List<MovieExtraInfo>> detailedMovies = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<List<MovieExtraInfo>> getMovies() {
        return detailedMovies;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchMovies(String query) {
        ApiInterface api = RetrofitClient.getRetrofitClient();
        Call<MovieResponse> call = api.getMovies(Constants.OMDB_API_KEY, query);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getMovies() != null) {
                    List<Movie> movies = response.body().getMovies();
                    List<MovieExtraInfo> detailedList = new ArrayList<>();

                    for (Movie movie : movies) {
                        api.getMovieById(Constants.OMDB_API_KEY, movie.getImdbID())
                                .enqueue(new Callback<MovieExtraInfo>() {
                                    @Override
                                    public void onResponse(@NonNull Call<MovieExtraInfo> call, @NonNull Response<MovieExtraInfo> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            detailedList.add(response.body());

                                            if (detailedList.size() == movies.size()) {
                                                detailedMovies.postValue(detailedList);  // LiveData gets updated
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<MovieExtraInfo> call, @NonNull Throwable t) {
                                        errorMessage.setValue("Error: Failed to fetch movie details.");
                                    }
                                });
                    }
                } else {
                    errorMessage.setValue("No results found.");
                    detailedMovies.setValue(new ArrayList<>()); // Clear list
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                errorMessage.setValue("Something went wrong.");
                Log.e("API_FAILURE", "API failed", t);
            }
        });
    }
}

