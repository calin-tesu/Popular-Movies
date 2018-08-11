package com.example.android.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.popularmovies.utilities.Movie;
import com.example.android.popularmovies.utilities.QueryUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private Movie[] moviesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movie_posters);

        mRecyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        new FetchMoviesTask().execute();

    }

    public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {


        @Override
        protected Movie[] doInBackground(String... params) {

            Uri builtUri = Uri.parse("http://api.themoviedb.org/3/movie/popular?api_key=your API key");
            URL url = null;

            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                String jsonMovieResponse = QueryUtils.getResponseFromHttpUrl(url);

                moviesArray = QueryUtils.extractMoviesFeaturesFromJson(jsonMovieResponse);

                return moviesArray;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
