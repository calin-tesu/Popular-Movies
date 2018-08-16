package com.example.android.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.Movie;
import com.example.android.popularmovies.utilities.MovieAdapter;
import com.example.android.popularmovies.utilities.QueryUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TMDB_BASE_URL = "http://api.themoviedb.org/3/movie/";

    //Please provide your own API Key to access TMDB
    private static final String API_KEY = "xxx";

    //API end-point for fetching the most popular movies
    private static final String POPULARITY = "popular";

    //API end_point for fetching the top rated movies
    private static final String TOP_RATED = "top_rated";

    String orderMoviesBy;

    private RecyclerView mRecyclerView;

    private TextView mErrorMessage;

    private ProgressBar mLoadingIndicator;

    private MovieAdapter mMovieAdapter;

    private Movie[] moviesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movie_posters);

        mErrorMessage = findViewById(R.id.empty_view);

        mLoadingIndicator = findViewById(R.id.loading_indicator);

        mRecyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        orderMoviesBy = POPULARITY;

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            new FetchMoviesTask().execute();
        } else {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mErrorMessage.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.most_popular) {
            orderMoviesBy = POPULARITY;
            new FetchMoviesTask().execute();
        } else if (id == R.id.highest_rated) {
            orderMoviesBy = TOP_RATED;
            new FetchMoviesTask().execute();
        }

        return super.onOptionsItemSelected(item);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {

        /**
         * Runs on the UI thread before {@link #doInBackground}.
         *
         * @see #onPostExecute
         * @see #doInBackground
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {

            Uri builtUri = Uri.parse(TMDB_BASE_URL + orderMoviesBy).buildUpon()
                    .appendQueryParameter("api_key", API_KEY)
                    .build();

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

        @Override
        protected void onPostExecute(Movie[] movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mMovieAdapter = new MovieAdapter(moviesArray);
            mRecyclerView.setAdapter(mMovieAdapter);
        }
    }
}
