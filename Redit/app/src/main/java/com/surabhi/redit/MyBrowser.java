package com.surabhi.redit;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
* Created by Surabhi Agrawal on 11/20/2016.
*/
public class MyBrowser extends Fragment{

    private String currentUrl;

    public void init(String url)
    {
        currentUrl = url;
    }

    @Override
    public void onActivityCreated(Bundle savedInstance)
    {
        super.onActivityCreated(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        Log.d("SwA", "WVF onCreateView");
        View v = inflater.inflate(R.layout.webview,container,false);
        if(currentUrl != null)
        {
            WebView wv = (WebView) v.findViewById(R.id.webview);
            wv.getSettings().setJavaScriptEnabled(true);
            wv.setWebViewClient(new SwAWebClient() );
            wv.loadUrl(currentUrl);
        }
        return v;
    }

    public void updateUrl(String url)
    {
        currentUrl =url;
        WebView wv = (WebView) getView().findViewById(R.id.webview);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl(url);
    }

    private class SwAWebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

    }

}

