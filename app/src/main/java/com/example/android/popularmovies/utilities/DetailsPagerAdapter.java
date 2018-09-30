package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.popularmovies.DetailsFragments.PlotSynopsisFragment;
import com.example.android.popularmovies.DetailsFragments.TrailersFragment;
import com.example.android.popularmovies.DetailsFragments.UserReviewsFragment;
import com.example.android.popularmovies.Models.Movie;
import com.example.android.popularmovies.R;

public class DetailsPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private Movie mCurrentMovie;

    public DetailsPagerAdapter(Context context, FragmentManager fm, Movie currentMovie) {
        super(fm);
        mContext = context;
        mCurrentMovie = currentMovie;

    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment;

        switch (position) {
            case 0:
                fragment = new PlotSynopsisFragment();
                break;
            case 1:
                fragment = new UserReviewsFragment();
                break;
            default:
                fragment = new TrailersFragment();
        }

        Bundle bundle = new Bundle();
        bundle.putString("movie_overview", mCurrentMovie.getOverview());
        bundle.putInt("movie_ID", mCurrentMovie.getMovieID());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.synopsis);
            case 1:
                return mContext.getString(R.string.reviews);
            default:
                return mContext.getString(R.string.trailers);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
