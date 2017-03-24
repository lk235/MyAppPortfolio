package com.example.android.myappportfolio.topMovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.myappportfolio.R;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_main);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_top_movies, new MovieListFragment())
                    .commit();
        }

    }


}
