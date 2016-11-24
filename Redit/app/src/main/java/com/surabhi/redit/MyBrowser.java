package com.surabhi.redit;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
    WebView wv;
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
            wv = (WebView) v.findViewById(R.id.webview);
            wv.getSettings().setJavaScriptEnabled(true);
            wv.setWebViewClient(new SwAWebClient() );
            wv.loadUrl(currentUrl);
        }
        return v;
    }

    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                   Intent intent = new Intent(getActivity(),MyActivity.class);
                   startActivity(intent);
                   return true;
                }

                return false;
            }
        });
    }

    private class SwAWebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

    }



}

