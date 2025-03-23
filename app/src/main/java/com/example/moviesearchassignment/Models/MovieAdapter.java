package com.example.moviesearchassignment.Models;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviesearchassignment.R;
import com.example.moviesearchassignment.databinding.RecyclerViewRowBinding;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private final Context context;
    private final List<MovieExtraInfo> movieByIdList;

    public MovieAdapter(Context context, List<MovieExtraInfo> movieList) {
        this.context = context;
        this.movieByIdList = movieList;
    }


    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerViewRowBinding binding = RecyclerViewRowBinding.inflate(LayoutInflater.from(parent.getContext())
        ,parent,
        false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {
        MovieExtraInfo movieById = movieByIdList.get(position);
        Log.d("TEST", movieById.getTitle());

        // Set the correct fields
        holder.binding.titleView.setText(movieById.getTitle() != null ? movieById.getTitle() : "No title");
        holder.binding.yearView.setText(movieById.getYear() != null ? "Year: " + movieById.getYear() : "Year: N/A");
        holder.binding.studioView.setText(movieById.getStudio() != null ? "Studio: " + movieById.getStudio() : "Studio: N/A");
        holder.binding.ratingView.setText(movieById.getImdbRating() != null ? "Rating: " + movieById.getImdbRating() : "Rating: N/A");

        // Poster
        if (movieById.getPoster() != null && !movieById.getPoster().equals("N/A")) {
            Glide.with(context)
                    .load(movieById.getPoster())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.binding.posterView);
        } else {
            holder.binding.posterView.setImageResource(R.drawable.placeholder);
        }
    }


    @Override
    public int getItemCount() {
        return movieByIdList != null ? movieByIdList.size() : 0;
    }

    public void setMovies(List<MovieExtraInfo> moviesById) {
        movieByIdList.addAll(moviesById);
        notifyDataSetChanged();
        Log.d("RECEIVED", movieByIdList.toString());
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public Object Log;
        RecyclerViewRowBinding binding;
        public ViewHolder (@NonNull RecyclerViewRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
