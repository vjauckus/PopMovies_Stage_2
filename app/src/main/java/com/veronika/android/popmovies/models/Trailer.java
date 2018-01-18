package com.veronika.android.popmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by veronika on 17.11.17.
 */

public class Trailer implements Parcelable{

    private String mVideoKey;
   // private String mName;

    //Constructor

    public Trailer(){

    }

    private Trailer(Parcel parcel){
        mVideoKey = parcel.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return mVideoKey;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mVideoKey);
    }
    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel parcel) {
            return new Trailer(parcel);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }

    };

    public void setVideoKey(String videoKey){

        mVideoKey = videoKey;
    }
    public String getVideoKey(){

        return mVideoKey;
    }
}
