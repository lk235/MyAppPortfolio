package com.example.android.myappportfolio.topMovies;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
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
public class MoiveDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER = 0;
    private ShareActionProvider mShareActionProvider;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_IMAGE_URL,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_VOTE,
            MovieContract.MovieEntry.COLUMN_OVER_VIEW
    };

    private static final int COL_MOVIE_ID = 0;
    private static final int COL_IMAGE_URL = 1;
    private static final int COL_TITLE = 2;
    private static final int COL_RELEASE_DATE = 3;
    private static final int COL_VOTE = 4;
    private static final int COL_OVERVIEW = 5;

    private String[] movie;
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
        mMovieTitleTextView = (TextView) rootView.findViewById(R.id.movie_title_text_view);
        mMoiveImageView = (ImageView) rootView.findViewById(R.id.moive_image_view);
        mReleaseDateTextView = (TextView) rootView.findViewById(R.id.release_date_text_view);
        mVoteAverageTextView = (TextView) rootView.findViewById(R.id.vote_average_text_view);
        mOverViewTextView = (TextView) rootView.findViewById(R.id.overview_text_view);

        //movie = getActivity().getIntent().getParcelableExtra(MovieListFragment.MOVIE_EXTRA);
//        Intent intent = getActivity().getIntent();
//        if (intent != null){
//            String[] movie = intent.getStringArrayExtra(MovieListFragment.INTENT_MOVIE);
//            Picasso.with(getActivity())
//                        .load(movie[0])
//                        .placeholder(R.drawable.ic_sync_black_24dp)
//                        .into(mMoiveImageView);
//
//            mMovieTitleTextView.setText(movie[1]);
//            mReleaseDateTextView.setText(movie[2]);
//            mVoteAverageTextView.setText(movie[3]);
//            mOverViewTextView.setText(movie[4]);
//
//        }


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle ags) {
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }

        return new CursorLoader(getActivity(),
                intent.getData(),
                MOVIE_COLUMNS,
                null,
                null,
                null
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){
        if (!cursor.moveToFirst()){
            Log.i("ERROR","" + cursor.getCount());
            Log.i("ERROR", cursor.toString());
            return ;
        }

        Picasso.with(getActivity())
                        .load( cursor.getString(COL_IMAGE_URL))
                        .placeholder(R.drawable.ic_sync_black_24dp)
                        .into(mMoiveImageView);

            mMovieTitleTextView.setText(cursor.getString(COL_TITLE));
            mReleaseDateTextView.setText(cursor.getString(COL_RELEASE_DATE));
            mVoteAverageTextView.setText(cursor.getString(COL_VOTE));
            mOverViewTextView.setText(cursor.getString(COL_OVERVIEW));



    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){

    }


}



