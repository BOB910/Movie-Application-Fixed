package com.bobdv.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bobdv.R;

import java.util.ArrayList;

/**
 * Created by Bob_91 on 5/1/2016.
 */
public class ReviewsListAdapter extends ArrayAdapter<String> {


    Activity context;
    final ArrayList<String> iarr_autherName;
    final ArrayList<String> iarr_autherReview;
    final ArrayList<String> iarr_ReviewURL;
    TextView txt_auther, autherReview, txt_Details;


    public ReviewsListAdapter(Activity Context, ArrayList<String> arr_autherName, ArrayList<String> arr_autherReview, ArrayList<String> arr_ReviewURL ) {
        super(Context, R.layout.reviews_item_list , arr_autherName);
        this.context = Context;
        this.iarr_autherName = arr_autherName;
        this.iarr_autherReview = arr_autherReview;
        this.iarr_ReviewURL = arr_ReviewURL;

    }

    @Override
    public int getCount() {
        return iarr_autherName.size();
    }

    @SuppressLint({"ViewHolder", "NewApi"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.reviews_item_list, null, true);

        txt_auther = (TextView) rowView.findViewById(R.id.autherName);
        autherReview = (TextView) rowView.findViewById(R.id.autherReview);
        txt_Details = (TextView) rowView.findViewById(R.id.txt_Details);

        Log.d("Movie" , iarr_autherName.toString());
        Log.d("Movie" , iarr_autherName.size()+"");

        txt_auther.setText(iarr_autherName.get(position));
        autherReview.setText(iarr_autherReview.get(position));

        txt_Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(iarr_ReviewURL.get(position)));
                context.startActivity(browserIntent);
            }
        });

        return rowView;
    }


///////////////////
}

