package com.example.moviesearchassignment.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.moviesearchassignment.Adapters.FavoriteAdapter;
import com.example.moviesearchassignment.Models.MovieExtraInfo;
import com.example.moviesearchassignment.Models.MovieRecyclerViewInterface;
import com.example.moviesearchassignment.ViewModels.FavoriteMovieViewModel;
import com.example.moviesearchassignment.databinding.ActivityFavoriteListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavoriteActivity extends AppCompatActivity implements MovieRecyclerViewInterface {

    private ActivityFavoriteListBinding binding;

    private FavoriteAdapter adapter;
    private final List<MovieExtraInfo> favoriteMovies = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    private FavoriteMovieViewModel viewModel;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new FavoriteAdapter( this, favoriteMovies, this);
        binding.movieRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.movieRecyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(FavoriteMovieViewModel.class);

        viewModel.getFavoriteMovies().observe(this, movies -> {
            favoriteMovies.clear();
            favoriteMovies.addAll(movies);
            adapter.setFavoriteMovies(movies);
        });

        viewModel.getErrorMessage().observe(this, error -> {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });

        mAuth = FirebaseAuth.getInstance();

        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        viewModel.fetchFavoriteMovies(uid);

        //back to MainActivity section
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoriteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onMovieClick(int position) {
        //        get position of the movie being clicked
        MovieExtraInfo movieClicked = favoriteMovies.get(position);
        Intent intent = new Intent(this, FavoriteMovieActivity.class);

//        Save all information of the movie being clicked
        intent.putExtra("MOVIE_INFORMATION", movieClicked);

//        Go to movie details activity
        startActivity(intent);
    }
}
