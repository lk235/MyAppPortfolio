package com.example.android.myappportfolio.topMovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.myappportfolio.R;
import com.example.android.myappportfolio.topMovies.sync.MovieSyncAdapter;


public class MainActivity extends AppCompatActivity implements MovieListFragment.Callback {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    //public static final String LIST_FRAGMENT_TAG = "list_fragment";
    public static final String DETAIL_FRAGMENT_TAG = "detail_fragment";
    public static String mLastCateGroySetting;
    private boolean mTwoPane;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLastCateGroySetting = getPreSetting();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_main);
        if(findViewById(R.id.container_detail) != null){
            mTwoPane = true;
            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_detail, new MoiveDetailFragment(), DETAIL_FRAGMENT_TAG)
                        .commit();

            }

        }else {
            mTwoPane = false;
        }
//        if(savedInstanceState == null){
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container_top_movies, new MovieListFragment(), MOVIE_FRAGMENT_TAG)
//                    .commit();
//        }

        MovieSyncAdapter.initializeSyncAdapter(this);
    }


    @Override
    protected void onResume() {
        super.onResume();

        String sortTypeSetting = getPreSetting();
        if (  !sortTypeSetting.equals(mLastCateGroySetting)) {
            mLastCateGroySetting = sortTypeSetting;
            Log.i("NOT EQUALS", "");
            MovieListFragment mf = (MovieListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_movie_main);
            if ( null != mf ) {
                mf.onSettingChanged();
            }
            MoiveDetailFragment df = (MoiveDetailFragment) getSupportFragmentManager().findFragmentByTag(DETAIL_FRAGMENT_TAG);
            if( null != df){
                df.onSettingChanged();
            }

        }
    }

    private String getPreSetting() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String cateGroySetting = sharedPreferences.getString(getString(R.string.pref_sort_key), getString((R.string.pref_sort_default)));
        return cateGroySetting;
    }

    @Override
    public void onItemSelected(Uri contentUri) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(MoiveDetailFragment.DETAIL_URI, contentUri);

            MoiveDetailFragment fragment = new MoiveDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_detail, fragment, DETAIL_FRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class)
                    .setData(contentUri);
            startActivity(intent);
        }
    }


}
