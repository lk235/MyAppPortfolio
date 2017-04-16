package com.example.android.myappportfolio.topMovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.android.myappportfolio.topMovies.Movie;

/**
 * Created by lk235 on 2017/4/10.
 */

public class MovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDBHelper mOpenHelper;

    public static final int MOVIE = 100;
    public static final int MOVIE_BY_CATEGORY = 101;
    public static final int MOVIE_BY_ID = 102;


   // private static final SQLiteQueryBuilder sMovieByCATEGORYQueryBuilder = new SQLiteQueryBuilder();

    public static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIE_BY_CATEGORY);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", MOVIE_BY_ID);

        return matcher;

    }

    //categroy.categroy_setting = ?
    private static final String sCategroySettingSelection =
            MovieContract.MovieEntry.COLUMN_CATEGROY_SETTING + "=? ";

    private static final String sID =
            MovieContract.MovieEntry._ID + " =?";


    private Cursor getMovieByCategroySetting(Uri uri, String[] projection, String sortOrder) {
        String cateGroySetting = MovieContract.MovieEntry.getCategroySettingFromUri(uri);
        String selection = sCategroySettingSelection;
        String[] selectionArgs = new String[]{cateGroySetting};

        return mOpenHelper.getReadableDatabase().query(
                MovieContract.MovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);




    }

    private Cursor getMovieByID(Uri uri, String[] projection) {
        //String cateGroySetting = MovieContract.MovieEntry.getCategroySettingFromUri(uri);
        String selection = sID;
        String movie_id = MovieContract.MovieEntry.getIDFromUri(uri);
        String[] selectionArgs = new String[]{movie_id};

        return mOpenHelper.getReadableDatabase().query(
                MovieContract.MovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);




    }


    @Override
    public boolean onCreate(){
        mOpenHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri){
        final int match = sUriMatcher.match(uri);

        switch (match){
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_BY_CATEGORY:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case  MOVIE_BY_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw  new UnsupportedOperationException("Unknow uri" + uri);
        }

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder){
        Cursor returnCusor;
        switch (sUriMatcher.match(uri)){
            case MOVIE:
                returnCusor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder

                );
                break;

            case MOVIE_BY_CATEGORY:
                returnCusor = getMovieByCategroySetting(uri, projection, sortOrder);
                break;

            case MOVIE_BY_ID:
                returnCusor = getMovieByID(uri, projection);
                break;

            default:
                throw new UnsupportedOperationException("Unknow uri: " + uri);
            }
            returnCusor.setNotificationUri(getContext().getContentResolver(), uri);
        return  returnCusor;


        }


    @Override
    public Uri insert(Uri uri, ContentValues contentValues){
        final SQLiteDatabase database = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        if(match == MOVIE){

                long _id = database.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
                if(_id > 0)
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);

        }else
            throw new UnsupportedOperationException("Unknown uri: " + uri);

        getContext().getContentResolver().notifyChange(uri, null);
        return  returnUri;


    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs){
        final SQLiteDatabase database = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if(selection == null) selection = "1";

        if (match == MOVIE){
            rowsDeleted = database.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
        }else
            throw new UnsupportedOperationException("Unknow uri: " + uri);

        if(rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){
        final SQLiteDatabase database = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsInsert;

        if(match == MOVIE)
            rowsInsert = database.update(MovieContract.MovieEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        else
            throw new UnsupportedOperationException("Unknow uri: " + uri );

        if(rowsInsert != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsInsert;


    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        if (match == MOVIE) {

                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;


        }else
            return super.bulkInsert(uri, values);
    }



}
