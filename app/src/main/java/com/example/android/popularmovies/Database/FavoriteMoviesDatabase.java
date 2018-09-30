package com.example.android.popularmovies.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.popularmovies.Models.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class FavoriteMoviesDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "favorite_movies_db";
    private static FavoriteMoviesDatabase sInstance;

    public static FavoriteMoviesDatabase getDatabase(Context context) {
        if (sInstance == null) {
            synchronized (FavoriteMoviesDatabase.class) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavoriteMoviesDatabase.class, FavoriteMoviesDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract FavoriteMoviesDao favoriteMoviesDao();
}
