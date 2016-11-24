package com.surabhi.redit;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Surabhi Agrawal on 11/18/2016.
 */
public class Post {
    String subreddit;
    String title;
    String author;
    int points;
    int numComments;
    String permalink;
    String url;
    String domain;
    String id;
    Date created_at;

    String getDetails(){

        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("hh:mm a");
        String formattedDate = simpleDateFormat.format(created_at);


        String details="submitted at " +
                formattedDate+
                " by "+
                author +
                "\n" +
                numComments +
                " comments";

        return details;
    }

    String getTitle(){
        return title;
    }

    String getScore(){
        return Integer.toString(points);
    }
}
