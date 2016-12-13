package com.reader.redditclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * Created by Surabhi Agrawal on 12/13/2016.
 */
public class WebViewActivity extends FragmentActivity {

    String TAG = "WebViewActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyBrowser webViewFragment = new MyBrowser();

        Intent i = this.getIntent();
        String link = i.getExtras().getString("link");

        Log.d(TAG, "URL [" + link + "]");
        webViewFragment.init(link);
        getFragmentManager().beginTransaction()
                .add(android.R.id.content, webViewFragment)
                .commit();

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

}
