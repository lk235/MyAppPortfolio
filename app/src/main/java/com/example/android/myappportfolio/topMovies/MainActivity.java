package com.example.android.myappportfolio.topMovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.myappportfolio.R;


public class MainActivity extends AppCompatActivity {

    public static final String MOVIE_FRAGMENT_TAG = "movie_fragment";
    public static String mLastCateGroySetting;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLastCateGroySetting = getPreSetting();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_main);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_top_movies, new MovieListFragment(), MOVIE_FRAGMENT_TAG)
                    .commit();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i("RESUME", "RESUME");
        String sortTypeSetting = getPreSetting();
        Log.i("OLD", mLastCateGroySetting);
        Log.i("NEW", sortTypeSetting);
        Log.i("BOOLEAN", "" + (!sortTypeSetting.equals(mLastCateGroySetting)));


        if (  !sortTypeSetting.equals(mLastCateGroySetting)) {
            mLastCateGroySetting = sortTypeSetting;
            Log.i("NOT EQUALS", "");
            MovieListFragment mf = (MovieListFragment) getSupportFragmentManager().findFragmentByTag(MOVIE_FRAGMENT_TAG);
            if ( null != mf ) {
                mf.onSettingChanged();
            }

        }
    }

    private String getPreSetting() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String cateGroySetting = sharedPreferences.getString(getString(R.string.pref_sort_key), getString((R.string.pref_sort_default)));
        return cateGroySetting;
    }


}
