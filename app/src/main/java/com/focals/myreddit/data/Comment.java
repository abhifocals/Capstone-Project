package com.focals.myreddit.data;

public class Comment {

    String subredditName;
    String title; // Subreddit Post
    String commentBody;
    int[] replies;
    Comment[] replyToComments;

    public Comment(String subredditName, String title, String commentBody, int[] replies) {
        this.subredditName = subredditName;
        this.title = title;
        this.commentBody = commentBody;
        this.replies = replies;
    }

    public Comment(String subredditName, String title, String commentBody, Comment[] replyToComments) {
        this.subredditName = subredditName;
        this.title = title;
        this.commentBody = commentBody;
        this.replyToComments = replyToComments;
    }

    public String getSubredditName() {
        return subredditName;
    }

    public String getTitle() {
        return title;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public int[] getReplies() {
        return replies;
    }
}
