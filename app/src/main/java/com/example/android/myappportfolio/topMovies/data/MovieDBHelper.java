package com.example.android.myappportfolio.topMovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.android.myappportfolio.topMovies.data.MovieContract.*;

/**
 * Created by lk235 on 2017/4/8.
 */

public class MovieDBHelper extends SQLiteOpenHelper{

    private static MovieDBHelper sInstance;

    private static final int DATEBASE_VRESION = 1;

    public static final String DATABASE_NAME = "movie.db";

    public static synchronized MovieDBHelper getInstance(Context context) {


        if (sInstance == null) {
            sInstance = new MovieDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private MovieDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATEBASE_VRESION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieEntry.COLUMN_CATEGROY_SETTING + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_IMAGE_URL + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_VOTE + " REAL NOT NULL, " +
                MovieEntry.COLUMN_OVER_VIEW + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RUNTIME + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_TRAILER_NAME + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_TRAILER_URL + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_REVIEW_AUTHOR + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_REVIEW_CONTENT + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_COLLECTED + " TEXT NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){
//        sqLiteDatabase.execSQL("DROP TABLE IF EXITS " + MovieEntry.TABLE_NAME);
//        onCreate(sqLiteDatabase);
    }

}
