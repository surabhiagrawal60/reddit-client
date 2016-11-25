package com.surabhi.redit;

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

    String getAuthor()
    {
        return author;
    }

    String getCreatedTime()
    {
        Date currentTime = new Date();


        long diff = currentTime.getTime() - created_at.getTime();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        if (diffDays == 0)
            if (diffHours == 0)
                if(diffMinutes ==0)
                    return "just now";
                else if (diffMinutes == 1)
                    return diffMinutes + " min";
                else
                    return diffMinutes + " mins";
            else if (diffHours == 1)
                return diffHours + " hr";
            else
                return diffHours + " hrs";
        else if (diffDays == 1)
            return diffDays + " day";
        else
            return diffDays + " days";

//
//
//        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("hh:mm a");
//        String formattedDate = simpleDateFormat.format(created_at);
//        return formattedDate;
    }




    int getComments()
    {
        return numComments;
    }
    String getTitle(){
        return title;
    }

    String getScore(){
        return Integer.toString(points);
    }
}
