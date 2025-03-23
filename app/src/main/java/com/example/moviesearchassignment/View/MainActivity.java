package com.example.moviesearchassignment.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesearchassignment.Models.ApiInterface;
import com.example.moviesearchassignment.Models.Movie;
import com.example.moviesearchassignment.Models.MovieAdapter;
import com.example.moviesearchassignment.Models.MovieExtraInfo;
import com.example.moviesearchassignment.Models.MovieResponse;
import com.example.moviesearchassignment.Models.RetrofitClient;
import com.example.moviesearchassignment.Utils.Constants;
import com.example.moviesearchassignment.databinding.ActivityMainBinding;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<MovieExtraInfo> movieByIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup RecyclerView
        recyclerView = binding.movieRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MovieAdapter(this, movieByIdList);
        recyclerView.setAdapter(adapter);

        // Search button click listener
        binding.searchBtn.setOnClickListener(v -> {
            String query = binding.searchBar.getText().toString().trim();
            if (!query.isEmpty()) {
                fetchMovies(query);
            } else {
                Toast.makeText(this, "Enter a movie title", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fetchMovies(String query) {
        Log.d("DEBUG", "Fetching movies for: " + query);

        ApiInterface api = RetrofitClient.getRetrofitClient();
        Call<MovieResponse> call = api.getMovies(Constants.OMDB_API_KEY, query);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getMovies() != null) {
                    List<Movie> movies = response.body().getMovies();

                    movieByIdList.clear();

                    Log.d("API_RESPONSE", "Fetched " + movies.size() + " basic movie results.");
                    for (Movie movie : movies) {
                        Log.d("API_TEST", "CALLING API: " + movie.getImdbID() + " " + api.getMovieById(Constants.OMDB_API_KEY, movie.getImdbID()));
                        api.getMovieById(Constants.OMDB_API_KEY, movie.getImdbID())
                                .enqueue(new Callback<MovieExtraInfo>() {
                                    @Override
                                    public void onResponse(@NonNull Call<MovieExtraInfo> call, @NonNull Response<MovieExtraInfo> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            MovieExtraInfo detailed = response.body();
                                            Log.d("SUCCESS", "Fetched additional information for: " + detailed);
                                            // Add detailed info to Movie object (if you want to extend Movie or replace it)
                                            // Or create a new Movie object using info from MovieExtraInfo

                                            // For now, assume MovieExtraInfo extends Movie or is compatible:
                                            movieByIdList.add(detailed);

                                            // Once we've gathered all, update the adapter
                                            if (movieByIdList.size() == movies.size()) {
                                                Log.d("Movie List passed", "SUCCESSFULLY PASSED");
                                                adapter.setMovies(movieByIdList);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<MovieExtraInfo> call, @NonNull Throwable t) {
                                        Log.e("DETAIL_FETCH", "Failed to fetch extra info for " + movie.getTitle(), t);
                                    }
                                });
                    }
                } else {
                    Log.e("API_RESPONSE", "No results or empty body");
                    Toast.makeText(MainActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                    adapter.setMovies(new ArrayList<>()); // clear the list
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                Log.e("API_FAILURE", "API call failed", t);
            }
        });
    }


}

