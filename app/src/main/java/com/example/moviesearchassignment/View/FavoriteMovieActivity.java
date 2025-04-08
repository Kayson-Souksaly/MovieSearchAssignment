package com.example.moviesearchassignment.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.moviesearchassignment.Adapters.FavoriteAdapter;
import com.example.moviesearchassignment.Models.MovieExtraInfo;
import com.example.moviesearchassignment.Models.MovieRecyclerViewInterface;
import com.example.moviesearchassignment.R;
import com.example.moviesearchassignment.ViewModels.FavoriteMovieViewModel;
import com.example.moviesearchassignment.databinding.ActivityFavoriteDetailBinding;
import com.example.moviesearchassignment.databinding.ActivityFavoriteListBinding;
import com.example.moviesearchassignment.databinding.ActivityMovieDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavoriteMovieActivity extends AppCompatActivity implements MovieRecyclerViewInterface {

    ActivityFavoriteDetailBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFavoriteDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


//        Retrieve the movie information from the intent
        MovieExtraInfo movie = (MovieExtraInfo) getIntent().getSerializableExtra("MOVIE_INFORMATION");
        String documentId = getIntent().getStringExtra("DOCUMENT_ID");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();


//        Set fields from database
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

//        Update plot
        binding.updateBtn.setOnClickListener(v -> {
//            Open a dialog to edit the plot
            EditText input = new EditText(this);
            input.setText(binding.plotView.getText());
            input.setSelection(input.getText().length()); // move cursor to end

//            Create and show the dialog
            new AlertDialog.Builder(this)
                    .setTitle("Edit Plot")
                    .setMessage("Update the movie's plot below:")
                    .setView(input)
//                    On positive button click, update the plot
                    .setPositiveButton("Save", (dialog, which) -> {
                        String newPlot = input.getText().toString().trim();

//                        If the plot is empty, show a toast and return to the previous activity
//                        If plot is empty, default to "Add a plot"
                        if (newPlot.isEmpty()) {
                            updatePlot("Add a plot", documentId, uid);
                        }else {
                            updatePlot(newPlot, documentId, uid);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });


//        On delete button click, delete the movie from the database
        binding.deleteBtn.setOnClickListener(v -> {
            assert documentId != null;
            db.collection("users")
                    .document(uid)
                    .collection("favourites")
                    .document(documentId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();

//                        Go back to the favorite list activity and display the updated list
                        setResult(RESULT_OK);
                        finish();
                    });

        });

    }

//    Update plot based on the document ID and inputted plot
    public void updatePlot(String newPlot, String documentId, String uid) {
//        Update the plot in the database
        assert documentId != null;
        db.collection("users")
                .document(uid)
                .collection("favourites")
                .document(documentId)
                .update("plot", newPlot)
                .addOnSuccessListener(aVoid -> {
                    binding.plotView.setText(newPlot); // update UI
                    Toast.makeText(this, "Plot updated", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onMovieClick(int position) {

    }
}