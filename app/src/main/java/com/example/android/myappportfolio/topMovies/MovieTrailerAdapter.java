package com.example.android.myappportfolio.topMovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myappportfolio.R;

/**
 * Created by lk235 on 2017/5/1.
 */

public class MovieTrailerAdapter extends ArrayAdapter<String> {

    private int mResourceId;

    public MovieTrailerAdapter(Context context, int textViewResourceId, String[] trailers){
        super(context, textViewResourceId, trailers);
        mResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        String trailerStr = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(mResourceId, null);
        ImageView trailerImageView = (ImageView) view.findViewById(R.id.movie_trailer_image_view) ;
        TextView trailerTextView = (TextView) view.findViewById(R.id.movie_trailer_text_view);
        trailerImageView.setImageResource(R.drawable.play);
        trailerTextView.setText(trailerStr);
        return view;
    }
}
