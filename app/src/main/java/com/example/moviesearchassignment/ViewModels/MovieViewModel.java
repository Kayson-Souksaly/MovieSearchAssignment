package com.example.moviesearchassignment.ViewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesearchassignment.Models.ApiInterface;
import com.example.moviesearchassignment.Models.Movie;
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

//     LiveData holding the list of detailed movies
    private final MutableLiveData<List<MovieExtraInfo>> detailedMovies = new MutableLiveData<>();

//     LiveData for displaying any error messages to the user
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

//     Getter for the list of detailed movie objects to be returned to the observer
    public LiveData<List<MovieExtraInfo>> getMovies() {
        return detailedMovies;
    }

//    Returns the error message to the observer
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

//     Calls the OMDb API to get a list of movies matching the query
    public void fetchMovies(String query) {
        ApiInterface api = RetrofitClient.getRetrofitClient();
        Call<MovieResponse> call = api.getMovies(Constants.OMDB_API_KEY, query);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
//                 Check if the movie list was fetched successfully
                if (response.isSuccessful() && response.body() != null && response.body().getMovies() != null) {
                    List<Movie> movies = response.body().getMovies();
                    List<MovieExtraInfo> detailedList = new ArrayList<>();

//                     Loop through each movie to get extra details using imdbID
                    for (Movie movie : movies) {
                        api.getMovieById(Constants.OMDB_API_KEY, movie.getImdbID())
                                .enqueue(new Callback<MovieExtraInfo>() {
                                    @Override
                                    public void onResponse(@NonNull Call<MovieExtraInfo> call, @NonNull Response<MovieExtraInfo> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            detailedList.add(response.body());

//                                             When all details are loaded, update the LiveData
                                            if (detailedList.size() == movies.size()) {
                                                detailedMovies.postValue(detailedList);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<MovieExtraInfo> call, @NonNull Throwable t) {
//                                         Error when fetching detailed info for a movie
                                        errorMessage.setValue("Error: Failed to fetch movie details.");
                                    }
                                });
                    }
                } else {
//                     No results were found
                    errorMessage.setValue("No results found.");
                    detailedMovies.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
//                 Error when calling the movie list API
                errorMessage.setValue("Something went wrong.");
                Log.e("API_FAILURE", "API failed: " + t.getMessage(), t);
            }
        });
    }
}
