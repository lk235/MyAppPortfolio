package com.example.android.myappportfolio.topMovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by lk235 on 2017/4/8.
 * Defines table and column names for the moive database.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.myappportfolio.topMovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_CATEGROY_SETTING = "categroy_setting";
        public static final String COLUMN_IMAGE_URL = "imageUrl";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTE = "vote";
        public static final String COLUMN_OVER_VIEW = "overview";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_TRAILER_NAME = "trailer_name";
        public static final String COLUMN_TRAILER_URL = "trailer_url";
        public static final String COLUMN_REVIEW_AUTHOR = "review_author";
        public static final String COLUMN_REVIEW_CONTENT = "review_content";
        public static final String COLUMN_COLLECTED = "collected";





        public static Uri buildMovieByCategory(String category){
            return CONTENT_URI.buildUpon().appendPath(category).build();
        }





        public static String getCategroySettingFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getCollectedFromUri(Uri uri){
            return uri.getPathSegments().get(2);
        }

        public static String getIDFromUri(Uri uri) {
             ;
            return  uri.getPathSegments().get(1);
        }

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }



}
