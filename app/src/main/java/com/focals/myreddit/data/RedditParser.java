package com.focals.myreddit.data;

import android.text.Html;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class RedditParser {

    static HashMap<Integer, ArrayList<String>> commentsMap = new HashMap<>();
    static String logTag = "Testing";
    static ArrayList<String> commentsList = new ArrayList<>();

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
            "                      \"depth\": 2,\n" +
            "                      \"body_html\": \"C-1.1.1\"\n" +
            "                    }\n" +
            "                  }\n" +
            "                ]\n" +
            "              }\n" +
            "            },\n" +
            "            \"depth\": 1,\n" +
            "            \"body_html\": \"C-1.1\"\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"data\": {\n" +
            "            \"replies\": {\n" +
            "              \"data\": {\n" +
            "                \"children\": [\n" +
            "                  {\n" +
            "                    \"data\": {\n" +
            "                      \"depth\": 2,\n" +
            "                      \"body_html\": \"C-1.2.1\"\n" +
            "                    }\n" +
            "                  }\n" +
            "                ]\n" +
            "              }\n" +
            "            },\n" +
            "            \"depth\": 1,\n" +
            "            \"body_html\": \"C-1.2\"\n" +
            "          }\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  },\n" +
            "  \"depth\": 0,\n" +
            "  \"body_html\": \"C-0\"\n" +
            "}\n";

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


    public static void parseComments(String response) throws JSONException {
        JSONObject commentJson = new JSONObject(reducedJson);
        ArrayList<String> commentsList = new ArrayList<>();

        String mainComment = commentJson.getString("body_html");
        commentsList.add(getStringFromHtml(mainComment));

        // Add main comment to the map
        commentsMap.put(0, commentsList);

        // Add replies (and their replies)
        addReplies(commentJson);

        System.out.println();
    }


    private static void addReplies(JSONObject input) throws JSONException {
        if (!repliesExist(input)) {
            return;

        } else {
            addAllReplies(input);

            for (int i = 0; i < input.getJSONObject("replies").getJSONObject("data").getJSONArray("children").length(); i++) {
                addReplies(input.getJSONObject("replies").getJSONObject("data").getJSONArray("children").getJSONObject(i).getJSONObject("data"));
            }
        }
    }

    private static void addAllReplies(JSONObject input) throws JSONException {
        JSONArray replies = input.getJSONObject("replies").getJSONObject("data").getJSONArray("children");

        // Create new list only if no existing comments at that depth
        int depth = replies.getJSONObject(0).getJSONObject("data").getInt("depth");
        if (commentsMap.get(depth) == null) {
            commentsList = new ArrayList<>();
        }

        for (int i = 0; i < replies.length(); i++) {
            String reply = replies.getJSONObject(i).getJSONObject("data").getString("body_html");

            commentsList.add(getStringFromHtml(reply));
            commentsMap.put(depth, commentsList);
        }
    }


    private static boolean repliesExist(JSONObject input) throws JSONException {
        return input.has("replies");
    }

    private static String getStringFromHtml(String input) {
        // This is to escape to html
        input = Html.fromHtml(input, Html.FROM_HTML_MODE_COMPACT).toString();

        // This is to read from above html
        input = Html.fromHtml(input, Html.FROM_HTML_MODE_COMPACT).toString();

        return input;
    }
}



