package com.example.android.myappportfolio.topMovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.myappportfolio.R;

/**
 * Created by lk235 on 2017/4/27.
 */

public class MovieReviewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_detail, new MovieReviewsFragment())
                    .commit();
        }
    }
}
