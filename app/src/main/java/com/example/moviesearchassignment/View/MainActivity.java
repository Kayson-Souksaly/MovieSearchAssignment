package com.example.moviesearchassignment.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesearchassignment.Models.ApiInterface;
import com.example.moviesearchassignment.Models.Movie;
import com.example.moviesearchassignment.Models.MovieAdapter;
import com.example.moviesearchassignment.Models.MovieExtraInfo;
import com.example.moviesearchassignment.Models.MovieRecyclerViewInterface;
import com.example.moviesearchassignment.Models.MovieResponse;
import com.example.moviesearchassignment.Models.RetrofitClient;
import com.example.moviesearchassignment.Utils.Constants;
import com.example.moviesearchassignment.ViewModels.MovieViewModel;
import com.example.moviesearchassignment.databinding.ActivityMainBinding;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieRecyclerViewInterface {
    private ActivityMainBinding binding;
    private MovieAdapter adapter;
    private MovieViewModel viewModel;
    private final List<MovieExtraInfo> currentMovies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new MovieAdapter(this, currentMovies, this);
        binding.movieRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.movieRecyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        viewModel.getMovies().observe(this, movies -> {
            currentMovies.clear();
            currentMovies.addAll(movies);
            adapter.setMovies(movies);
        });

        viewModel.getErrorMessage().observe(this, error -> {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });

        binding.searchBtn.setOnClickListener(v -> {
            String query = binding.searchBar.getText().toString().trim();
            if (!query.isEmpty()) {
                viewModel.fetchMovies(query);
            } else {
                Toast.makeText(this, "Enter a movie title", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMovieClick(int position) {
        MovieExtraInfo movieClicked = currentMovies.get(position);
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("MOVIE_INFORMATION", movieClicked);
        startActivity(intent);
    }
}


