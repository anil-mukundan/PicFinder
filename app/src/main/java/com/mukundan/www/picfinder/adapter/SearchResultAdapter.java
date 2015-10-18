package com.mukundan.www.picfinder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.mukundan.www.picfinder.R;
import com.mukundan.www.picfinder.model.SearchResult;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by amukund on 10/17/15.
 */
public class SearchResultAdapter extends ArrayAdapter<SearchResult> {

    public SearchResultAdapter(Context context, List<SearchResult> results) {
        super(context, R.layout.item_search_result, results);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchResult result = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_search_result, parent, false);
        }

        ImageView ivThumbnail = (ImageView) convertView.findViewById(R.id.ivThumbnail);
        ivThumbnail.setImageResource(0);
        Picasso.with(getContext()).load(result.thumbnailUrl).into(ivThumbnail);

        return convertView;
    }
}
