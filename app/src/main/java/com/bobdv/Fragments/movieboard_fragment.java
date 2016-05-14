package com.bobdv.Fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.bobdv.Adapters.MainBoard_gvAdapter;
import com.bobdv.Database.AppDataBase;
import com.bobdv.MovieDataListner;
import com.bobdv.R;

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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A simple {@link Fragment} subclass.
 */
public class movieboard_fragment extends Fragment {
    private Toolbar mToolbar;
    GridView mainBoardGridView;
    MainBoard_gvAdapter mainBoard_gvAdapter;
    private ProgressDialog pDialog;

    MovieDataListner iMovieDataListner;

    ArrayList<String> arrPoster_path, arrIsForAdults, arr_overview, arr_genre_ids,

    arrRelease_date, arrMovieId, arrOriginal_title, arrOriginal_language,
            arrTitle, arrBackdrop_path, arrPopularity, arrVote_count, arrHasVideo,
            arrVote_average;

    private String API_KEY = "f9a4969da1764353e869bdd6bad48dbb";
    private String imageLink = "http://image.tmdb.org/t/p/w185";
    private String popularLink = "https://api.themoviedb.org/3/movie/popular?api_key=";
    private String topRatedLink = "https://api.themoviedb.org/3/movie/top_rated?api_key=";

    AppDataBase MyDb;

    HashMap<String, ArrayList<String>> hashMap_favMovies = new HashMap<String, ArrayList<String>>();

    public movieboard_fragment() {
        // Required empty public constructor
    }

