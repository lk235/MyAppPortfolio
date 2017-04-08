package com.example.android.myappportfolio.topMovies.data;

import android.provider.BaseColumns;

/**
 * Created by lk235 on 2017/4/8.
 * Defines table and column names for the moive database.
 */

public class MovieContract {

    public static final class MovieEntry implements BaseColumns{
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_IMAGE_URL = "imageUrl";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTE = "vote";
        public static final String COLUMN_OVER_VIEW = "overview";
    }

}
