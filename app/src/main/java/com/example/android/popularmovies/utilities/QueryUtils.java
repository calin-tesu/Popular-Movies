package com.example.android.popularmovies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public final class QueryUtils {

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static Movie[] extractMoviesFeaturesFromJson(String jsonMovieResponse) {

        Movie[] movies = null;

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJson = new JSONObject(jsonMovieResponse);

            //Extract the JSONArray associated with the key called "results
            JSONArray moviesArray = baseJson.getJSONArray("results");

            movies = new Movie[moviesArray.length()];

            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject currentMovie = moviesArray.getJSONObject(i);
                String title = currentMovie.getString("title");
                int movieID = currentMovie.getInt("id");
                String releaseDate = currentMovie.getString("release_date");
                int voteAverage = currentMovie.getInt("vote_average");
                String overview = currentMovie.getString("overview");
                String posterPath = currentMovie.getString("poster_path");

                movies[i] = new Movie(title, movieID, releaseDate, voteAverage, overview, posterPath);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }
}
