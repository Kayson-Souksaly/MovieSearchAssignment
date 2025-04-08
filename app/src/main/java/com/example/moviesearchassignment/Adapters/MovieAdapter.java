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
 * Recycler view adapter for the movie list
 * */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> implements MovieRecyclerViewInterface {
    private final MovieRecyclerViewInterface movieRecyclerViewInterface;
    private final Context context;
    private final List<MovieExtraInfo> movieByIdList;

    public MovieAdapter(Context context, List<MovieExtraInfo> movieList, MovieRecyclerViewInterface movieRecyclerViewInterface) {
        this.context = context;
        this.movieByIdList = movieList;
        this.movieRecyclerViewInterface = movieRecyclerViewInterface;
    }


    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerViewRowBinding binding = RecyclerViewRowBinding.inflate(LayoutInflater.from(parent.getContext())
        ,parent,
        false);
        return new ViewHolder(binding, movieRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {
        MovieExtraInfo movieById = movieByIdList.get(position);
        Log.d("TEST", movieById.getTitle());

//      Set the correct fields
        holder.binding.titleView.setText(movieById.getTitle() != null ? movieById.getTitle() : "No title");
        holder.binding.yearView.setText(movieById.getYear() != null ? "Year: " + movieById.getYear() : "Year: N/A");
        holder.binding.studioView.setText(movieById.getStudio() != null ? "Studio: " + movieById.getStudio() : "Studio: N/A");
        holder.binding.ratingView.setText(movieById.getImdbRating() != null ? "Rating: " + movieById.getImdbRating() : "Rating: N/A");

//      Poster
        if (movieById.getPoster() != null && !movieById.getPoster().equals("N/A")) {
//          Glide allows to get images from a link,
//          using the the placeholder method as load a placeholder while retrieving
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
