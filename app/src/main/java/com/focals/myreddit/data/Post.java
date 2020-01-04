package com.focals.myreddit.data;

public class Post {

    String subredditName;
    String title;
    int score;
    int numComments;
    String id;
    String imageUrl;
    String videoUrl;

    public Post(String subredditName, String title, int score, int numComments, String id, String imageUrl) {
        this.subredditName = subredditName;
        this.title = title;
        this.score = score;
        this.numComments = numComments;
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public String getSubredditName() {
        return subredditName;
    }

    public String getTitle() {
        return title;
    }

    public int getScore() {
        return score;
    }

    public int getNumComments() {
        return numComments;
    }

    public String getId() {
        return id;
    }
}
