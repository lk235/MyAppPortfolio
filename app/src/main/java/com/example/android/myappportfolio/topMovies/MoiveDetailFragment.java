package com.example.android.myappportfolio.topMovies;


import android.content.AsyncQueryHandler;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.myappportfolio.R;
import com.example.android.myappportfolio.topMovies.data.MovieContract;
import com.example.android.myappportfolio.topMovies.sync.MovieSyncAdapter;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoiveDetailFragment extends Fragment  {

    private static final int DETAIL_LOADER = 0;
    public static final String DETAIL_URI = "URI";
    private static final String MOVIE_COLLECT = "收藏";
    private static final String MOVIE_COLLECTED = "已收藏";
    public static final String MOVIE_TRAILER_NAME = "movie_trailer_name";
    public static final String MOVIE_TRAILER_URL_DATA = "movie_trailer_data";
    public static final String MOVIE_REVIEW_AUTHOR = "movie_review_author";
    public static final String MOVIE_REVIEW_CONTENT = "movie_review_content";


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
            MovieContract.MovieEntry.COLUMN_REVIEW_AUTHOR,
            MovieContract.MovieEntry.COLUMN_REVIEW_CONTENT,
            MovieContract.MovieEntry.COLUMN_COLLECTED
    };

    private static final int COL_MOVIE_ID = 0;
    private static final int COL_IMAGE_URL = 1;
    private static final int COL_TITLE = 2;
    private static final int COL_RELEASE_DATE = 3;
    private static final int COL_VOTE = 4;
    private static final int COL_OVERVIEW = 5;
    private static final int COL_RUNTIME = 6;
    private static final int COL_TRAILER_NAME = 7;
    private static final int COL_TRAILER_URL = 8;
    private static final int COL_REVIEW_AUTHOR = 9;
    private static final int COL_REVIEW_CONTENT = 10;
    private static final int COL_COLLECTED = 11;

    private TextView mMovieTitleTextView;
    private ImageView mMoiveImageView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private TextView mOverViewTextView;
    private TextView mRunTimeTextView;
    private Button mCollectButton;

    private ListView mTrailerListView;
    private TextView mReviewsTextView;

    private MovieTrailerAdapter trailerAdapter;
    private AsyncQueryHandler mQueryHandler;
    private Uri mUri;
    private Uri intentUri;
    private String mLastSorTypeSetting;



    public MoiveDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume(){
        super.onResume();

        getActivity().registerReceiver(syncFinishedReceiver, new IntentFilter(MovieSyncAdapter.SYNC_DONE));
        if( !mLastSorTypeSetting.equals(Utility.getPrefSortSetting(getActivity()))){
            UpdateTwoPaneDetailView();
        }
        mLastSorTypeSetting = Utility.getPrefSortSetting(getActivity());
    }

    @Override
    public void onPause(){
        super.onPause();
        getActivity().unregisterReceiver(syncFinishedReceiver);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // mUri = MovieContract.MovieEntry.buildMovieUri(0);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DETAIL_URI);
        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_trailer, container, false);
        mTrailerListView = (ListView) rootView.findViewById(R.id.trailer_list_view);

        View detailView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_movie_detail, mTrailerListView, false);
        View reviewsView = LayoutInflater.from(getActivity()).inflate(R.layout.movie_reviews, mTrailerListView, false);

        mTrailerListView.addHeaderView(detailView);
        mTrailerListView.addFooterView(reviewsView);
        mMovieTitleTextView = (TextView) detailView.findViewById(R.id.movie_title_text_view);
        mMoiveImageView = (ImageView) detailView.findViewById(R.id.moive_image_view);
        mReleaseDateTextView = (TextView) detailView.findViewById(R.id.release_date_text_view);
        mVoteAverageTextView = (TextView) detailView.findViewById(R.id.vote_average_text_view);
        mOverViewTextView = (TextView) detailView.findViewById(R.id.overview_text_view);
        mRunTimeTextView = (TextView) detailView.findViewById(R.id.runtime_text_view);
        mCollectButton = (Button) detailView.findViewById(R.id.movie_collect_button);
        mReviewsTextView = (TextView) reviewsView.findViewById(R.id.movie_review_text_view);
        mLastSorTypeSetting = Utility.getPrefSortSetting(getActivity());

