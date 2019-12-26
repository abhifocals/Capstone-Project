package com.focals.myreddit.data;

public class Comment {

    String subredditName;
    String postText;
    String commentBody;
    int[] replies;
    Comment[] replyToComments;

    public Comment(String subredditName, String postText, String commentBody, int[] replies) {
        this.subredditName = subredditName;
        this.postText = postText;
        this.commentBody = commentBody;
        this.replies = replies;
    }

    public Comment(String subredditName, String postText, String commentBody, Comment[] replyToComments) {
        this.subredditName = subredditName;
        this.postText = postText;
        this.commentBody = commentBody;
        this.replyToComments = replyToComments;
    }

    public String getSubredditName() {
        return subredditName;
    }

    public String getPostText() {
        return postText;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public int[] getReplies() {
        return replies;
    }
}
