package com.example.android.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.Models.Movie;
import com.example.android.popularmovies.utilities.MovieAdapter;
import com.example.android.popularmovies.utilities.QueryUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String orderMoviesBy;

    private RecyclerView mRecyclerView;

    private TextView mErrorMessage;

    private ProgressBar mLoadingIndicator;

    private MovieAdapter mMovieAdapter;

    private Movie[] moviesArray;

    private FavoriteMovieViewModel movieViewModel;

    private GridLayoutManager layoutManager;

    private NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movie_posters);

        mErrorMessage = findViewById(R.id.empty_view);

        mLoadingIndicator = findViewById(R.id.loading_indicator);

        mRecyclerView.setHasFixedSize(true);

        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                layoutManager = new GridLayoutManager(this, 2);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                layoutManager = new GridLayoutManager(this, 3);
                break;
        }

        mRecyclerView.setLayoutManager(layoutManager);

        orderMoviesBy = Constants.POPULARITY;

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (haveInternetConnection()) new FetchMoviesTask().execute();

        // Get a new or existing ViewModel from the ViewModelProvider.
        movieViewModel = ViewModelProviders.of(this).get(FavoriteMovieViewModel.class);

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
        switch (id) {
            case R.id.most_popular:
                orderMoviesBy = Constants.POPULARITY;
                // If there is a network connection, fetch data
                if (haveInternetConnection()) new FetchMoviesTask().execute();
                break;
            case R.id.highest_rated:
                orderMoviesBy = Constants.TOP_RATED;
                if (haveInternetConnection()) new FetchMoviesTask().execute();
                break;
            case R.id.favorites:
                mRecyclerView.setVisibility(View.VISIBLE);
                mErrorMessage.setVisibility(View.INVISIBLE);
                movieViewModel.getAllFavoriteMovies().observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(@Nullable List<Movie> movies) {
                        moviesArray = movies.toArray(new Movie[0]);
                        mMovieAdapter = new MovieAdapter(Arrays.asList(moviesArray));
                        mRecyclerView.setAdapter(mMovieAdapter);
                    }
                });
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean haveInternetConnection() {
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mErrorMessage.setVisibility(View.VISIBLE);
            return false;
        }
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

            Uri builtUri = Uri.parse(Constants.TMDB_BASE_URL + orderMoviesBy).buildUpon()
                    .appendQueryParameter("api_key", Constants.API_KEY)
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
            mMovieAdapter = new MovieAdapter(Arrays.asList(moviesArray));
            mRecyclerView.setAdapter(mMovieAdapter);
        }
    }
}
