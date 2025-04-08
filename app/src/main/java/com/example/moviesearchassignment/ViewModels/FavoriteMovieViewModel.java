package com.example.moviesearchassignment.ViewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesearchassignment.Models.MovieExtraInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMovieViewModel extends ViewModel {
    private final MutableLiveData<List<MovieExtraInfo>> favoriteMovies = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<List<MovieExtraInfo>> getFavoriteMovies() {
        return favoriteMovies;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchFavoriteMovies(String uid) {
        db.collection("users")
                .document(uid)
                .collection("favourites")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<MovieExtraInfo> movies = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            MovieExtraInfo movie = doc.toObject(MovieExtraInfo.class);
                            movies.add(movie);
                        }
                        favoriteMovies.postValue(movies);
                    } else {
                        errorMessage.postValue("Failed to load favorites");
                    }
                });
    }

}
