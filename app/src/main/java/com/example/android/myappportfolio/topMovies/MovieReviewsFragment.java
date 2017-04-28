package com.example.android.myappportfolio.topMovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.myappportfolio.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by lk235 on 2017/4/27.
 */

public class MovieReviewsFragment extends Fragment{

    public MovieReviewsFragment(){

    }

    private ListView mReviewsListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_reviews, container, false);
        mReviewsListView = (ListView) rootView.findViewById(R.id.reviews_list_view);

        String[] movieReviewAuthor = getActivity().getIntent().getStringArrayExtra(MoiveDetailFragment.MOVIE_REVIEW_AUTHOR);
        String[] movieReviewContent = getActivity().getIntent().getStringArrayExtra(MoiveDetailFragment.MOVIE_REVIEW_CONTENT);
        ArrayList reviewsList = new ArrayList();
        String[] reviewItemStr;
        for(int i=0; i < movieReviewAuthor.length; i++){
            reviewItemStr = new String[]{movieReviewAuthor[i], movieReviewContent[i]};
            reviewsList.add(reviewItemStr);
        }


        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getActivity(),
                R.layout.movie_reviews_item,
                reviewsList
                );



        mReviewsListView.setAdapter(reviewsAdapter);

        return rootView;


    }

     public class ReviewsAdapter extends ArrayAdapter<String[]>{
         private int mResourceId;


         public ReviewsAdapter(Context context, int resourceId, ArrayList reviewsList){
             super(context, resourceId, reviewsList);
             mResourceId = resourceId;

         }

         @Override
         public View getView(int position, View convertView, ViewGroup parent){
             View view = LayoutInflater.from(getContext()).inflate(mResourceId, null);
             TextView authorTextView = (TextView) view.findViewById(R.id.movie_review_author_text_view);
             TextView contentTextView = (TextView) view.findViewById(R.id.movie_review_content_text_view);
             String[] reviewStr = getItem(position);
             authorTextView.setText(reviewStr[0]);
             contentTextView.setText(reviewStr[1]);
             return view;
         }



    }
}
