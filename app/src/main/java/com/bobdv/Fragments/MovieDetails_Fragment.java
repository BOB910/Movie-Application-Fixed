package com.bobdv.Fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bobdv.Adapters.ReviewsListAdapter;
import com.bobdv.Database.AppDataBase;
import com.bobdv.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetails_Fragment extends Fragment {


    public MovieDetails_Fragment() {
        // Required empty public constructor
    }

    public static View view;

    ListView linear_Trailers, linear_Review;
    TextView txt_MovieName, txt_releaseDate, txt_MovieRate, txt_review ;
    ImageView img_posterDetails;
    private Toolbar mToolbar;
    private ProgressDialog pDialog;
    String MovieID;
    Button btn_Trailers, btn_Reviews;

    ArrayList<String> reviewsAuthor = new ArrayList<>();

    ArrayList<String> reviewsContent = new ArrayList<>();

    ArrayList<String> reviewsUrl = new ArrayList<>();

    Button btn_fav;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_movie_details_, container, false);


            init();
            myDB = new AppDataBase(getActivity());
            setHasOptionsMenu(true);

            MovieID = getArguments().getString("iMovieID");
            txt_MovieName.setText(getArguments().getString("iMovieTitle"));
            txt_releaseDate.setText(getArguments().getString("iMovieDate"));
            txt_MovieRate.setText(getArguments().getString("vote_average") + "/10");
            txt_review.setText(getArguments().getString("iOverView"));
            Picasso.with(getActivity()).load(getArguments().getString("iPosterPath")).placeholder(R.drawable.logo).into(img_posterDetails);

            Check_IsFavourite(MovieID);
            if (fav_Indecator) {
                btn_fav.setText("Remove from favorite");
            } else {
                btn_fav.setText("MARK AS FAVORITE");
            }

            btn_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fav_Indecator) {
                        myDB.openDataBase();
                        myDB.deleteFavorite(MovieID);
                        myDB.closeDataBase();
                        Toast.makeText(getActivity() , "Removed from favorite !! " , Toast.LENGTH_LONG).show();
                        btn_fav.setText("MARK AS FAVORITE");
                        fav_Indecator=false;

                    } else {
                        myDB.openDataBase();
                        myDB.insertMovieDetails(MovieID, getArguments().getString("iPosterPath"), getArguments().getString("iMovieTitle"), getArguments().getString("iMovieDate")
                                , getArguments().getString("vote_average") + "/10", getArguments().getString("iOverView"));
                        myDB.closeDataBase();
                        Toast.makeText(getActivity() , " favorite added  !! " , Toast.LENGTH_LONG).show();
                        btn_fav.setText("Remove from favorite");
                        fav_Indecator=true;
                    }
                }
            });
            new getMovieDetails().execute();

            linear_Trailers.setVisibility(View.VISIBLE);
            linear_Review.setVisibility(View.GONE);
            btn_Trailers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (linear_Trailers.getVisibility() == View.VISIBLE && linear_Review.getVisibility() == View.GONE) {
                    } else {
                        linear_Trailers.setVisibility(View.VISIBLE);

                        linear_Review.setVisibility(View.GONE);
                        btn_Trailers.setBackgroundColor(Color.parseColor("#169190"));
                        btn_Reviews.setBackgroundColor(Color.parseColor("#ffffff"));

                    }
                }
            });

            btn_Reviews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (linear_Trailers.getVisibility() == View.VISIBLE && linear_Review.getVisibility() == View.GONE) {
                        linear_Trailers.setVisibility(View.GONE);
                        linear_Review.setVisibility(View.VISIBLE);
                        btn_Trailers.setBackgroundColor(Color.parseColor("#ffffff"));
                        btn_Reviews.setBackgroundColor(Color.parseColor("#169190"));

                    } else {

                    }
                }
            });


        } catch (InflateException e) {
            Toast.makeText(getActivity(), "something went wrong !!", Toast.LENGTH_SHORT).show();
        }
        return view;

    }


    private void init() {
        btn_fav = (Button) view.findViewById(R.id.btn_fav);
        txt_MovieName = (TextView) view.findViewById(R.id.txt_MovieName);
        txt_releaseDate = (TextView) view.findViewById(R.id.txt_releaseDate);
        txt_MovieRate = (TextView) view.findViewById(R.id.txt_MovieRate);
        txt_review = (TextView) view.findViewById(R.id.txt_review);
        img_posterDetails = (ImageView) view.findViewById(R.id.img_posterDetails);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Details");
        linear_Trailers = (ListView) view.findViewById(R.id.linear_Trailers);
        linear_Review = (ListView) view.findViewById(R.id.linear_Review);

        btn_Trailers = (Button) view.findViewById(R.id.btn_Trailers);
        btn_Reviews = (Button) view.findViewById(R.id.btn_Reviews);


    }

    AppDataBase myDB;
    boolean fav_Indecator = false;

    private void Check_IsFavourite(String ID) {
        myDB.openDataBase();
        boolean check = myDB.CheckIfFavorite(ID);
        myDB.closeDataBase();
        if (check) {
            fav_Indecator = true;
        } else {
            fav_Indecator = false;
        }


    }

    public class getMovieDetails extends AsyncTask<Void, Void, Void> {
        String reviewsResult, VideosResult;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity()
            );
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            VideosResult = getApiResponse("http://api.themoviedb.org/3/movie/" + MovieID + "/videos?api_key=f9a4969da1764353e869bdd6bad48dbb", 15000);
            reviewsResult = getApiResponse("http://api.themoviedb.org/3/movie/" + MovieID + "/reviews?api_key=f9a4969da1764353e869bdd6bad48dbb", 15000);
            Log.d("Movie", "http://api.themoviedb.org/3/movie/" + MovieID + "/reviews?api_key=f9a4969da1764353e869bdd6bad48dbb");

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (TextUtils.isEmpty(VideosResult)) {

            } else {
                parseVideosResult(VideosResult);
            }
            if (TextUtils.isEmpty(reviewsResult)) {

                Log.d("MOVIE", reviewsResult + "");

            } else {
                Log.d("MOVIE", reviewsResult + "");
                parseReviewsResult(reviewsResult);


            }

            if (pDialog.isShowing()) ;
            pDialog.dismiss();
        }
    }


    private void parseReviewsResult(String videosResult) {
        JSONObject jsonResponse;
        try {
            jsonResponse = new JSONObject(videosResult);
            JSONArray resultsArray = jsonResponse.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject Obj = resultsArray.getJSONObject(i);


                reviewsAuthor.add(Obj.getString("author"));
                reviewsContent.add(Obj.getString("content"));
                reviewsUrl.add(Obj.getString("url"));

            }
            ReviewsListAdapter reviewsListAdapter = new ReviewsListAdapter(getActivity(), reviewsAuthor, reviewsContent, reviewsUrl);
            linear_Review.setAdapter(reviewsListAdapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    ArrayList<String> VideosNames = new ArrayList<>();

    ArrayList<String> VideosLink = new ArrayList<>();


    private void parseVideosResult(String videosResult) {
        JSONObject jsonResponse;
        try {
            jsonResponse = new JSONObject(videosResult);
            JSONArray resultsArray = jsonResponse.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject Obj = resultsArray.getJSONObject(i);

                String name = Obj.getString("name");
                String Link = Obj.getString("key");
                VideosNames.add(name);
                VideosLink.add("https://www.youtube.com/watch?v=" + Link);
            }


            ArrayAdapter listAdapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, VideosNames);
            linear_Trailers.setAdapter(listAdapter);
            linear_Trailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(VideosLink.get(position))));

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private String getApiResponse(String str_url, int TimeOUT) {
        HttpURLConnection httpURLConnection = null;


        try {


            URL url = new URL(str_url);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-length", "0");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setAllowUserInteraction(false);
            httpURLConnection.setConnectTimeout(TimeOUT);
            httpURLConnection.setReadTimeout(TimeOUT);
            httpURLConnection.connect();
            int status = httpURLConnection.getResponseCode();


            switch (status) {
                case 200:
                case 201:

                    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();

            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (httpURLConnection != null) {
                try {
                    httpURLConnection.disconnect();
                    // Toast.makeText(getActivity() , "Something went wrong !!" , Toast.LENGTH_LONG).show();
                } catch (Exception err) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, err);
                }
            }
        }
        return null;
    }
}

