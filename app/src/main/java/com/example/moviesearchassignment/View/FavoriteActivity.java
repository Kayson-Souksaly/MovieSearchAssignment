package com.example.moviesearchassignment.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.moviesearchassignment.Adapters.FavoriteAdapter;
import com.example.moviesearchassignment.Models.MovieExtraInfo;
import com.example.moviesearchassignment.Models.MovieRecyclerViewInterface;
import com.example.moviesearchassignment.ViewModels.FavoriteMovieViewModel;
import com.example.moviesearchassignment.databinding.ActivityFavoriteListBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Activity for the favorite movie list
 */
public class FavoriteActivity extends AppCompatActivity implements MovieRecyclerViewInterface {

    private ActivityFavoriteListBinding binding;

    private FavoriteAdapter adapter;
    private final List<MovieExtraInfo> favoriteMovies = new ArrayList<>();

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

//        Get the user ID
        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

//        Fetch favorite movies from Firestore with the user ID
        viewModel.fetchFavoriteMovies(uid);

        //back to MainActivity section
        binding.searchBackBtn.setOnClickListener(v -> finish());
    }

    @Override
    public void onMovieClick(int position) {
        //        get position of the movie being clicked
        MovieExtraInfo movieClicked = favoriteMovies.get(position);
        Intent intent = new Intent(this, FavoriteMovieActivity.class);

//        Save all information of the movie being clicked
        intent.putExtra("MOVIE_INFORMATION", movieClicked);
        intent.putExtra("DOCUMENT_ID", movieClicked.getDocumentId());

//        Go to movie details activity
        startActivityForResult(intent, 1);

    }

//    Update the list when returning from the movie details activity
//    when deleting a movie from the favorite list
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            viewModel.fetchFavoriteMovies(uid); // 🔄 refresh the list
        }
    }


}
