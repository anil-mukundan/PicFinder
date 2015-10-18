package com.mukundan.www.picfinder.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.mukundan.www.picfinder.R;
import com.mukundan.www.picfinder.model.SearchResult;
import com.squareup.picasso.Picasso;

public class ImageDisplayActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().hide();

        SearchResult result = (SearchResult) getIntent().getSerializableExtra("result");
        ImageView ivImageResult = (ImageView) findViewById(R.id.ivImageResult);
        Picasso.with(this).load(result.imageUrl).into(ivImageResult);
        TextView ivTitle = (TextView) findViewById(R.id.tvTitle);
        ivTitle.setText(Html.fromHtml(result.title));
    }

}
