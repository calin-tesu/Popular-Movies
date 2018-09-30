package com.example.android.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.popularmovies.Database.FavoriteMoviesRepository;
import com.example.android.popularmovies.Models.Movie;

import java.util.List;

public class FavoriteMovieViewModel extends AndroidViewModel {

    private FavoriteMoviesRepository mRepository;
    private LiveData<List<Movie>> mAllFavoriteMovies;

    public FavoriteMovieViewModel(@NonNull Application application) {
        super(application);
        mRepository = new FavoriteMoviesRepository(application);
        mAllFavoriteMovies = mRepository.getAllFavoriteMovies();
    }

    LiveData<List<Movie>> getAllFavoriteMovies() {
        return mAllFavoriteMovies;
    }

    public void addMovieToFavorites(Movie movie) {
        mRepository.addMovieToFavorites(movie);
    }

    public void removeMovieFromFavorites(Movie movie) {
        mRepository.removeMovieFromFavorites(movie);
    }
}
