package com.example.android.popularmovies.DetailsFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;

public class PlotSynopsisFragment extends Fragment {

    // Required empty public constructor
    public PlotSynopsisFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.synopsis, container, false);

        String movieOverview = getArguments().getString("movie_overview");
        TextView plotSynopsisTV = rootView.findViewById(R.id.synopsis_tv);
        plotSynopsisTV.setText(movieOverview);

        return rootView;
    }
}
