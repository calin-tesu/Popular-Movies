package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.Movie;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailsActivity extends AppCompatActivity {

    private TextView mTitle;
    private ImageView mPoster;
    private TextView mReleaseDate;
    private TextView mRating;
    private TextView mSynopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mTitle = findViewById(R.id.movie_title);
        mPoster = findViewById(R.id.movie_poster);
        mReleaseDate = findViewById(R.id.release_date);
        mRating = findViewById(R.id.rating);
        mSynopsis = findViewById(R.id.synopsis);

        Movie currentMovie = getIntent().getParcelableExtra("currentMovie");

        mTitle.setText(currentMovie.getTitle());
        mRating.setText(String.valueOf(currentMovie.getVoteAverage()) + "/10");
        mSynopsis.setText(currentMovie.getOverview());

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
                .load("http://image.tmdb.org/t/p/" + "w185/" + currentMovie.getPosterPath())
                .into(mPoster);
    }
}
