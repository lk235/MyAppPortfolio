package com.example.android.myappportfolio.topMovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
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




    public MovieAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    public static class ViewHolder{
        public final ImageView movieImage;

        public ViewHolder(View view){
          movieImage =(ImageView) view.findViewById(R.id.movie_image_item);
        }

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //int idx_image_url = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE_URL);
        ViewHolder viewHolder = (ViewHolder)view.getTag();
        //ImageView imageView = (ImageView) view.findViewById(R.id.movie_image_item);
        String imageUrl = cursor.getString(MovieListFragment.COL_IMAGE_URL);
        Picasso.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_sync_black_24dp)
                .error(R.drawable.ic_info_black_24dp)
                .into(viewHolder.movieImage);


    }


}





