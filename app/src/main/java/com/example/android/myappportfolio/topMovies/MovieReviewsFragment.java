package com.example.android.myappportfolio.topMovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.example.android.myappportfolio.R;

/**
 * Created by lk235 on 2017/4/27.
 */

public class MovieReviewsFragment extends Fragment{

    public MovieReviewsFragment(){

    }

    private ListView mReviewsListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_trailer, container, false);
        mReviewsListView = (ListView) rootView.findViewById(R.id.reviews_list_view);

        String[] movieReviewAuthor = getActivity().getIntent().getStringArrayExtra(MoiveDetailFragment.MOVIE_REVIEW_AUTHOR);
        String[] movieReviewContent = getActivity().getIntent().getStringArrayExtra(MoiveDetailFragment.MOVIE_REVIEW_CONTENT);

        ArrayAdapter<String> trailerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.si,
                movieTrailerName);

        mReviewsListView.setAdapter(trailerAdapter);
        mReviewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movieTrailerUrl[position]));
                startActivity(intent);
            }
        });
        return rootView;


    }

     class ReviewsAdapter extends ArrayAdapter<String[]>{

         public ReviewsAdapter





    }
}
