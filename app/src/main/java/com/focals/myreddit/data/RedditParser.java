package com.focals.myreddit.data;

import android.text.Html;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RedditParser {


    static String reducedJson = "{\n" +
            "  \"replies\": {\n" +
            "    \"data\": {\n" +
            "      \"children\": [\n" +
            "        {\n" +
            "          \"data\": {\n" +
            "            \"replies\": {\n" +
            "              \"data\": {\n" +
            "                \"children\": [\n" +
            "                  {\n" +
            "                    \"data\": {\n" +
            "                      \"body_html\": \"<div class=\\\"md\\\"><p>Just start asking for free dances, you&#39;ll get shown out pretty quick.</p>\\n</div>\"\n" +
            "                    }\n" +
            "                  }\n" +
            "                ]\n" +
            "              }\n" +
            "            },\n" +
            "            \"body_html\": \"<div class=\\\"md\\\"><p>I guess a guy could do worse, but once you&#39;re out of singles, they&#39;ll come help you find the door and even push you OUT through it.  (been there-done that)</p>\\n</div>\"\n" +
            "          }\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  },\n" +
            "  \"body_html\": \"<div class=\\\"md\\\"><p>My friend got stuck at a strip club because he was drunk, the place was too dark and he couldn&#39;t find the exit.</p>\\n</div>\"\n" +
            "}";

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
                String id = subredditJson.getString("id");

                Subreddit subreddit = new Subreddit(name, bannerlUrl, publicDescription, subscribers, favorite, id);

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
                        videoUrl = postJson.getJSONObject("secure_media_embed").getString("media_domain_url");
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

/*                JSONObject commentJson = commentsArray.getJSONObject(i).getJSONObject("data");

                String commentBody = commentJson.getString("body_html");

                // This is to escape to html
                commentBody = Html.fromHtml(commentBody, Html.FROM_HTML_MODE_COMPACT).toString();

                // This is to read from above html
                commentBody = Html.fromHtml(commentBody, Html.FROM_HTML_MODE_COMPACT).toString();*/







                JSONObject commentJson = new JSONObject(reducedJson);
                String commentBody = commentJson.getString("body_html");
                commentBody = Html.fromHtml(commentBody, Html.FROM_HTML_MODE_COMPACT).toString();









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
