package com.focals.myreddit.data;

public class Post {

    String subredditName;
    String title;
    int score;
    int numComments;
    String id;

    public Post(String subredditName, String title, int score, int numComments, String id) {
        this.subredditName = subredditName;
        this.title = title;
        this.score = score;
        this.numComments = numComments;
        this.id = id;
    }
}
