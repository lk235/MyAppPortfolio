package com.example.android.myappportfolio.topMovies;


import android.content.ContentValues;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.myappportfolio.R;
import com.example.android.myappportfolio.topMovies.data.MovieContract;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoiveDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER = 0;
    private static  final String MOVIE_COLLECT = "收藏";
    private static final String MOVIE_COLLECTED = "已收藏";



    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_IMAGE_URL,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_VOTE,
            MovieContract.MovieEntry.COLUMN_OVER_VIEW,
            MovieContract.MovieEntry.COLUMN_RUNTIME,
            MovieContract.MovieEntry.COLUMN_TRAILER_NAME,
            MovieContract.MovieEntry.COLUMN_TRAILER_URL,
            MovieContract.MovieEntry.COLUMN_COLLECTED
    };

    private static final int COL_MOVIE_ID = 0;
    private static final int COL_IMAGE_URL = 1;
    private static final int COL_TITLE = 2;
    private static final int COL_RELEASE_DATE = 3;
    private static final int COL_VOTE = 4;
    private static final int COL_OVERVIEW = 5;
    private static final int COL_RUNTIME = 6;
    private static final int COL_TRAILER_NAME= 7;
    private static final int COL_TRAILER_URL= 8;
    private static final int COL_COLLECTED = 9;

    private TextView mMovieTitleTextView;
    private ImageView mMoiveImageView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private TextView mOverViewTextView;
    private TextView mRunTimeTextView;
    private Button mCollectButton;
    private ListView mTrailerListView;




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
        mRunTimeTextView = (TextView) rootView.findViewById(R.id.runtime_text_view) ;
        mCollectButton = (Button) rootView.findViewById(R.id.movie_collect_button);
        mTrailerListView = (ListView) rootView.findViewById(R.id.trailer_list_view);




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
    public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor){
        if (!cursor.moveToFirst()){

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
            mRunTimeTextView.setText(cursor.getString(COL_RUNTIME));
            mCollectButton.setText(cursor.getString(COL_COLLECTED));

            mCollectButton.setOnClickListener(new View.OnClickListener() {
                Uri uri = MovieContract.MovieEntry.buildMovieUri(cursor.getLong(COL_MOVIE_ID));


                public ContentValues getCollectValues() {
                    ContentValues collectValues = new ContentValues();
                    collectValues.put(MovieContract.MovieEntry.COLUMN_COLLECTED, MOVIE_COLLECT);
                    return collectValues;
                }

                public ContentValues getCollectedValues() {
                    ContentValues collectValues = new ContentValues();
                    collectValues.put(MovieContract.MovieEntry.COLUMN_COLLECTED, MOVIE_COLLECTED);
                    return collectValues;
                }

                @Override
                public void onClick(View v) {
                    Log.i("button clicked", "" + mCollectButton.getText());
                    Log.i("getText" , getString(R.string.movie_collect));

                    switch (mCollectButton.getText().toString()){
                        case MOVIE_COLLECT :
                            mCollectButton.setText(R.string.movie_colleted);
                            getActivity().getContentResolver().update(uri, getCollectedValues(), null, null);
                            break;

                        case MOVIE_COLLECTED:
                            mCollectButton.setText(R.string.movie_collect);
                            getActivity().getContentResolver().update(uri, getCollectValues(), null, null);
                            break;

                        default:

                    }

                }
            });

       ;
        final String[] trailerNameData = FetchMovieTask.convertStringToArray(cursor.getString(COL_TRAILER_NAME));
        final String[] trailerUrlData =   FetchMovieTask.convertStringToArray(cursor.getString(COL_TRAILER_URL));



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                trailerNameData);

        mTrailerListView.setAdapter(adapter);
        ListAdapter listAdapter = mTrailerListView.getAdapter();
        int rows = listAdapter.getCount();
        int height = 150 * rows;

        ViewGroup.LayoutParams params = mTrailerListView.getLayoutParams();
        params.height = height;
        mTrailerListView.setLayoutParams(params);
        mTrailerListView.requestLayout();

        mTrailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrlData[position]) );
                    startActivity(intent);

            }
        });



    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){

    }




}