//        mTrailerButton = (Button) rootView.findViewById(R.id.movie_trailer_button);
//        mReviewsButton = (Button) rootView.findViewById(R.id.movie_reviews_button);


        mQueryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {


                if (cursor == null || cursor.getCount() == 0) {
                     return;
                }else {
                    cursor.moveToFirst();
                    Picasso.with(getActivity())
                            .load(cursor.getString(COL_IMAGE_URL))
                            .placeholder(R.drawable.ic_sync_black_24dp)
                            .into(mMoiveImageView);


                    mMovieTitleTextView.setText(cursor.getString(COL_TITLE));
                    mReleaseDateTextView.setText(cursor.getString(COL_RELEASE_DATE));
                    mVoteAverageTextView.setText(cursor.getString(COL_VOTE));
                    mOverViewTextView.setText(cursor.getString(COL_OVERVIEW));
                    mRunTimeTextView.setText(cursor.getString(COL_RUNTIME));
                    mCollectButton.setText(cursor.getString(COL_COLLECTED));

                    String[] trailerName = Utility.convertStringToArray(cursor.getString(COL_TRAILER_NAME));
                    final String[] trailerUrls = Utility.convertStringToArray(cursor.getString(COL_TRAILER_URL));

                    trailerAdapter = new MovieTrailerAdapter(getActivity(), R.layout.movie_trailer_item, trailerName);
                    mTrailerListView.setAdapter(trailerAdapter);
                    mTrailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrls[position]));
                            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                startActivity(intent);
                            }

                        }
                    });

                    String[] reviewAuthor = Utility.convertStringToArray(cursor.getString(COL_REVIEW_AUTHOR));
                    String[] reviewContent = Utility.convertStringToArray(cursor.getString(COL_REVIEW_CONTENT));
                    String[] reviewMixed = new String[reviewAuthor.length];
                    for (int i = 0; i < reviewAuthor.length; i++) {
                        reviewMixed[i] = "REVIEW BY: " + reviewAuthor[i] + "\n" + reviewContent[i] + "\n\n";
                    }

                    mReviewsTextView.setText(Utility.convertArrayToString(reviewMixed)
                            .replace("\\n", System.getProperty("line.separator")));


                }

            }




        };

        Intent intent = getActivity().getIntent();
        intentUri = intent.getData();
        if(intent == null ||  intentUri == null){
            if(null == mUri){
                UpdateTwoPaneDetailView();

            }else {
                mQueryHandler.startQuery(1, null, mUri, MOVIE_COLUMNS, null, null, null);
            }

        }else {
            mQueryHandler.startQuery(1, null, intentUri, MOVIE_COLUMNS, null, null, null);
        }




        mCollectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri uri;
                if(intentUri == null){
                    uri = mUri;
                }else {
                    uri = intentUri;
                }
                switch (mCollectButton.getText().toString()) {
                    case MOVIE_COLLECT:
                        mCollectButton.setText(R.string.movie_colleted);
                        mQueryHandler.startUpdate(1, null, uri, getCollectedValues(), null, null);
                        break;

                    case MOVIE_COLLECTED:
                        mCollectButton.setText(R.string.movie_collect);
                        mQueryHandler.startUpdate(1, null, uri, getCollectValues(), null ,null);
                        break;


                }

            }
        });


        return rootView;
    }





    public  ContentValues getCollectValues() {
        ContentValues collectValues = new ContentValues();
        collectValues.put(MovieContract.MovieEntry.COLUMN_COLLECTED, MOVIE_COLLECT);
        return collectValues;
    }

    public  ContentValues getCollectedValues() {
        ContentValues collectValues = new ContentValues();
        collectValues.put(MovieContract.MovieEntry.COLUMN_COLLECTED, MOVIE_COLLECTED);
        return collectValues;
    }





    void onSettingChanged(){
        //String currentSetting = Utility.getPrefSortSetting(getActivity());
       if(mUri != null)
        mQueryHandler.startQuery(1, null, mUri, MOVIE_COLUMNS, null, null, null);



    }

    private BroadcastReceiver syncFinishedReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intentUri == null)
            UpdateTwoPaneDetailView();
        }
    };

    private void UpdateTwoPaneDetailView() {
        Uri uriBySort = MovieContract.MovieEntry.buildMovieByCategory(Utility.getPrefSortSetting(getActivity()));
        mQueryHandler.startQuery(1, null, uriBySort, MOVIE_COLUMNS, null, null, null);
    }


}



