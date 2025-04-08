package com.example.moviesearchassignment.View;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.moviesearchassignment.Models.MovieExtraInfo;
import com.example.moviesearchassignment.R;
import com.example.moviesearchassignment.databinding.ActivityFavoriteDetailBinding;
import com.example.moviesearchassignment.databinding.ActivityFavoriteListBinding;
import com.example.moviesearchassignment.databinding.ActivityMovieDetailsBinding;

public class FavoriteMovieActivity extends AppCompatActivity {

    ActivityFavoriteDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFavoriteDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MovieExtraInfo movie = (MovieExtraInfo) getIntent().getSerializableExtra("MOVIE_INFORMATION");

        binding.titleView.setText(movie.getTitle());
        binding.releasedView.setText(movie.getReleased());
        binding.ratedView.setText(movie.getRated());
        binding.runtimeView.setText(movie.getRuntime());
        binding.genreView.setText(movie.getGenre());
        binding.studioView.setText(movie.getStudio());
        binding.directorView.setText(movie.getDirector());
        binding.actorsView.setText(movie.getActors());
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
//        The finish() method closes the activity and return to previous activity
        binding.backBtn.setOnClickListener(v -> finish());

    }
}
