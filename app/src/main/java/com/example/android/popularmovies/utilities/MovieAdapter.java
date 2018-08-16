package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.DetailsActivity;
import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private Movie[] moviesArray;

    private Context context;

    // Provide a suitable constructor
    public MovieAdapter(Movie[] movies) {
        moviesArray = movies;
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movies_grid_item, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {
        String posterBasePath = moviesArray[position].getPosterPath();
        String posterCompletePath = "http://image.tmdb.org/t/p/" + "w185/" + posterBasePath;

        Picasso.with(context)
                .load(posterCompletePath)
                .into(holder.mImageView);

    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return moviesArray.length;
    }

    /**
     * Cache of the children views for a movies list item.
     */
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.movie_poster);
            itemView.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie currentMovie = moviesArray[adapterPosition];
            Intent intentToStartDetailActivity = new Intent(context, DetailsActivity.class);
            intentToStartDetailActivity.putExtra("currentMovie", currentMovie);
            context.startActivity(intentToStartDetailActivity);
        }
    }
}
