package com.example.android.myappportfolio.topMovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.android.myappportfolio.R;

/**
 * Created by lk235 on 2017/4/27.
 */

public class MovieTrailerFragment extends Fragment {

    private GridView mMovieTrailerListView;

    public MovieTrailerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_trailer, container, false);
        mMovieTrailerListView = (GridView) rootView.findViewById(R.id.trailer_grid_view);

        String[] movieTrailerName = getActivity().getIntent().getStringArrayExtra(MoiveDetailFragment.MOVIE_TRAILER_NAME);
        String[] movieTrailerUrl = getActivity().getIntent().getStringArrayExtra(MoiveDetailFragment.MOVIE_TRAILER_URL_DATA);

        ArrayAdapter<String> trailerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                movieTrailerName);

        mMovieTrailerListView.setAdapter(trailerAdapter);
        return rootView;


    }
}
