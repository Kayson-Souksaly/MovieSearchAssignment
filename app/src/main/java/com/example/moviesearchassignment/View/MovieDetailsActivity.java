package com.example.moviesearchassignment.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.moviesearchassignment.Models.MovieExtraInfo;
import com.example.moviesearchassignment.R;
import com.example.moviesearchassignment.databinding.ActivityMovieDetailsBinding;
import com.example.moviesearchassignment.Models.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    private ActivityMovieDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get Movie object passed from Intent
        MovieExtraInfo movie = (MovieExtraInfo) getIntent().getSerializableExtra("extraInfo");

        Intent intent = new Intent(this, MovieDetailsActivity.class);
//        intent.putExtra("extraInfo", extraInfo); // Make sure MovieExtraInfo implements Serializable
        startActivity(intent);


        if (movie == null) {
            Toast.makeText(this, "Movie details not available.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Display movie details
        binding.titleTextView.setText("Title: " + movie.getTitle());
//        binding.releasedTextView.setText("Released: " + movie.getReleased());
        binding.imdbTextView.setText("IMDb ID: " + movie.getImdbID());

        String posterUrl = movie.getPoster();
        if (posterUrl != null && !posterUrl.equalsIgnoreCase("N/A")) {
            Glide.with(this)
                    .load(posterUrl)
                    .into(binding.posterView);
        } else {
            binding.posterView.setImageResource(R.drawable.placeholder); // Optional fallback image
        }

        // Back button closes the activity
        binding.backButton.setOnClickListener(v -> finish());

    }
    private String safe(String value) {
        return value != null ? value : "N/A";
    }

}
