package com.focals.myreddit.data;

import java.util.ArrayList;
import java.util.HashMap;

public class Comment {

    private String id;
    private String text;
    private int depth;


    public Comment(String id, String text, int depth) {
        this.id = id;
        this.text = text;
        this.depth = depth;
    }



    private String subredditName;
    private String postText;
    private String commentBody;
    private HashMap<Integer, ArrayList<String>> repliesMap;


    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

}
