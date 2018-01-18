/*
 * Copyright (C) 2017 VJauckus
 *
 */
package com.veronika.android.popmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.veronika.android.popmovies.R;
import com.veronika.android.popmovies.models.Review;

import java.util.ArrayList;

/**
 * The Adapter for reviews is responsible for linking our reviews data with the Views that
 * will end up displaying our reviews.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

   // private static final String TAG = ReviewAdapter.class.getSimpleName();
    /**
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView, when a single item is clicked
     */
  //  private final ReviewAdapterOnClickHandler mClickHandler;
    private final ArrayList<Review> mReviews;

    /**Our own Constructor
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param reviewArrayList our reviews ArrayList
     * our RecyclerView and works with Adapter. this single handler is called.
     */

    public ReviewAdapter(ArrayList<Review> reviewArrayList){

        //mClickHandler = clickHandler;
        mReviews = reviewArrayList;

    }

    /**
     * Cache of the children views for a movie list item
     */
    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder{
        private TextView mReviewAuthor;
        private TextView mReviewContent;

        /**
         * Constructor of ReviewAdapterViewHolder
         * @param view the current View in the recycler view list
         */
        public ReviewAdapterViewHolder(View view){

            super(view);
            mReviewAuthor = view.findViewById(R.id.review_author);
            mReviewContent = view.findViewById(R.id.review_content);
        }

    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     * @param parent The ViewGroup that these ViewHolders are contained within.
     * @param viewType If our RecyclerView has more than one type of item (which ours doesn't) we
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ReviewAdapterViewHolder that holds the View for each list item
     */
    @Override
    public ReviewAdapter.ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewAdapterViewHolder holder, int position) {
     //   Log.v(TAG, "I am in Binding of view");
        Review currentReview = mReviews.get(position);
        holder.mReviewAuthor.setText(currentReview.getAuthor());
        holder.mReviewContent.setText(currentReview.getContent());

    }

    @Override
    public int getItemCount() {
        if (mReviews == null){
            return 0;
        }
        return mReviews.size();
    }


}
