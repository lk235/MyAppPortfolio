package com.example.android.myappportfolio.topMovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
        values.put(MovieContract.MovieEntry.COLUMN_CATEGROY_SETTING, "TEST");
        values.put(MovieContract.MovieEntry.COLUMN_IMAGE_URL, movie.getImageUrl());
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getRelease_date());
        values.put(MovieContract.MovieEntry.COLUMN_VOTE, movie.getVote());
        values.put(MovieContract.MovieEntry.COLUMN_OVER_VIEW, movie.getOverview());

        return values;
    }

    public List<Movie> getmMovies(){
        //return mMovies;
        List<Movie> movies = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
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

    public Movie getMovie( String title){

        //return mMovies.get(id);
        Cursor cursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
            MovieContract.MovieEntry.COLUMN_TITLE + " =? ",
                new String[]{title},
                null,
                null);



       // return null;
        return null;
    }

    public void addMovie(Movie movie){
        //mMovies.add(movie);
        ContentValues values = getContentValues(movie);
        mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
    }

    public void clearMovies(){
        //mMovies.clear();
        mContext.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                null,
                null);
    }

    public boolean isEmpty(){
//        if(mMovies.size() > 0){
//            return true;
//        }else {
//            return false;
//        }
       Cursor cursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
               null,
               null,
               null,
               null,
               null);

        if(cursor.getCount() == 0){
            return true;
        }else{
            return false;
        }
    }


}
