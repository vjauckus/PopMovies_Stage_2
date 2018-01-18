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
import com.veronika.android.popmovies.models.Trailer;

import java.util.ArrayList;

/**
 * The Adapter for trailer is responsible for linking our trailer data with the Views that
 * will end up displaying our trailer data.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder>{

  //  private final static String TAG = TrailerAdapter.class.getSimpleName();

    /**
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView, when a single item is clicked
     */
    private final TrailerAdapterOnClickHandler mClickHandler;
    private ArrayList<Trailer> mTrailerList;
    private Context mContext;

    /**
     * This is our own custom constructor
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     * @param clickHandler The on-click handler makes it easy for an Activity to interface with
     * our RecyclerView and works with Adapter. this single handler is called.
     */

    public TrailerAdapter(Context context, ArrayList<Trailer> trailerList, TrailerAdapterOnClickHandler clickHandler){

        mTrailerList = trailerList;
        mContext = context;

        mClickHandler = clickHandler;
    }
    /**
     * Cache of the children views for a movie list item.
     */
    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder{

       // @BindView(R.id.trailer_item)
        private TextView mTrailerTitle;
        //video key important for play video
        private Context mContext;

        /**
         * Constructor
         * @param view the current View in the list of trailers
         */
        public TrailerAdapterViewHolder(View view){
            super(view);
            mTrailerTitle = view.findViewById(R.id.trailer_item);
            mContext = view.getContext();

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickHandler.onClick(mTrailerList.get(getAdapterPosition()));
                }
            });

        }

    }
    /**
     * Called by RecyclerView to display the movie data at the specified position
     * @param holder The ViewHolder that should be updated to represent the content of the item at the given position
     * @param position The position of item within adapter's data list
     */
    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
       // Log.v(TAG, "I am in Binding of view");

      //  Trailer currentTrailer = mTrailerList.get(position);
        //final int adapterPosition = holder.getAdapterPosition();
        int trailerNumber = position +1;
        String currentTrailerTitle = mContext.getResources().getString(R.string.trailer_item_label)+" "+trailerNumber;
        holder.mTrailerTitle.setText(currentTrailerTitle);


    }

    /**This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent The ViewGroup that these ViewHolders are contained within.
     * @param viewType If our RecyclerView has more than one type of items(which ours does not), we can use this integer to provide a different layout
     * @return A new MovieAdapterViewHolder that holds the View for each list item
     */
    @Override
    public TrailerAdapter.TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public int getItemCount() {

        if (mTrailerList == null) return 0;

        return mTrailerList.size();
    }

    /**
     * That interface receives onClick AndroidMovie object
     */
    public interface TrailerAdapterOnClickHandler{

        void onClick(Trailer currentTrailer);
    }
}
