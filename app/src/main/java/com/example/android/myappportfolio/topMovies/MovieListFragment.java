package com.example.android.myappportfolio.topMovies;


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
import com.example.android.myappportfolio.topMovies.data.MovieDBHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.net.ssl.HttpsURLConnection;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int MOVIE_LOADER = 0;
    private static final String MOVIE_COLLECTED = "已收藏";

    public static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_CATEGROY_SETTING,
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
    // These indices are tied to MOVIE_COLUMNS
    public static final int COL_MOVIE_ID = 0;
    public static final int COL_MOVIE_CATEGROY_SETTING = 1;
    public static final int COL_IMAGE_URL = 2;
    public static final int COL_COLUMN_TITLE = 3;
    public static final int COL_COLUMN_RELEASE_DATE = 4;
    public static final int COL_COLUMN_VOTE = 5;
    public static final int COL_COLUMN_OVER_VIEW = 6;
    public static final int COL_COLUMN_RUNTIME = 7;
    public static final int COL_COLUMN_TRAILER_NAME = 8;
    public static final int COL_COLUMN_TRAILER_URL = 9;
    public static final int COL_COLUMN_COLLECTED = 10;


    public static final String NETWORK_NOT_CONNECTED = "network is not connted!";
    public static final String MOVIE_EXTRA = "movie extra";
    public MovieLab mMovieLab;
    public List<Movie> mMovies;

   // private RecyclerView mMovieListRecylerView;
    private GridView mGridView;
    private MovieAdapter mMovieAdapter;
    public  static String mLastCateGorySetting;





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

        mGridView = (GridView)rootView.findViewById(R.id.gridview_movie);
        mMovieLab  = MovieLab.get(getActivity());


        mMovieAdapter = new MovieAdapter(getActivity(), null, 0);
        mGridView.setAdapter(mMovieAdapter);
        Log.i("OnCreateView", "");
        mLastCateGorySetting = getPrefCateGorySetting();
        if(mMovieLab.isEmpty(getPrefCateGorySetting() )){
            checkNetworkAndFetchData();}


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                Log.i("TAG", cursor.toString());
                Log.i("TAG" , "" + cursor.getCount());
                if(cursor != null){
                    Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                    intent.setData(MovieContract.MovieEntry.buildMovieUri(cursor.getLong(COL_MOVIE_ID)));

                    startActivity(intent);
                }
            }
        });


        return rootView;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle){
        Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(getPrefCateGorySetting()).build();
        return  new CursorLoader(getActivity(),
                uri,
                MOVIE_COLUMNS,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor){
              mMovieAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader){
        mMovieAdapter.swapCursor(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.action_setting:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivityForResult(intent, 0);
                break;

            case R.id.action_refresh:
                checkNetworkAndFetchData();
                break;

            case R.id.action_colletced:
                Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(getPrefCateGorySetting())
                        .appendPath(MOVIE_COLLECTED).build();
                Cursor cursor = getActivity().getContentResolver().query(uri, MOVIE_COLUMNS, null, null, null);

                mMovieAdapter.swapCursor(cursor);



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



        new FetchMovieTask(getActivity(), mMovieLab).execute(getPrefCateGorySetting());


    }

    private   String getPrefCateGorySetting() {
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


    void onSettingChanged( ) {


        if(mMovieLab.isEmpty(getPrefCateGorySetting())){
            Log.i("FetchData", "");

            checkNetworkAndFetchData();
            getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
        }else {
            Log.i("RESTART LOADER", "");
            getLoaderManager().restartLoader(MOVIE_LOADER, null, this);

        }






    }
}