    public static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_movieboard, container, false);
            setHasOptionsMenu(true);


            init();
            MyDb = new AppDataBase(getActivity());

            new getAPIDataAsync().execute(popularLink + API_KEY);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Most popular");
        } catch (InflateException e) {
            Toast.makeText(getActivity(), "something went wrong !!", Toast.LENGTH_SHORT).show();
        }
        return view;
    }


    private void init() {

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);


        mainBoardGridView = (GridView) view.findViewById(R.id.MainBoardGridView);
        arrRelease_date = new ArrayList<>();
        arrPoster_path = new ArrayList<>();
        arrIsForAdults = new ArrayList<>();
        arr_overview = new ArrayList<>();
        arr_genre_ids = new ArrayList<>();
        arrMovieId = new ArrayList<>();
        arrOriginal_title = new ArrayList<>();
        arrOriginal_language = new ArrayList<>();
        arrTitle = new ArrayList<>();
        arrBackdrop_path = new ArrayList<>();
        arrPopularity = new ArrayList<>();
        arrVote_count = new ArrayList<>();
        arrHasVideo = new ArrayList<>();
        arrVote_average = new ArrayList<>();

        mainBoardGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iMovieDataListner.setSelected_MovieID(arrMovieId.get(position));
                iMovieDataListner.setSelected_PosterPath(arrPoster_path.get(position));
                iMovieDataListner.setSelected_MovieOrignalTitle(arrOriginal_title.get(position));
                iMovieDataListner.setSelected_mDate(arrRelease_date.get(position));
                iMovieDataListner.setSelected_vote_average(arrVote_average.get(position));
                iMovieDataListner.setSelected_OverView(arr_overview.get(position));

                Log.d("Movie", "arr " + arrVote_average.get(position));
                Log.d("Movie", "arr_overview " + arr_overview.get(position));

            }
        });
    }

    public void setMovieDataListner(MovieDataListner movieDataListner) {
        iMovieDataListner = movieDataListner;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();
        switch (id) {
            case R.id.most_popular:

                new getAPIDataAsync().execute(popularLink + API_KEY);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Most popular");

                break;

            case R.id.top_rated:

                new getAPIDataAsync().execute(topRatedLink + API_KEY);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Top rated");
                break;

            case R.id.my_Favorite:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Favorite");


                                    arrMovieId.clear();
                    arrPoster_path.clear();
                    arrOriginal_title.clear();
                    arrRelease_date.clear();
                    arrVote_average.clear();
                    arr_overview.clear();
                MyDb.openDataBase();
                hashMap_favMovies = MyDb.GetMovieDetails();
                MyDb.closeDataBase();
                if (hashMap_favMovies.size() > 0) {

                    arrMovieId = hashMap_favMovies.get("arr_MovieID");
                    arrPoster_path = hashMap_favMovies.get("arr_PosterPath");
                    arrOriginal_title = hashMap_favMovies.get("arr_MovieOrignalTitle");
                    arrRelease_date = hashMap_favMovies.get("arr_mDate");
                    arrVote_average = hashMap_favMovies.get("arr_vote_average");
                    arr_overview = hashMap_favMovies.get("arr_MovieOverView");
                    Log.d("MYMOVIE", "Ids  : " + arrMovieId.toString());
                    mainBoard_gvAdapter = new MainBoard_gvAdapter(getActivity().getApplicationContext(), arrPoster_path);
                    mainBoard_gvAdapter.notifyDataSetChanged();
                    mainBoardGridView.setAdapter(mainBoard_gvAdapter);


                } else {
                    Toast.makeText(getActivity(), "No favorite movies", Toast.LENGTH_LONG).show();
                }
        }


        return super.onOptionsItemSelected(item);
    }


    private class getAPIDataAsync extends AsyncTask<String, Void, Void> {
        String result;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(String... params) {


            result = getApiResponse(params[0], 10000);
            Log.d("MYMOVIE", "JSON RESULT   :  " + result);

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (TextUtils.isEmpty(result)) {

                if (pDialog.isShowing()) ;
                pDialog.dismiss();

                Toast.makeText(getActivity().getApplicationContext(), "Please check internet connection !!", Toast.LENGTH_LONG).show();
            } else {

                ParseMoviesResult(result);

//                mainBoard_gvAdapter = new MainBoard_gvAdapter(getActivity().getApplicationContext(), arrMovieId, arrPoster_path, arrTitle, arrRelease_date, arrVote_average, arr_overview);
                mainBoard_gvAdapter = new MainBoard_gvAdapter(getActivity().getApplicationContext(), arrPoster_path);
                mainBoard_gvAdapter.notifyDataSetChanged();
                mainBoardGridView.setAdapter(mainBoard_gvAdapter);


                if (pDialog.isShowing()) ;
                pDialog.dismiss();

            }
        }
    }


    private void ParseMoviesResult(String JsonString) {

        JSONObject jsonResponse;
        try {


            jsonResponse = new JSONObject(JsonString);
            JSONArray resultsArray = jsonResponse.getJSONArray("results");
            Log.d("MYMOVIE", "resultsArray.length()   :  " + resultsArray.length());
            arrPoster_path.clear();
            arrIsForAdults.clear();
            arr_overview.clear();
            arrMovieId.clear();
            arrOriginal_title.clear();
            arrOriginal_language.clear();
            arrTitle.clear();
            arrBackdrop_path.clear();
            arrPopularity.clear();
            arrVote_count.clear();
            arrHasVideo.clear();
            arrVote_average.clear();

            for (int i = 0; i < resultsArray.length(); i++) {
//
                JSONObject Obj = resultsArray.getJSONObject(i);

//                String poster_path = Obj.getString("poster_path");
//                String adult = Obj.getString("adult");
//                String overview = Obj.getString("overview");
//                String id = Obj.getString("id");
//                String original_title = Obj.getString("original_title");
//                String original_language = Obj.getString("original_language");
//                String title = Obj.getString("title");
//                String backdrop_path = Obj.getString("backdrop_path");
//                String popularity = Obj.getString("popularity");
//                String vote_count = Obj.getString("vote_count");
//                String video = Obj.getString("video");
//                String vote_average = Obj.getString("vote_average");
//                String mDate = Obj.getString("release_date");
                arrPoster_path.add(imageLink + Obj.getString("poster_path"));
                arrIsForAdults.add(Obj.getString("adult"));
                arr_overview.add(Obj.getString("overview"));
                arrMovieId.add(Obj.getString("id"));
                arrOriginal_title.add(Obj.getString("original_title"));
                arrRelease_date.add(Obj.getString("release_date"));
                arrOriginal_language.add(Obj.getString("original_language"));
                arrTitle.add(Obj.getString("title"));
                arrBackdrop_path.add(Obj.getString("backdrop_path"));
                arrPopularity.add(Obj.getString("popularity"));
                arrVote_count.add(Obj.getString("vote_count"));
                arrHasVideo.add(Obj.getString("video"));
                arrVote_average.add(Obj.getString("vote_average"));


            }

            Log.d("MYMOVIE", "arrVote_average    :  " + arrVote_average.toString());
            Log.d("MYMOVIE", "arrPopularity    :  " + arrPopularity.toString());


            Log.d("MYMOVIE", "arrPopularity 2   :  " + arrPopularity.toString());
//            Log.d("MYMOVIE", "overview    :  " + overview);
//            Log.d("MYMOVIE", "id    :  " + id);
//            Log.d("MYMOVIE", "original_title    :  " + original_title);
//            Log.d("MYMOVIE", "original_language    :  " + original_language);
//            Log.d("MYMOVIE", "title    :  " + title);


        } catch (JSONException e) {

            Log.d("MYMOVIE", "JSONException   :  " + e.getMessage());

            Toast.makeText(getActivity().getApplicationContext(), "something went wrong !!", Toast.LENGTH_SHORT).show();

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
