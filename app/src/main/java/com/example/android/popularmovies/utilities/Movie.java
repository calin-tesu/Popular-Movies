package com.example.android.popularmovies.utilities;

public class Movie {

    private String title;
    private int movie_ID;
    private String releaseDate;
    private int vote_average;
    private String overview;

    public Movie(String title, int movie_ID, String releaseDate, int vote_average, String overview) {
        this.title = title;
        this.movie_ID = movie_ID;
        this.releaseDate = releaseDate;
        this.vote_average = vote_average;
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public int getMovie_ID() {
        return movie_ID;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getVote_average() {
        return vote_average;
    }

    public String getOverview() {
        return overview;
    }
}
