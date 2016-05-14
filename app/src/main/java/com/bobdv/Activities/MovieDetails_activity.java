package com.bobdv.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bobdv.Fragments.MovieDetails_Fragment;
import com.bobdv.R;

public class MovieDetails_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Bundle extrasData = getIntent().getExtras();

        if(null == savedInstanceState)
        {
            MovieDetails_Fragment movieDetails_fragment = new MovieDetails_Fragment();
            movieDetails_fragment.setArguments(extrasData);
            getFragmentManager().beginTransaction().add(R.id.llDetailsContainer, movieDetails_fragment).commit();

        }
    }
}
