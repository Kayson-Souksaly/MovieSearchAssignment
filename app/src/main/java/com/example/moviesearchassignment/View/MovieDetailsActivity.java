package com.example.moviesearchassignment.View;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.moviesearchassignment.Models.MovieExtraInfo;
import com.example.moviesearchassignment.R;
import com.example.moviesearchassignment.databinding.ActivityMovieDetailsBinding;

import java.io.Serializable;

public class MovieDetailsActivity extends AppCompatActivity implements Serializable {

    private ActivityMovieDetailsBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        Get Movie object passed from Intent
        MovieExtraInfo movie = (MovieExtraInfo) getIntent().getSerializableExtra("MOVIE_INFORMATION");


//        Display movie details
        assert movie != null;
        binding.titleView.setText(movie.getTitle());
        binding.releasedView.setText("Released: " + movie.getReleased());
        binding.ratedView.setText("Rated: " + movie.getRated());
        binding.runtimeView.setText("Runtime: " + movie.getRuntime());
        binding.genreView.setText("Genre: " + movie.getGenre());
        binding.studioView.setText("Studio: " + (movie.getStudio() != null ? movie.getStudio() : "N/A"));
        binding.directorView.setText("Director: " + movie.getDirector());
        binding.actorsView.setText("Actors: " + movie.getActors());
        binding.plotView.setText(movie.getPlot());

        String posterUrl = movie.getPoster();
        if (posterUrl != null && !posterUrl.equalsIgnoreCase("N/A")) {
            Glide.with(this)
                    .load(posterUrl)
                    .into(binding.posterView);
        } else {
            binding.posterView.setImageResource(R.drawable.placeholder);
        }

//        Back button closes the activity
        binding.backBtn.setOnClickListener(v -> finish());

    }

}
