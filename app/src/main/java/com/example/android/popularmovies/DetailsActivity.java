package com.example.android.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Models.Movie;
import com.example.android.popularmovies.utilities.DetailsPagerAdapter;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private ImageView mPoster;
    private TextView mReleaseDate;
    private TextView mRating;
    private FloatingActionButton mFloatingActionButton;

    private List<Movie> moviesList;
    private Movie currentMovie;

    private FavoriteMovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mPoster = findViewById(R.id.movie_poster);
        mReleaseDate = findViewById(R.id.release_date);
        mRating = findViewById(R.id.rating);
        mFloatingActionButton = findViewById(R.id.fab);

        // Get a new or existing ViewModel from the ViewModelProvider.
        movieViewModel = ViewModelProviders.of(DetailsActivity.this).get(FavoriteMovieViewModel.class);

        currentMovie = getIntent().getParcelableExtra("currentMovie");

        // Add an observer on the LiveData
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        movieViewModel.getAllFavoriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                moviesList = movies;
                if (moviesList == null || moviesList.size() == 0) {
                    mFloatingActionButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                } else {
                    for (int i = 0; i < moviesList.size(); i++) {
                        if (currentMovie.getMovieID() == moviesList.get(i).getMovieID()) {
                            mFloatingActionButton.setImageResource(R.drawable.ic_favorite_red_24dp);
                            break;
                            //else if current movie is not in the database then we will add it
                        } else {
                            mFloatingActionButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                        }
                    }
                }
            }
        });

        //Display the movie title in the action bar
        if (currentMovie.getTitle() == null || !TextUtils.isEmpty(currentMovie.getTitle())) {
            getSupportActionBar().setTitle(currentMovie.getTitle());
        }

        mRating.setText(String.valueOf(currentMovie.getVoteAverage()) + "/10");

        //try to parse the release date to display only the year
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("yyyy");
        try {
            Date date = inputFormat.parse(currentMovie.getReleaseDate());
            mReleaseDate.setText(outputFormat.format(date));
        } catch (ParseException e) {
            mReleaseDate.setText(currentMovie.getReleaseDate());
        }

        Picasso.with(this)
                .load(Constants.BASE_IMAGE_URL + currentMovie.getPosterPath())
                .into(mPoster);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moviesList == null || moviesList.size() == 0) {
                    movieViewModel.addMovieToFavorites(currentMovie);
                    mFloatingActionButton.setImageResource(R.drawable.ic_favorite_red_24dp);
                    Toast.makeText(DetailsActivity.this, "Added to favorite", Toast.LENGTH_LONG).show();
                } else {
                    for (int i = 0; i < moviesList.size(); i++) {
                        //if current movie is in the database then we will remove it
                        if (currentMovie.getMovieID() == moviesList.get(i).getMovieID()) {
                            movieViewModel.removeMovieFromFavorites(currentMovie);
                            //mFloatingActionButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(DetailsActivity.this, "Removed from favorite", Toast.LENGTH_LONG).show();
                            break;
                            //else if current movie is not in the database then we will add it
                        } else {
                            movieViewModel.addMovieToFavorites(currentMovie);
                            //mFloatingActionButton.setImageResource(R.drawable.ic_favorite_red_24dp);
                            Toast.makeText(DetailsActivity.this, "Added to favorite", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        // Create an adapter that knows which fragment should be shown on each page
        DetailsPagerAdapter adapter = new DetailsPagerAdapter(DetailsActivity.this, getSupportFragmentManager(), currentMovie);

        ViewPager viewPager = findViewById(R.id.viewpager);

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
