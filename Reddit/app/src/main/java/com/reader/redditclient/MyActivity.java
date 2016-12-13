package com.reader.redditclient;

import android.app.Activity;
import android.os.Bundle;

//import android.support.v4.app.*;


public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my);
        addFragment();
    }

    public void addFragment(){
        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragments_holder,
                        PostsFragment.newInstance("AskReddit",""))
                .commit();

    }

}