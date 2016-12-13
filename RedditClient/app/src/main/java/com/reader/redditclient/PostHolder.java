package com.reader.redditclient;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Surabhi Agrawal on 12/13/2016.
 */
public class PostHolder {
    String TAG = "PostHolder";
    /**
     * We will be fetching JSON data from the API.
     */
    private final String URL_TEMPLATE=
            "https://www.reddit.com/r/SUBREDDIT_NAME/"
                    +".json"
                    +"?after=AFTER";

    String subreddit;
    String url;
    String after;

    PostHolder(String sr,String af){
        subreddit=sr;
        after=af;
        generateURL();
    }

    /**
     * Generates the actual URL from the template based on the
     * subreddit name and the 'after' property.
     */
    private void generateURL(){
        url=URL_TEMPLATE.replace("SUBREDDIT_NAME", subreddit);
        url=url.replace("AFTER", after);
    }

    /**
     * Returns a list of Post objects after fetching data from
     * Reddit using the JSON API.
     *
     * @return
     */
    List<Post> fetchPosts() throws MalformedURLException {
        Log.d(TAG, "fetchPosts()");
        Log.d(TAG, "url:" + url);
        String raw = RemoteData.readContents(url);

        if (raw == null)
        {
            Log.d(TAG, "response = null");
            return null;
        }

        Log.d(TAG, "Response: "+raw);
        List<Post> list=new ArrayList<Post>();
        try{
            JSONObject data=new JSONObject(raw)
                    .getJSONObject("data");
            JSONArray children=data.getJSONArray("children");
            Log.d(TAG, "data.Json[]"+ data.toString());
            Log.d(TAG, "children.Json[]"+ children.toString());
            //Using this property we can fetch the next set of
            //posts from the same subreddit
            after=data.getString("after");

            for(int i=0;i<children.length();i++){
                JSONObject cur=children.getJSONObject(i)
                        .getJSONObject("data");
                Post p=new Post();
                p.title=cur.optString("title");
                p.url=cur.optString("url");
                p.numComments=cur.optInt("num_comments");
                p.points=cur.optInt("score");
                p.author=cur.optString("author");
                p.subreddit=cur.optString("subreddit");
                p.permalink=cur.optString("permalink");
                p.domain=cur.optString("domain");
                p.id=cur.optString("id");
                p.created_at = new Date(cur.optLong("created_utc")*1000);

                if(p.title!=null)
                    list.add(p);
                Log.d(TAG,"children.json[i]:"+ p);
            }
        }catch(Exception e){
            Log.e(TAG, e.toString());
        }
        return list;
    }

    /**
     * This is to fetch the next set of posts
     * using the 'after' property
     * @return
     */
    List<Post> fetchMorePosts() throws MalformedURLException {
        generateURL();
        return fetchPosts();
    }
}
