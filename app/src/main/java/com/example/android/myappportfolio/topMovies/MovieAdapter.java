package com.example.android.myappportfolio.topMovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.android.myappportfolio.R;
import com.example.android.myappportfolio.topMovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lk235 on 2017/4/16.
 */

public class MovieAdapter extends CursorAdapter {

        private int resourceID;
        private List<Movie> mMovies;

//        public MovieAdapter(Context context, int imageViewResourceID, List<Movie> movies){
//            super(context, imageViewResourceID, movies);
//            resourceID = imageViewResourceID;
//
//        }

    public MovieAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){

        //int idx_image_url = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE_URL);
                    ImageView imageView = (ImageView) view.findViewById(R.id.movie_image_item);
        String imageUrl = cursor.getString(MovieListFragment.COL_IMAGE_URL);
            Picasso.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_sync_black_24dp)
                    .error(R.drawable.ic_info_black_24dp)
                    .into(imageView);


    }

//        @Override
//        public View getView(int position, View converView, ViewGroup parent){
//            Movie movie = getItem(position);
//            View view;
//            String imageUrl = movie.getImageUrl();
//            if(converView == null){
//                view = LayoutInflater.from(getContext()).inflate(resourceID, null);
//            }else{
//                view = converView;
//            }
//
//            ImageView imageView = (ImageView) view.findViewById(R.id.movie_image_item);
//            Picasso.with(getContext())
//                    .load(imageUrl)
//                    .placeholder(R.drawable.ic_sync_black_24dp)
//                    .error(R.drawable.ic_info_black_24dp)
//                    .into(imageView);
//            return view;
//        }

        public void addMovie(List<Movie> movies){
            mMovies = movies;
        }
    }

