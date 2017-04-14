package com.example.android.myappportfolio.topMovies.data;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.android.myappportfolio.topMovies.Movie;

/**
 * Created by lk235 on 2017/4/13.
 */

public class MovieCursorWrapper extends CursorWrapper {
    public MovieCursorWrapper(Cursor cursor){
        super(cursor);
    }
    public Movie getMovie(){
        String categroy = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_CATEGROY_SETTING));
        String imageUrl = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE_URL));
        String title = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
        String release_date = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
        String vote = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE));
        String overview = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_OVER_VIEW));

        Movie movie = new Movie();
        movie.setCategroy(categroy);
        movie.setImageUrl(imageUrl);
        movie.setTitle(title);
        movie.setRelease_date(release_date);
        movie.setVote(vote);
        movie.setOverview(overview);

        return movie;
    }
}
