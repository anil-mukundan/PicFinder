package com.mukundan.www.picfinder.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/*
 GsearchResultClass: "GimageSearch",
 width: "1152",
 height: "864",
 imageId: "ANd9GcQQigy-U6KTXke82n5hma5qvFM2UyVnkGtJme6pkZgl_1GYM--Yb90oqnOJ",
 tbWidth: "150",
 tbHeight: "113",
 unescapedUrl: "http://www.blirk.net/wallpapers/1152x864/fuzzy-monkey-1.jpg",
 url: "http://www.blirk.net/wallpapers/1152x864/fuzzy-monkey-1.jpg",
 visibleUrl: "stackoverflow.com",
 title: "json - rails best way to extract values from response hash - Stack <b>...</b>",
 titleNoFormatting: "json - rails best way to extract values from response hash - Stack ...",
 originalContextUrl: "http://stackoverflow.com/questions/17773949/rails-best-way-to-extract-values-from-response-hash",
 content: "json - rails best way to",
 contentNoFormatting: "json - rails best way to",
 tbUrl: "http://t1.gstatic.com/images?q=tbn:ANd9GcQQigy-U6KTXke82n5hma5qvFM2UyVnkGtJme6pkZgl_1GYM--Yb90oqnOJ"
 */
public class SearchResult implements Serializable {
    public String imageUrl;
    public String title;
    public String thumbnailUrl;

    public SearchResult(JSONObject resultJson) {
        try {
            this.imageUrl = resultJson.getString("url");
            this.title = resultJson.getString("title");
            this.thumbnailUrl = resultJson.getString("tbUrl");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<SearchResult> parse(JSONObject resp) {
        ArrayList<SearchResult> results = new ArrayList<>();
        try {
            JSONArray resultsJson = resp.getJSONObject("responseData").getJSONArray("results");
            int size = resultsJson.length();
            for (int i = 0; i < size; i++) {
                SearchResult result = new SearchResult(resultsJson.getJSONObject(i));
                results.add(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }

}
