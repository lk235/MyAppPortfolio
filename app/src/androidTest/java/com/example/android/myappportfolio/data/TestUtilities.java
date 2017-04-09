package com.example.android.myappportfolio.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;


import com.example.android.myappportfolio.topMovies.data.MovieContract;
import com.example.android.myappportfolio.topMovies.data.MovieDBHelper;



import java.util.Map;
import java.util.Set;

/*
    Students: These are functions and some test data to make it easier to test your database and
    Content Provider.  Note that you'll want your MovieContract class to exactly match the one
    in our solution to use these as-given.
 */
public class TestUtilities extends AndroidTestCase {


    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    /*
        Students: Use this to create some default Movie values for your database tests.
     */
    static ContentValues createMovieValues() {
        ContentValues MovieValues = new ContentValues();
        MovieValues.put(MovieContract.MovieEntry.COLUMN_IMAGE_URL, "www.themoviedb.org/");
        MovieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "title");
        MovieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, 2016-8-26);
        MovieValues.put(MovieContract.MovieEntry.COLUMN_VOTE, 9.5);
        MovieValues.put(MovieContract.MovieEntry.COLUMN_OVER_VIEW, "overview");


        return MovieValues;
    }



    /*
        Students: You can uncomment this function once you have finished creating the
        LocationEntry part of the MovieContract as well as the MovieDbHelper.
     */
    static long insertMovieValues(Context context) {
        // insert our test records into the database
        MovieDBHelper dbHelper = new MovieDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createMovieValues();

        long movieRowId;
        movieRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert North Pole Location Values", movieRowId != -1);

        return movieRowId;
    }

    /*
        Students: The functions we provide inside of TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
        CTS tests.

        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

//        public void waitForNotificationOrFail() {
//            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
//            // It's useful to look at the Android CTS source for ideas on how to test your Android
//            // applications.  The reason that PollingCheck works is that, by default, the JUnit
//            // testing framework is not running on the main Android application thread.
//            new PollingCheck(5000) {
//                @Override
//                protected boolean check() {
//                    return mContentChanged;
//                }
//            }.run();
//            mHT.quit();
//        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
