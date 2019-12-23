package com.focals.myreddit.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RedditParser {

    public static void parseReddit(String response) {
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

                Subreddit subreddit = new Subreddit(name, bannerlUrl, publicDescription, subscribers);

                subredditsList.add(subreddit);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void parseRedditPosts(String response) {

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

                Post post = new Post(subRedditName, title, score, numComments, id);

                postsList.add(post);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
