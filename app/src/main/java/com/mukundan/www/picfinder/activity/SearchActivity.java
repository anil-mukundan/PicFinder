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
    private static final int FILTER_REQUEST_CODE = 0;
    private static final String ALL = "all";
    private GridView gvResults;
    private List<SearchResult> images;
    private SearchResultAdapter aSearchResults;
    private String currentQuery;
    private String imageSize = ALL;
    private String imageType = ALL;
    private String colorFilter = ALL;
    private String siteFilter = "";

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
                showAdvancedFilters();
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
        if (startPageIndex > 64) {
            Toast.makeText(this, "Reached Max Images", Toast.LENGTH_LONG).show();
            return;
        }
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("v", "1.0");
        params.put("rsz", "8");
        params.put("q", currentQuery);
        if (!imageSize.equals(ALL)) {
            params.put("imgsz", imageSize);
        }
        if (!imageType.equals(ALL)) {
            params.put("imgtype", imageType);
        }
        if (!colorFilter.equals(ALL)) {
            params.put("imgcolor", colorFilter);
        }
        if (!siteFilter.isEmpty()) {
            params.put("as_sitesearch", siteFilter);
        }
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

    private void showAdvancedFilters() {
        Intent i = new Intent(SearchActivity.this, SearchFilterActivity.class);
        i.putExtra("image_type", this.imageType);
        i.putExtra("image_size", this.imageSize);
        i.putExtra("color_filter", this.colorFilter);
        i.putExtra("site_filter", this.siteFilter);
        startActivityForResult(i, FILTER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == FILTER_REQUEST_CODE) {
            this.imageType = data.getExtras().getString("image_type");
            this.imageSize = data.getExtras().getString("image_size");
            this.colorFilter = data.getExtras().getString("color_filter");
            this.siteFilter = data.getExtras().getString("site_filter");

            this.searchForImages();
        }
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
