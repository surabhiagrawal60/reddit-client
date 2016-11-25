package com.surabhi.redit;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Surabhi Agrawal on 11/18/2016.
 */
public class PostsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    ListView postList;
    ArrayAdapter<Post> adapter;
    Handler handler;
    View footerView;
    Button loadMore;
    String subreddit;
    String after;
    List<Post> posts;
    PostHolder postHolder;
    String TAG = "PostFragment";
    SwipeRefreshLayout swipeLayout;


    public PostsFragment()
    {
        handler = new Handler();
        posts = new ArrayList<Post>();
    }

    public static Fragment newInstance(String subreddit,String after){
        PostsFragment pf= new PostsFragment();
        pf.subreddit = subreddit;
        pf.after = after;
        pf.postHolder = new PostHolder(pf.subreddit,pf.after);
        return  pf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstance){
        View v = inflater.inflate(R.layout.posts,container,false);
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.footer_view, null);
        postList = (ListView)v.findViewById(R.id.posts_list);
        postList.addFooterView(footerView);

        loadMore = (Button) footerView.findViewById(R.id.loadmore);
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new Thread(){
                        public void run(){
                            try {
                                posts.addAll(postHolder.fetchMorePosts());
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    createAdapter();
                                }
                            });
                        }
                    }.start();

            }
        });

        postList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post post = adapter.getItem(position);
                String url = post.url;
                MyBrowser myWebView = new MyBrowser();
                myWebView.init(url);
                getFragmentManager().beginTransaction().add(android.R.id.content,myWebView).commit();

            }
        });
//        postList.setOnScrollListener(PostsFragment.this);
//        footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_view, null, false);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setRetainInstance(true);
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String access_token = preferences.getString("accessToken","");
//        String refresh_token = preferences.getString("refreshToken","");

//        if(access_token == null)
//        {
//            Intent intent = new Intent(getActivity(),SignIn.class);
//            startActivity(intent);
//        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);
        initialize();
    }

    private void initialize(){
        if(posts.size() == 0)
        {
            new Thread(){
                public void run(){
                    try {
                        posts.addAll(postHolder.fetchPosts());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            createAdapter();
                        }
                    });
                }
            }.start();
        }else{
            createAdapter();
        }
    }

    private void createAdapter(){
        if(getActivity() == null) return;

        adapter = new ArrayAdapter<Post>(getActivity(),R.layout.post_item,posts){
            @Override
            public View getView(int position,View convertView, ViewGroup parent)
            {
                if(convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.post_item, null);
                }

                TextView postTitle;
                postTitle= (TextView) convertView.findViewById(R.id.post_title);

                TextView postDetails;
                postDetails =(TextView) convertView.findViewById(R.id.post_details);

                TextView postScore;
                postScore = (TextView) convertView.findViewById(R.id.post_score);

                TextView tv_postComments;
                tv_postComments = (TextView) convertView.findViewById(R.id.tv_comments);

                TextView tv_postAuthor;
                tv_postAuthor = (TextView) convertView.findViewById(R.id.tv_author);


                postTitle.setText(posts.get(position).getTitle());
                postDetails.setText(posts.get(position).getCreatedTime());
                postScore.setText(posts.get(position).getScore());
                tv_postComments.setText(posts.get(position).getComments()+"");
                tv_postAuthor.setText("u/"+posts.get(position).getAuthor());

                return convertView;
            }

            @Override
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
            }

            @Override
            public Post getItem(int position) {
                return super.getItem(position);
            }
        };


        postList.setAdapter(adapter);
    }

//    @Override
//    public void onResume(){
//        super.onResume();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    posts.addAll(postHolder.fetchPosts());
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        createAdapter();
//                        Log.d(TAG, "List Refreshed");
//                    }
//                });
//            }
//        },10000);
//    }

    @Override
    public void onRefresh() {
        swipeLayout.setRefreshing(false);
        new Thread(){
            public void run(){
                try {
                    posts.addAll(postHolder.fetchPosts());

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        createAdapter();
                    }
                });

            }
        }.start();
    }
}
