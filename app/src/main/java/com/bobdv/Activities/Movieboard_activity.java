package com.bobdv.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.bobdv.Fragments.MovieDetails_Fragment;
import com.bobdv.Fragments.movieboard_fragment;
import com.bobdv.MovieDataListner;
import com.bobdv.R;


public class Movieboard_activity extends AppCompatActivity implements MovieDataListner {

    boolean mPaneTwo;
    String iMovieID, iPosterPath, iMovieTitle, iVoteAvarage, iOverView, iMovieDate;
    Intent DetailsActivity;
    movieboard_fragment movieboard_fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movieboard_activity);
// check if found .. we are in tablet screen
        FrameLayout detailsFrame = (FrameLayout) findViewById(R.id.pane_two);
        if (null == detailsFrame) {
            mPaneTwo = false;
        } else {
            mPaneTwo = true;
        }

        if (null == savedInstanceState) {
              movieboard_fragment = new movieboard_fragment();
            movieboard_fragment.setMovieDataListner(this);
            getFragmentManager().beginTransaction().add(R.id.pane_one, movieboard_fragment).commit();

        }
        else
        {
            movieboard_fragment = new movieboard_fragment();
            movieboard_fragment.setMovieDataListner(this);
            getFragmentManager().beginTransaction().add(R.id.pane_one, movieboard_fragment).commit();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState ) {
        outState.putString("a","a");

        super.onSaveInstanceState(outState );

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);




    }

    @Override
    public void setSelected_MovieID(String MovieID) {
        this.iMovieID = MovieID;

    }

    @Override
    public void setSelected_PosterPath(String PosterPath) {
        this.iPosterPath = PosterPath;


    }

    @Override
    public void setSelected_MovieOrignalTitle(String MovieOrignalTitle) {

        this.iMovieTitle = MovieOrignalTitle;


    }




    @Override
    public void setSelected_vote_average(String vote_average) {
        this.iVoteAvarage = vote_average;
    }

    @Override
    public void setSelected_mDate(String mDate) {
        this.iMovieDate = mDate;


    }




    @Override
    public void setSelected_OverView(String MovieOverView) {
        this.iOverView = MovieOverView;
        if(mPaneTwo)
        {
            MovieDetails_Fragment details_fragment = new MovieDetails_Fragment();
            Bundle extras = new Bundle();
            extras.putString("iMovieID", iMovieID);
            extras.putString("iPosterPath", iPosterPath);
            extras.putString("vote_average", iVoteAvarage);
            extras.putString("iOverView", iOverView);
            extras.putString("iMovieDate", iMovieDate);
            extras.putString("iMovieTitle", iMovieTitle);
            details_fragment.setArguments(extras);
            getFragmentManager().beginTransaction().replace(R.id.pane_two , details_fragment).commit();
        }
        else
        {

            DetailsActivity = new Intent(this , MovieDetails_activity.class);
            DetailsActivity.putExtra("iMovieID",iMovieID);
            DetailsActivity.putExtra("iPosterPath",iPosterPath);
            DetailsActivity.putExtra("vote_average",iVoteAvarage);
            DetailsActivity.putExtra("iOverView",iOverView);
            DetailsActivity.putExtra("iMovieDate", iMovieDate);
            DetailsActivity.putExtra("iMovieTitle", iMovieTitle);

            Log.d("Movie", "iVoteAvarage  :  " + iVoteAvarage);
            startActivity(DetailsActivity);

        }

    }

}
