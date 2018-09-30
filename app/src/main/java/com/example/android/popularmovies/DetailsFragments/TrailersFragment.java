package com.example.android.popularmovies.DetailsFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.popularmovies.Constants;
import com.example.android.popularmovies.Models.Trailer;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.utilities.QueryUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class TrailersFragment extends Fragment {

    private View rootView;

    // Required empty public constructor
    public TrailersFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.users_review, container, false);

        new FetchTrailersTask().execute();

        return rootView;
    }

    private class FetchTrailersTask extends AsyncTask<String, Void, Trailer[]> {

        String[] trailerNamesArray;

        @Override
        protected Trailer[] doInBackground(String... params) {

            int movieID = getArguments().getInt("movie_ID");

            Uri builtUri = Uri.parse(Constants.TMDB_BASE_URL + movieID + "/videos").buildUpon()
                    .appendQueryParameter("api_key", Constants.API_KEY)
                    .build();

            URL url = null;

            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                String jsonReviewsResponse = QueryUtils.getResponseFromHttpUrl(url);

                Trailer[] trailersArray = QueryUtils.extractTrailersFromJson(jsonReviewsResponse);

                return trailersArray;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Trailer[] videosArray) {
            super.onPostExecute(videosArray);

            trailerNamesArray = new String[videosArray.length];

            for (int i = 0; i < videosArray.length; i++) {
                trailerNamesArray[i] = videosArray[i].getTrailerName();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, trailerNamesArray);

            // Simplification: Using a ListView instead of a RecyclerView
            ListView listView = rootView.findViewById(R.id.list_item);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    Uri builtUri = Uri.parse(Constants.YOUTUBE_BASE_URL + videosArray[position].getTrailerKey());

                    Intent intentViewTrailer = new Intent(Intent.ACTION_VIEW, builtUri);

                    // Verify that the intent will resolve to an activity
                    if (intentViewTrailer.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intentViewTrailer);
                    }
                }
            });
        }
    }
}
