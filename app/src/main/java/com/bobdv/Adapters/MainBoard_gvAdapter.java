package com.bobdv.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bobdv.MovieDataListner;
import com.bobdv.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Bob_91 on 3/25/2016.
 */
public class MainBoard_gvAdapter extends BaseAdapter {
    private Context mContext;


    ArrayList<String> imoviesArrPoster_path;


    MovieDataListner iMovieDataListner;
    // Constructor
    public MainBoard_gvAdapter(Context c,    ArrayList<String> moviesArrPoster_path) {
        mContext = c;

        imoviesArrPoster_path =  moviesArrPoster_path;

        }


    public int getCount() {
        return imoviesArrPoster_path.size();

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        convertView = mInflater.inflate(R.layout.main_grid_adapter_item, null);
        ImageView MainBoardPoster = (ImageView) convertView.findViewById(R.id.MainBoardPoster);




        Picasso.with(mContext).load(imoviesArrPoster_path.get(position)).placeholder(R.drawable.logo).into(MainBoardPoster);
//



        return convertView;
    }





}


