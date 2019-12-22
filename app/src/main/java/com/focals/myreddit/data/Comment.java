package com.focals.myreddit.data;

public class Comment {

    String subredditName;
    String title; // Subreddit Post
    String commentBody;
    int[] replies;

    public Comment(String subredditName, String title, String commentBody, int[] replies) {
        this.subredditName = subredditName;
        this.title = title;
        this.commentBody = commentBody;
        this.replies = replies;
    }
}
