package com.example.android.myappportfolio.topMovies;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myappportfolio.R;
import com.example.android.myappportfolio.topMovies.data.MovieContract;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoiveDetailFragment extends Fragment {
    private Movie movie;
    private Uri mMovieUri;
    private TextView mMovieTitleTextView;
    private ImageView mMoiveImageView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private TextView mOverViewTextView;



    public MoiveDetailFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mMovieTitleTextView = (TextView)rootView.findViewById(R.id.movie_title_text_view);
        mMoiveImageView = (ImageView)rootView.findViewById(R.id.moive_image_view);
        mReleaseDateTextView = (TextView)rootView.findViewById(R.id.release_date_text_view);
        mVoteAverageTextView = (TextView)rootView.findViewById(R.id.vote_average_text_view);
        mOverViewTextView = (TextView)rootView.findViewById(R.id.overview_text_view);

        //movie = getActivity().getIntent().getParcelableExtra(MovieListFragment.MOVIE_EXTRA);
        Intent intent = getActivity().getIntent();
        if (intent != null){
            mMovieUri = intent.getData();

        }

        if(mMovieUri != null){
            Cursor cursor = getActivity().getContentResolver().query(mMovieUri,MovieListFragment.MOVIE_COLUMNS,
                    null,
                    null,
                    null,
                    null
                    );
            String imageUrl = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE_URL));
//            mMovieTitleTextView.setText(movie.getTitle());
            Picasso.with(getActivity())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_sync_black_24dp)
                    .into(mMoiveImageView);
//            mReleaseDateTextView.setText(movie.getRelease_date());
//            mVoteAverageTextView.setText(movie.getVote());
//            mOverViewTextView.setText(movie.getOverview());
        }
       ;



        return rootView;

    }

}
