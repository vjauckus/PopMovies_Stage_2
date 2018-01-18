package com.veronika.android.popmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by veronika on 17.11.17.
 */

public class Review implements Parcelable {

    private String mAuthor;

    private  String mContent;

    //Constructor
    public Review(){

    }
    private Review(Parcel in){
        mAuthor = in.readString();
        mContent = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Author: "+mAuthor+ "review: "+mContent;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(mAuthor);
        dest.writeString(mContent);
    }
    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }

    };
    public void setAuthor (String author){

        mAuthor = author;
    }
    public void setContent (String content){

        mContent = content;
    }
    public String getAuthor(){

        return mAuthor;
    }
    public String getContent(){

        return mContent;
    }
}
