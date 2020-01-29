package com.focals.myreddit.data;

public class Post {

    private String subredditName;
    private String title;
    private int score;
    private int numComments;
    private String id;
    private String imageUrl;
    private String videoUrl;

    public Post(String subredditName, String title, int score, int numComments, String id, String imageUrl, String videoUrl) {
        this.subredditName = subredditName;
        this.title = title;
        this.score = score;
        this.numComments = numComments;
        this.id = id;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
