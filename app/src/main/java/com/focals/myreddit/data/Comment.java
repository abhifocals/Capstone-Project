package com.focals.myreddit.data;

import java.util.ArrayList;
import java.util.HashMap;

public class Comment {

    private final String id;
    private final String text;


    public Comment(String id, String text, int depth) {
        this.id = id;
        this.text = text;
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
