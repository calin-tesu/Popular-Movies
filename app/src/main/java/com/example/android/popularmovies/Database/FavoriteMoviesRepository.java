package com.example.android.popularmovies.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.android.popularmovies.Models.Movie;

import java.util.List;

public class FavoriteMoviesRepository {

    private static final int INSERT_TASK = 1;
    private static final int DELETE_TASK = 2;

    private FavoriteMoviesDao mFavoriteMoviesDao;
    private LiveData<List<Movie>> mAllFavoriteMoviesList;

    public FavoriteMoviesRepository(Application application) {
        FavoriteMoviesDatabase db = FavoriteMoviesDatabase.getDatabase(application);
        mFavoriteMoviesDao = db.favoriteMoviesDao();
        mAllFavoriteMoviesList = mFavoriteMoviesDao.loadAllFavoriteMovies();
    }

    public LiveData<List<Movie>> getAllFavoriteMovies() {
        return mAllFavoriteMoviesList;
    }

    public void addMovieToFavorites(Movie movie) {
        new FavoriteMovieAsyncTask(mFavoriteMoviesDao, INSERT_TASK).execute(movie);
    }

    public void removeMovieFromFavorites(Movie movie) {
        new FavoriteMovieAsyncTask(mFavoriteMoviesDao, DELETE_TASK).execute(movie);
    }

    private static class FavoriteMovieAsyncTask extends AsyncTask<Movie, Void, Void> {

        private FavoriteMoviesDao mAsyncTaskDao;
        private int taskAction;

        FavoriteMovieAsyncTask(FavoriteMoviesDao dao, int task) {
            mAsyncTaskDao = dao;
            taskAction = task;
        }

        @Override
        protected Void doInBackground(Movie... params) {
            switch (taskAction) {
                case INSERT_TASK:
                    mAsyncTaskDao.insertFavoriteMovie(params[0]);
                    break;
                case DELETE_TASK:
                    mAsyncTaskDao.deleteFavoriteMovie(params[0]);
                    break;
            }
            return null;
        }
    }
}
