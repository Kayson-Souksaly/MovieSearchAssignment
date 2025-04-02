package com.example.moviesearchassignment.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.moviesearchassignment.Adapters.MovieAdapter;
import com.example.moviesearchassignment.Models.MovieExtraInfo;
import com.example.moviesearchassignment.Models.MovieRecyclerViewInterface;
import com.example.moviesearchassignment.ViewModels.MovieViewModel;
import com.example.moviesearchassignment.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieRecyclerViewInterface {
    private ActivityMainBinding binding;
    private MovieAdapter adapter;
    private MovieViewModel viewModel;
    private final List<MovieExtraInfo> currentMovies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            // No user logged in, go to login
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // close MainActivity
            return;
        }
        Toast.makeText(this, "Welcome " + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new MovieAdapter(this, currentMovies, this);
        binding.movieRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.movieRecyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

//        observable data when receiving movie list
        viewModel.getMovies().observe(this, movies -> {
            currentMovies.clear();
            currentMovies.addAll(movies);
            adapter.setMovies(movies);
        });

//        observable data for any errors that happened in the search process
        viewModel.getErrorMessage().observe(this, error -> {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });

//        onClickListener for when the search button is clicked with the search fields is filled
        binding.searchBtn.setOnClickListener(v -> {
            String query = binding.searchBar.getText().toString().trim();
            if (!query.isEmpty()) {
                viewModel.fetchMovies(query);
            } else {
//                Send error message to the user if the field is empty
                Toast.makeText(this, "Enter a movie title", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    Intent listener for when the card is clicked
//    This sends them to the movie details activity
    @Override
    public void onMovieClick(int position) {
//        get position of the movie being clicked
        MovieExtraInfo movieClicked = currentMovies.get(position);
        Intent intent = new Intent(this, MovieDetailsActivity.class);

//        Save all information of the movie being clicked
        intent.putExtra("MOVIE_INFORMATION", movieClicked);

//        Go to movie details activity
        startActivity(intent);
    }
}


