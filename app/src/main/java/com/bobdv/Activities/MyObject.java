package com.bobdv.Activities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mohamed Magdy on 5/22/2016.
 */
public class MyObject implements Parcelable {
    String MovieId;
    String PosterPath;
    String MovieOrignalTitle;
    String mDate;
    String vote_average;
    String OverView;


    public MyObject(String MovieId, String PosterPath ,String MovieOrignalTitle ,String mDate,String vote_average ,String OverView ) {
        this.MovieId = MovieId;
        this.PosterPath = PosterPath;
        this.MovieOrignalTitle = MovieOrignalTitle;
        this.mDate = mDate;
        this.vote_average = vote_average;
        this.OverView = OverView;


    }

    private MyObject(Parcel in) {
        MovieId = in.readString();
        PosterPath = in.readString();
        MovieOrignalTitle = in.readString();
        mDate = in.readString();
        vote_average = in.readString();

        OverView = in.readString();
    }

    public int describeContents() {
        return 0;
    }



    public void writeToParcel(Parcel out, int flags) {
        out.writeString(MovieId);
        out.writeString(PosterPath);
        out.writeString(MovieOrignalTitle);
        out.writeString(mDate);
        out.writeString(vote_average);

        out.writeString(OverView);
    }

    public static final Parcelable.Creator<MyObject> CREATOR = new Parcelable.Creator<MyObject>() {
        public MyObject createFromParcel(Parcel in) {
            return new MyObject(in);
        }

        public MyObject[] newArray(int size) {
            return new MyObject[size];
        }
    };
}