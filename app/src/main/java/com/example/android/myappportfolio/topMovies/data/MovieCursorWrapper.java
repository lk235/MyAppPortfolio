package com.example.android.myappportfolio.topMovies.data;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.android.myappportfolio.topMovies.Movie;
import com.example.android.myappportfolio.topMovies.MovieListFragment;

/**
 * Created by lk235 on 2017/4/13.
 */

public class MovieCursorWrapper extends CursorWrapper {
    public MovieCursorWrapper(Cursor cursor){
        super(cursor);
    }
    public Movie getMovie(){
//        String categroy = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_CATEGROY_SETTING));
//        String imageUrl = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE_URL));
//        String title = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
//        String release_date = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
//        String vote = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE));
//        String overview = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_OVER_VIEW));

        String categroy = getString(MovieListFragment.COL_MOVIE_CATEGROY_SETTING);
        String imageUrl = getString(MovieListFragment.COL_IMAGE_URL);
        String title = getString(MovieListFragment.COL_COLUMN_TITLE);
        String release_date = getString(MovieListFragment.COL_COLUMN_RELEASE_DATE);
        String vote = getString(MovieListFragment.COL_COLUMN_VOTE);
        String overview = getString(MovieListFragment.COL_COLUMN_OVER_VIEW);
        String runtime = getString(MovieListFragment.COL_COLUMN_RUNTIME);
        String collected = getString(MovieListFragment.COL_COLUMN_COLLECTED);


        Movie movie = new Movie();
        movie.setCategroy(categroy);
        movie.setImageUrl(imageUrl);
        movie.setTitle(title);
        movie.setRelease_date(release_date);
        movie.setVote(vote);
        movie.setOverview(overview);
        movie.setRuntime(runtime);
        movie.setColledted(collected);

        return movie;
    }
}
