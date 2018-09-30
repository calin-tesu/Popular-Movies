package com.example.android.popularmovies.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.popularmovies.Models.Movie;

import java.util.List;

@Dao
public interface FavoriteMoviesDao {

    @Query("SELECT * FROM favorite_movies ORDER BY vote_average ")
    LiveData<List<Movie>> loadAllFavoriteMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteMovie(Movie movie);

    @Delete
    void deleteFavoriteMovie(Movie movie);

    @Query("SELECT * FROM favorite_movies WHERE movie_id = :id")
    LiveData<Movie> loadMovieById(int id);
}
