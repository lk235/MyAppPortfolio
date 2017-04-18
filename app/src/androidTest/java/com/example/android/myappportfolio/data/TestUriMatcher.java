/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.myappportfolio.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.example.android.myappportfolio.topMovies.data.MovieContract;
import com.example.android.myappportfolio.topMovies.data.MovieProvider;

/*
    Uncomment this class when you are ready to test your UriMatcher.  Note that this class utilizes
    constants that are declared with package protection inside of the UriMatcher, which is why
    the test must be in the same data package as the Android app code.  Doing the test this way is
    a nice compromise between data hiding and testability.
 */
public class TestUriMatcher extends AndroidTestCase {
    private static final String CATEGORY_QUERY = "popular";


    // content://com.example.android.myappportfolio/Moive"
    private static final Uri TEST_Moive_DIR = MovieContract.MovieEntry.CONTENT_URI;
    private static final Uri TEST_Moive_BY_CATEGORY_DIR = MovieContract.MovieEntry.buildMovieByCategory(CATEGORY_QUERY);
    private static final Uri TEST_Moive_WITH_ID_ITEM = MovieContract.MovieEntry.buildMovieUri(14);
    //private static final Uri TEST_Moive_WITH_ID_ITEM = MovieContract.MovieEntry.buildMovieByID(4);
   // private static final Uri TEST_Moive_WITH_ID_ITEM = Uri.parse("content://com.example.android.myappportfolio.topMovies/movie/4");




    /*
        Students: This function tests that your UriMatcher returns the correct integer value
        for each of the Uri types that our ContentProvider can handle.  Uncomment this when you are
        ready to test your UriMatcher.
     */
    public void  testUriMatcher() {
        UriMatcher testMatcher = MovieProvider.buildUriMatcher();

        assertEquals("Error: The Moive URI was matched incorrectly.",
                testMatcher.match(TEST_Moive_DIR), MovieProvider.MOVIE);
        assertEquals("Error: The Moive WITH CATEGROY URI was matched incorrectly.",
                testMatcher.match(TEST_Moive_BY_CATEGORY_DIR), MovieProvider.MOVIE_BY_CATEGORY);
        assertEquals("Error: The Moive WITH ID URI was matched incorrectly.",
                testMatcher.match(TEST_Moive_WITH_ID_ITEM), MovieProvider.MOVIE_WITH_ID);

    }
}
