package com.example.android.popularmovies.DetailsFragments;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.popularmovies.Constants;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.utilities.QueryUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class UserReviewsFragment extends Fragment {

    private View rootView;

    // Required empty public constructor
    public UserReviewsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.users_review, container, false);

        new FetchReviewsTask().execute();

        return rootView;
    }

    private class FetchReviewsTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {

            int movieID = getArguments().getInt("movie_ID");

            Uri builtUri = Uri.parse(Constants.TMDB_BASE_URL + movieID + "/reviews").buildUpon()
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

                String[] reviewsArray = QueryUtils.extractReviewsFromJson(jsonReviewsResponse);

                return reviewsArray;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         *
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param strings The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, strings);

            // Simplification: Using a ListView instead of a RecyclerView
            ListView listView = rootView.findViewById(R.id.list_item);
            listView.setAdapter(adapter);
        }
    }
}
