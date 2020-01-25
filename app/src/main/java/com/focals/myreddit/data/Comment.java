package com.focals.myreddit.data;

import java.util.ArrayList;
import java.util.HashMap;

public class Comment {

    String id;
    String text;
    int depth;


    public Comment(String id, String text, int depth) {
        this.id = id;
        this.text = text;
        this.depth = depth;
    }



    String subredditName;
    String postText;
    String commentBody;
    HashMap<Integer, ArrayList<String>> repliesMap;

    public Comment(String subredditName, String postText, String commentBody, HashMap<Integer, ArrayList<String>> repliesMap) {
        this.subredditName = subredditName;
        this.postText = postText;
        this.commentBody = commentBody;
        this.repliesMap = repliesMap;
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

    public HashMap<Integer, ArrayList<String>> getRepliesMap() {
        return repliesMap;
    }


    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getDepth() {
        return depth;
    }
}
