package com.example.android.myappportfolio.topMovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.example.android.myappportfolio.R;
import com.example.android.myappportfolio.topMovies.data.MovieCursorWrapper;

import com.example.android.myappportfolio.topMovies.data.MovieContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lk235 on 2017/3/18.
 */

public class MovieLab {
    private static MovieLab sMovieLab;
    private Context mContext;
    //private List<Movie> mMovies;



    public static MovieLab get(Context context){
        if (sMovieLab == null){
            sMovieLab = new MovieLab(context);
        }
        return sMovieLab;
    }

    private MovieLab(Context context){
        //mMovies = new ArrayList<>();

        mContext = context.getApplicationContext();


    }

    private static ContentValues getContentValues(Movie movie){

        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_CATEGROY_SETTING, movie.getCategroy());
        values.put(MovieContract.MovieEntry.COLUMN_IMAGE_URL, movie.getImageUrl());
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getRelease_date());
        values.put(MovieContract.MovieEntry.COLUMN_VOTE, movie.getVote());
        values.put(MovieContract.MovieEntry.COLUMN_OVER_VIEW, movie.getOverview());
        values.put(MovieContract.MovieEntry.COLUMN_RUNTIME, movie.getRuntime());
        values.put(MovieContract.MovieEntry.COLUMN_TRAILER_NAME, movie.getTrailerName());
        values.put(MovieContract.MovieEntry.COLUMN_TRAILER_URL, movie.getTrailerUrl());
        values.put(MovieContract.MovieEntry.COLUMN_REVIEW_AUTHOR, movie.getReviewAuthor());
        values.put(MovieContract.MovieEntry.COLUMN_REVIEW_CONTENT, movie.getReviewContent());
        values.put(MovieContract.MovieEntry.COLUMN_REVIEW_URL, movie.getReviewUrl());
        values.put(MovieContract.MovieEntry.COLUMN_COLLECTED, movie.getColledted());

        return values;
    }

    public List<Movie> getmMovies(){
        //return mMovies;
        List<Movie> movies = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                MovieListFragment.MOVIE_COLUMNS,
                null,
                null,
                null,
                null);

        MovieCursorWrapper cursorWrapper = new MovieCursorWrapper(cursor);

        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                movies.add(cursorWrapper.getMovie());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return movies;
    }

    public Boolean isMovieExist( String categroySetting){

        //return mMovies.get(id);
        Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(categroySetting).build();
        Cursor cursor = mContext.getContentResolver().query(
                uri,
                null,
                null,
                new String[]{categroySetting},
                null,
                null
        );


        if(cursor.moveToFirst()){
            return true;
        }



       // return null;
        return null;
    }

    public void addMovie(Movie movie){
        //mMovies.add(movie);
        ContentValues values = getContentValues(movie);
        mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
    }

    public void deleteMovies(String categroySetting){
        //mMovies.clear();
        Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(categroySetting).build();
        mContext.getContentResolver().delete(uri,
                null,
                null);
    }

    public void clearAllMovies(){
        //mMovies.clear();

        mContext.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                null,
                null);
    }

    public boolean isEmpty(String categroySetting){
//        if(mMovies.size() > 0){
//            return true;
//        }else {
//            return false;
//        }
       Cursor cursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
               null,
               MovieContract.MovieEntry.COLUMN_CATEGROY_SETTING + " =? ",
               new String[] {categroySetting},
               null,
               null);

        if(cursor.getCount() == 0){
            return true;
        }else{
            return false;
        }
    }




}
