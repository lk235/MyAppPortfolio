package com.example.android.myappportfolio.topMovies;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.myappportfolio.R;
import com.example.android.myappportfolio.topMovies.data.MovieContract;

import com.example.android.myappportfolio.topMovies.sync.MovieSyncAdapter;

import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int MOVIE_LOADER = 0;

    public static final String SORT_BY_POPULAR = "popular";
    public static final String SORT_BY_TOP_RATED = "top_rated";
    public static final String SORT_BY_COLLECTED = "已收藏";
    private static final String sCollected =
            MovieContract.MovieEntry.COLUMN_COLLECTED + " =? ";
    private static final String sSortType =
            MovieContract.MovieEntry.COLUMN_CATEGROY_SETTING + " =? ";


    public static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_CATEGROY_SETTING,
            MovieContract.MovieEntry.COLUMN_IMAGE_URL,
//            MovieContract.MovieEntry.COLUMN_TITLE,
//            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
//            MovieContract.MovieEntry.COLUMN_VOTE,
//            MovieContract.MovieEntry.COLUMN_OVER_VIEW,
//            MovieContract.MovieEntry.COLUMN_RUNTIME,

            MovieContract.MovieEntry.COLUMN_COLLECTED

    };
    // These indices are tied to MOVIE_COLUMNS
    public static final int COL_MOVIE_ID = 0;
    public static final int COL_MOVIE_CATEGROY_SETTING = 1;
    public static final int COL_IMAGE_URL = 2;
    public static final int COL_COLUMN_TITLE = 3;
    public static final int COL_COLUMN_RELEASE_DATE = 4;
    public static final int COL_COLUMN_VOTE = 5;
    public static final int COL_COLUMN_OVER_VIEW = 6;
    public static final int COL_COLUMN_RUNTIME = 7;
    public static final int COL_COLUMN_COLLECTED = 8;


    public static final String NETWORK_NOT_CONNECTED = "network is not connted!";
    public static final String MOVIE_EXTRA = "movie extra";
    public MovieLab mMovieLab;
    public List<Movie> mMovies;

    // private RecyclerView mMovieListRecylerView;
    private GridView mGridView;
    private MovieAdapter mMovieAdapter;
    private String mLastSorTypeSetting;


    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }

    public MovieListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_main, container, false);

        mGridView = (GridView) rootView.findViewById(R.id.gridview_movie);
        mMovieLab = MovieLab.get(getActivity());


        mMovieAdapter = new MovieAdapter(getActivity(), null, 0);
        mGridView.setAdapter(mMovieAdapter);

        mLastSorTypeSetting = getPrefCateGorySetting();

        if( !mLastSorTypeSetting.equals(SORT_BY_COLLECTED) && mMovieLab.isEmpty(mLastSorTypeSetting)){
            checkNetworkAndFetchData();
        }



        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                Log.i("TAG", cursor.toString());
                Log.i("TAG", "" + cursor.getCount());
                if (cursor != null) {
//                    Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
//                    intent.setData(MovieContract.MovieEntry.buildMovieUri(cursor.getLong(COL_MOVIE_ID)));
//
//                    startActivity(intent);
                    ((Callback) getActivity())
                            .onItemSelected(MovieContract.MovieEntry.buildMovieUri(
                                    cursor.getLong(COL_MOVIE_ID))
                            );


                }
            }
        });

        Log.i("createVIEW", mLastSorTypeSetting);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(getPrefCateGorySetting()).build();
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;

        switch (getPrefCateGorySetting()) {
            case SORT_BY_POPULAR:
                return new CursorLoader(getActivity(),
                        uri,
                        MOVIE_COLUMNS,
                        sSortType,
                        new String[]{SORT_BY_POPULAR},
                        null
                );

            case SORT_BY_TOP_RATED:
                return new CursorLoader(getActivity(),
                        uri,
                        MOVIE_COLUMNS,
                        sSortType,
                        new String[]{SORT_BY_TOP_RATED},
                        null);

            case SORT_BY_COLLECTED:
                return new CursorLoader(getActivity(),
                        uri,
                        MOVIE_COLUMNS,
                        sCollected,
                        new String[]{SORT_BY_COLLECTED},
                        null);
            default:
                return null;
        }


    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mMovieAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mMovieAdapter.swapCursor(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
//        switch (menuItem.getItemId()) {
//            case R.id.action_setting:
//                Intent intent = new Intent(getActivity(), SettingActivity.class);
//                startActivityForResult(intent, 0);
//                break;
//
//            case R.id.action_refresh:
//                if(!getPrefCateGorySetting().equals(SORT_BY_COLLECTED)){
//                    checkNetworkAndFetchData();
//                }
//
//                break;
//
//        }
        if(menuItem.getItemId() == R.id.action_setting){
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivityForResult(intent, 0);
        }

        return super.onOptionsItemSelected(menuItem);
    }

    /**
     * This method check the networkState and fetch movie data.
     */
    private void checkNetworkAndFetchData() {
        if (isOnline()) {
            updateMovieData();
        } else {
            Toast.makeText(getActivity(), NETWORK_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * This method get the sharedPreftrence value and call the asyncTask to get movie data.
     */
    private void updateMovieData() {
//        Intent intent = new Intent(getActivity(), MovieService.class);
//        intent.putExtra(MovieService.SORT_SETTING, getPrefCateGorySetting());
//        getActivity().startService(intent);
       //new FetchMovieTask(getActivity(), mMovieLab).execute(getPrefCateGorySetting());

//        Intent alarmIntent = new Intent(getActivity(), MovieService.AlarmService.class);
//        alarmIntent.putExtra(MovieService.SORT_SETTING, Utility.getPrefSortSetting(getActivity()));
//
//        //Wrap in a pending intent which only fires once.
//        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0,alarmIntent,PendingIntent.FLAG_ONE_SHOT);//getBroadcast(context, 0, i, 0);
//
//        AlarmManager am=(AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
//
//        //Set the AlarmManager to wake up the system.
//        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 50000, pi);
        MovieSyncAdapter.syncImmediately(getActivity());


    }

    private String getPrefCateGorySetting() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            }
        });
        String cateGroySetting = sharedPreferences.getString(getString(R.string.pref_sort_key), getString((R.string.pref_sort_default)));

        return cateGroySetting;
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }


    void onSettingChanged() {
        String currentSetting = getPrefCateGorySetting();

        switch (currentSetting) {
            case SORT_BY_COLLECTED:
                getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
                break;
            case SORT_BY_POPULAR:
                updateLoader(currentSetting);
                break;
            case SORT_BY_TOP_RATED:
               updateLoader(currentSetting);
                break;


        }




    }

    private void updateLoader(String currentSetting) {
        if (mMovieLab.isEmpty(currentSetting)) {
            checkNetworkAndFetchData();
            getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
        } else {
            getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
        }
    }
}





