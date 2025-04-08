package com.example.moviesearchassignment.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.moviesearchassignment.Models.MovieExtraInfo;
import com.example.moviesearchassignment.R;
import com.example.moviesearchassignment.databinding.ActivityMovieDetailsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MovieDetailsActivity extends AppCompatActivity{

    private ActivityMovieDetailsBinding binding;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

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
//        The finish() method closes the activity and return to previous activity
        binding.backBtn.setOnClickListener(v -> finish());


//        On favorite button click, add the movie to the favorite list
        binding.favBtn.setOnClickListener(v -> {

//            Map to store the movie information and save it to the database
            Map<String, Object> movieMap = new HashMap<>();
            movieMap.put("uid", Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
            movieMap.put("title", movie.getTitle());
            movieMap.put("released", movie.getReleased());
            movieMap.put("rated", movie.getRated());
            movieMap.put("runtime", movie.getRuntime());
            movieMap.put("genre", movie.getGenre());
            movieMap.put("rating", movie.getImdbRating());
            movieMap.put("year", movie.getYear());
            movieMap.put("studio", movie.getStudio());
            movieMap.put("director", movie.getDirector());
            movieMap.put("actors", movie.getActors());
            movieMap.put("plot", movie.getPlot());
            movieMap.put("poster", movie.getPoster());
            movieMap.put("imdbID", movie.getImdbID());
            movieMap.put("imdbRating", movie.getImdbRating());

//            Make separate collection for each user by their UID
            String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

//            Save the movie to the database
            db.collection("users")
                    .document(uid)
                    .collection("favourites")
                    .whereEqualTo("imdbID", movie.getImdbID()) // or use "imdbID"
                    .get()
                    .addOnSuccessListener(querySnapshot -> {

//                        If the movie is not in the database, add it
                        if (querySnapshot.isEmpty()) {
                            db.collection("users")
                                    .document(uid)
                                    .collection("favourites")
                                    .add(movieMap)
                                    .addOnSuccessListener(documentReference -> {
                                        Log.d("SUCCESS", "Movie added to favourites");
                                        Toast.makeText(this, "Added to Favourites", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("ERROR", "Failed to add movie", e);
                                        Toast.makeText(this, "Failed to add to Favourites", Toast.LENGTH_SHORT).show();
                                    });

//                            Error message if the movie is already in the database
                        } else {
                            Log.d("ERROR", "Movie already exists in favourites");
                            Toast.makeText(this, "Movie already exists in Favourites", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("ERROR", "Failed to add movie", e);
                        Toast.makeText(this, "Failed to add to Favourites", Toast.LENGTH_SHORT).show();
                    });



        });
    }

}
