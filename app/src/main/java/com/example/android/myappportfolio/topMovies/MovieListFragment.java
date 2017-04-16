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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
public class MovieListFragment extends Fragment {


    public static final String NETWORK_NOT_CONNECTED = "network is not connted!";
    public static final String MOVIE_EXTRA = "movie extra";
    public MovieLab mMovieLab;
    public List<Movie> mMovies;

   // private RecyclerView mMovieListRecylerView;
    private MovieAdapter mMovieAdapter;

    private String mLastSortType;



    public MovieListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart(){
        super.onStart();


        if(mMovieLab.isEmpty() || !getPrefSortType().equals(mLastSortType)){


            checkNetworkAndFetchData();
            mLastSortType = getPrefSortType();
        }else{

        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_main, container, false);
//        mMovieListRecylerView = (RecyclerView) rootView.findViewById(R.id.movie_list_recycler_view);
//        mMovieListRecylerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        GridView gridView = (GridView)rootView.findViewById(R.id.gridview_movie);
        mMovieLab  = MovieLab.get(getActivity());
        mMovies = mMovieLab.getmMovies();

        //mMovieAdapter = new MoiveAdapter(mMovieLab.getmMovies());
        mMovieAdapter = new MovieAdapter(getActivity(), R.layout.movie_item, mMovies);
        gridView.setAdapter(mMovieAdapter);
        Log.e("ORIGIN", "" + mMovieAdapter);

       // mMovieListRecylerView.setAdapter(mMovieAdapter);
       // checkNetworkAndFetchData();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_setting) {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivityForResult(intent, 0);
        }
        if (menuItem.getItemId() == R.id.action_refresh) {
            Log.i("REFRESH", "refresh");
            checkNetworkAndFetchData();
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


//    public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        private ImageView mMoiveImageView;
//
//        public MovieHolder(View itemView) {
//            super(itemView);
//            itemView.setOnClickListener(this);
//            mMoiveImageView = (ImageView) itemView.findViewById(R.id.movie_image_item);
//        }
//
//        public void bindMovieItem(Movie movieItem) {
//            Picasso.with(getActivity())
//                    .load(movieItem.getImageUrl())
//                    .placeholder(R.drawable.ic_sync_black_24dp)
//                    .error(R.drawable.ic_info_black_24dp)
//                    .into(mMoiveImageView);
//
//
//        }
//
//        @Override
//        public void onClick(View view) {
//            Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
//           // intent.putExtra(MOVIE_EXTRA, mMovieLab.getMovie(getAdapterPosition()));
//
//            startActivity(intent);
//
//
//        }
//    }
//
//    public class MoiveAdapter extends RecyclerView.Adapter<MovieHolder> {
//        private List<Movie> mMovies;
//
//        public MoiveAdapter(List<Movie> movies) {
//            mMovies = movies;
//        }
//
//        public void addMovie(List<Movie> movies){
//            mMovies = movies;
//        }
//
//        @Override
//        public MovieHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//            LayoutInflater inflater = LayoutInflater.from(getActivity());
//            final View view = inflater.inflate(R.layout.movie_item, viewGroup, false);
//            return new MovieHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(MovieHolder movieHolder, int positon) {
//            Movie movieItem = mMovies.get(positon);
//            movieHolder.bindMovieItem(movieItem);
//
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return mMovies.size();
//        }
//
//    }

    /**
     * This method get the sharedPreftrence value and call the asyncTask to get movie data.
     */
    private void updateMovieData() {



        new FetchMovieTask(getActivity(), mMovieAdapter, mMovieLab).execute(getPrefSortType());


    }

    private String getPrefSortType() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            }
        });
        String lastSortType = sharedPreferences.getString(getString(R.string.pref_sort_key), getString((R.string.pref_sort_default)));

        return lastSortType;
    }



    private ArrayList<String[]> getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_RESULTS = "results";
        final String OWM_POSTER_PATH = "poster_path";
        final String OWM_OVERVIEW = "overview";
        final String OWM_TITLE = "title";
        final String OWM_RELEASE_DATE = "release_date";
        final String OWM_VOTE_AVERAGE = "vote_average";

        ArrayList<String[]> resultStrs = new ArrayList<>();


        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(OWM_RESULTS);


        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject singleMovie = movieArray.getJSONObject(i);

            String[] moiveItem = new String[]{
                    singleMovie.getString(OWM_TITLE),
                    singleMovie.getString(OWM_POSTER_PATH),
                    singleMovie.getString(OWM_RELEASE_DATE),
                    singleMovie.getString(OWM_VOTE_AVERAGE),
                    singleMovie.getString(OWM_OVERVIEW)};

            resultStrs.add(moiveItem);

        }

        return resultStrs;

    }








    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}





