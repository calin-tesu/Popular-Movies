package com.example.android.popularmovies.Models;

public class Trailer {

    private String trailerName;
    private String trailerKey;

    public Trailer(String trailerName, String trailerKey) {
        this.trailerName = trailerName;
        this.trailerKey = trailerKey;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public String getTrailerKey() {
        return trailerKey;
    }
}
