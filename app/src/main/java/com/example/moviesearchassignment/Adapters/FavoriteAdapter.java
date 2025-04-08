package com.example.moviesearchassignment.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviesearchassignment.Models.MovieExtraInfo;
import com.example.moviesearchassignment.Models.MovieRecyclerViewInterface;
import com.example.moviesearchassignment.R;
import com.example.moviesearchassignment.databinding.RecyclerViewRowBinding;

import java.util.List;

/**
 * Recycler view adapter for the favorite movie list
 * */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> implements MovieRecyclerViewInterface {

    private final MovieRecyclerViewInterface movieRecyclerViewInterface;
    private final Context context;
    private final List<MovieExtraInfo> favoriteMovies;

    public FavoriteAdapter(Context context, List<MovieExtraInfo> favoriteMovies, MovieRecyclerViewInterface movieRecyclerViewInterface) {
        this.context = context;
        this.favoriteMovies = favoriteMovies;
        this.movieRecyclerViewInterface = movieRecyclerViewInterface;
    }



    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerViewRowBinding binding = RecyclerViewRowBinding.inflate(LayoutInflater.from(parent.getContext())
                ,parent,
                false);
        return new ViewHolder(binding, movieRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {
        MovieExtraInfo favoriteMovie = favoriteMovies.get(position);

//        Dev debugging
        Log.d("TEST", "String of: " + String.valueOf(favoriteMovie));

        holder.binding.titleView.setText(favoriteMovie.getTitle() != null ? favoriteMovie.getTitle() : "No title");
        holder.binding.yearView.setText(favoriteMovie.getYear() != null ? "Year: " + favoriteMovie.getYear() : "Year: N/A");
        holder.binding.studioView.setText(favoriteMovie.getStudio() != null ? "Studio: " + favoriteMovie.getStudio() : "Studio: N/A");
        holder.binding.ratingView.setText(favoriteMovie.getImdbRating() != null ? "Rating: " + favoriteMovie.getImdbRating() : "Rating: N/A");

//      Poster
        if (favoriteMovie.getPoster() != null && !favoriteMovie.getPoster().equals("N/A")) {
//          Glide allows to get images from a link,
//          using the the placeholder method as load a placeholder while retrieving
            Glide.with(context)
                    .load(favoriteMovie.getPoster())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.binding.posterView);
        } else {
            holder.binding.posterView.setImageResource(R.drawable.placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return favoriteMovies.size();
    }


    public void setFavoriteMovies(List<MovieExtraInfo> favoriteMovies) {
        this.favoriteMovies.clear();
        this.favoriteMovies.addAll(favoriteMovies);
        notifyDataSetChanged();

//        Log updated list for debugging purposes
        Log.d("TEST", favoriteMovies.toString());
    }


    @Override
    public void onMovieClick(int position) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerViewRowBinding binding;
        public ViewHolder (@NonNull RecyclerViewRowBinding binding, MovieRecyclerViewInterface movieRecyclerViewInterface) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (movieRecyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            movieRecyclerViewInterface.onMovieClick(pos);
                        }
                    }
                }
            });
        }
    }
}
