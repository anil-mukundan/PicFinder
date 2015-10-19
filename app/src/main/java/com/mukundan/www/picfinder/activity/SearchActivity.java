package com.mukundan.www.picfinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mukundan.www.picfinder.R;
import com.mukundan.www.picfinder.adapter.SearchResultAdapter;
import com.mukundan.www.picfinder.model.SearchResult;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends ActionBarActivity {

    private static final String IMAGE_SEARCH_URL = "https://ajax.googleapis.com/ajax/services/search/images";
    private GridView gvResults;
    private List<SearchResult> images;
    private SearchResultAdapter aSearchResults;
    private String currentQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_pic_finder_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setupViews();
        images = new ArrayList<>();
        aSearchResults = new SearchResultAdapter(this, images);
        gvResults.setAdapter(aSearchResults);
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                Log.d("Page", Integer.toString(page));
                loadImages(page);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentQuery = query;
                searchForImages();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentQuery = newText;
                searchForImages();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_filter:
                Toast.makeText(this, "Filter", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void searchForImages() {
        loadImages(0);
    }

    public void loadImages(int startPageIndex) {
        if (startPageIndex == 0) {
            aSearchResults.clear();
        }
        if (startPageIndex > 63) {
            Toast.makeText(this, "Reached Max Images", Toast.LENGTH_LONG).show();
            return;
        }
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("v", "1.0");
        params.put("rsz", "8");
        params.put("q", currentQuery);
        if (startPageIndex > 0) {
            params.put("start", Integer.toString(startPageIndex));
        }
        httpClient.get(this.IMAGE_SEARCH_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                aSearchResults.addAll(SearchResult.parse(response));
            }
        });
    }

    public void loadMoreImages() {
        AsyncHttpClient httpClient = new AsyncHttpClient();
    }

    private void setupViews() {
        gvResults = (GridView) findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Launch Image Display Activity
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                SearchResult result = images.get(position);
                i.putExtra("result", result);
                startActivity(i);
            }
        });

    }
}
