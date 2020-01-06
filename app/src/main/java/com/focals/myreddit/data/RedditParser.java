package com.focals.myreddit.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RedditParser {

    public static ArrayList<Subreddit> parseReddit(String response) {
        JSONObject jsonObject = null;
        ArrayList<Subreddit> subredditsList = new ArrayList<>();

        try {
            jsonObject = new JSONObject(response);

            JSONArray subredditsArray = jsonObject.getJSONObject("data").getJSONArray("children");


            for (int i = 0; i < subredditsArray.length(); i++) {

                JSONObject subredditJson = subredditsArray.getJSONObject(i).getJSONObject("data");

                String name = subredditJson.getString("display_name");
                String bannerlUrl = subredditJson.getString("banner_img");
                String publicDescription = subredditJson.getString("public_description");
                int subscribers = subredditJson.getInt("subscribers");
                boolean favorite = false;

                Subreddit subreddit = new Subreddit(name, bannerlUrl, publicDescription, subscribers, favorite);

                subredditsList.add(subreddit);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return subredditsList;
    }

    public static ArrayList<Post> parseRedditPosts(String response) {

        JSONObject jsonObject = null;
        ArrayList<Post> postsList = new ArrayList<>();

        try {
            jsonObject = new JSONObject(response);

            JSONArray postsArray = jsonObject.getJSONObject("data").getJSONArray("children");

            for (int i = 0; i < postsArray.length(); i++) {

                JSONObject postJson = postsArray.getJSONObject(i).getJSONObject("data");

                String subRedditName = postJson.getString("subreddit");
                String title = postJson.getString("title");
                int score = postJson.getInt("score");
                int numComments = postJson.getInt("num_comments");
                String id = postJson.getString("id");
                String imageUrl = null;
                String videoUrl = null;


                if (postJson.has("post_hint")) {
                    if (postJson.getString("post_hint").equals("image")) {
                        imageUrl = postJson.getJSONObject("preview").getJSONArray("images")
                                .getJSONObject(0).getJSONObject("source")
                                .getString("url");
                    }

                    if (postJson.getString("post_hint").equals("rich:video")) {
                        videoUrl = postJson.getString("url");
                    }
                }

                Post post = new Post(subRedditName, title, score, numComments, id, imageUrl, videoUrl);

                postsList.add(post);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return postsList;
    }

    public static ArrayList<Comment> parseComments(String response) {

        JSONArray jsonArray = null;
        ArrayList<Comment> commentsList = new ArrayList<>();

        try {
            jsonArray = new JSONArray(response);

            JSONArray subredditArray = jsonArray.getJSONObject(0).getJSONObject("data").getJSONArray("children");
            JSONArray commentsArray = jsonArray.getJSONObject(1).getJSONObject("data").getJSONArray("children");

            JSONObject subredditJson = subredditArray.getJSONObject(0).getJSONObject("data");
            String subRedditName = subredditJson.getString("subreddit");
            String title = subredditJson.getString("title");

            for (int i = 0; i < commentsArray.length() - 1; i++) {

                JSONObject commentJson = commentsArray.getJSONObject(i).getJSONObject("data");

                String commentBody = commentJson.getString("body");
                int[] replies = new int[1];
                Comment comment = new Comment(subRedditName, title, commentBody, replies);
                commentsList.add(comment);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return commentsList;
    }

    public static void recurseComments(JSONObject commentJson) {

        try {

            if (commentJson.getJSONObject("replies") == null) {
                return;
            }

            if (commentJson.getJSONObject("replies") != null) {

                JSONArray childrenArray = commentJson.getJSONObject("replies").getJSONObject("data").getJSONArray("children");

                for (int i = 0; i < childrenArray.length() - 1; i++) {
                    JSONObject childJson = childrenArray.getJSONObject(i).getJSONObject("data");

                    String body = childJson.getString("body");

                    Log.d(RedditParser.class.getSimpleName(), body);

                    recurseComments(childJson);

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        System.out.println();


    }

}
