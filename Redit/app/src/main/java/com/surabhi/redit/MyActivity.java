package com.surabhi.redit;

import android.app.Activity;
import android.os.Bundle;

//import android.support.v4.app.*;


public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
