/*
 * Copyright (C) 2017 VJauckus
 *
 */
package com.veronika.android.popmovies.adapters;



import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.veronika.android.popmovies.R;
import com.veronika.android.popmovies.data.MoviesContract;
import com.veronika.android.popmovies.utilities.NetworkUtils;
/**
 *This class helps to expose a list of favourites to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.TaskViewHolder> {

  //  private static final String LOG_TAG = CustomCursorAdapter.class.getSimpleName();
    private Context mContext;
    private Cursor mCursor;

    /**
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final CustomCursorOnClickHandler mClickHandler;

    public CustomCursorAdapter(Context context, CustomCursorOnClickHandler clickHandler){

       // Log.d(LOG_TAG, "I am in CustomCursorAdapter");
        mContext = context;
        mClickHandler = clickHandler;



    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {

        final int adapterPosition = position;
        try{
            mCursor.moveToPosition(position);

          //  Log.d(LOG_TAG, "I am in bind View");
            int imageIndex = mCursor.getColumnIndex(MoviesContract.MoviesEntry.MOVIE_POSTER_PATH);

            String imagePath = mCursor.getString(imageIndex);

            //Build picasso url
            String picassoImageUrl = NetworkUtils.buildUrlPicasso(imagePath);
           // Log.i(LOG_TAG, "Image path extracted: " + imagePath);
            //Set image
            Picasso.with(mContext).load(picassoImageUrl)
                    .placeholder(R.drawable.user_placeholder)
                    .error(R.drawable.user_placeholder_error)
                    .into(holder.imageViewMovie);


            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                //    Log.d(LOG_TAG, "I am in TaskViewHolder, the mCursor: "+ mCursor);
                    mClickHandler.onMovieClick(adapterPosition, mCursor);
                }
            });
        }
        catch (Exception ex){

            ex.printStackTrace();

        }


    }



    //inner class for creating view holder
    /**
     * Cache of the children views for a movie list item.
     */
    class TaskViewHolder extends RecyclerView.ViewHolder{
        final  ImageView imageViewMovie;


        public TaskViewHolder(final View view){
            super(view);
            imageViewMovie = view.findViewById(R.id.movie_poster);

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
     * @return A new TaskViewHolder that holds the View for each list item
     */
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new TaskViewHolder(view);
    }

    public int getItemCount() {
        if (mCursor == null){
            return 0;
        }
        return mCursor.getCount();
    }

    /**
     * When data is changing and a re-query occurs, this function replaces the ols cursor with a newly updated Cursor (newCursor)
     * @param newCursor
     * @return
     */

    public Cursor swapCursor(Cursor newCursor) {

        //if the cursor is still the same as previous, nothing has to be done
        if (mCursor == newCursor){
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor = newCursor; // new Cursor value assigned
        //check if cursor is valid
        if (newCursor != null){
            this.notifyDataSetChanged();
        }

        return temp;

    }
    /**
     * That interface receives onClick AndroidMovie object
     */
    public interface CustomCursorOnClickHandler{

        void onMovieClick(int adapterPosition, Cursor mCursor);
    }

}
