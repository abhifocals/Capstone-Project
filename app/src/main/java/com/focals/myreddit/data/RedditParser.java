package com.focals.myreddit.data;

import android.text.Html;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RedditParser {

    private static HashMap<String, ArrayList<Comment>> commentsMap;
    public static final ArrayList<HashMap<String, ArrayList<Comment>>> listOfCommentsMap = new ArrayList<>();

    static String logTag = "Testing";
    private static ArrayList<Comment> commentsList;
    public static final String TOP = "TOP";

    static String reducedJson = "[\n" +
            "    {\n" +
            "      \"data\": {\n" +
            "        \"replies\": {\n" +
            "          \"data\": {\n" +
            "            \"children\": [\n" +
            "              {\n" +
            "                \"data\": {\n" +
            "                  \"body_html\": \"R1\",\n" +
            "                  \"parent_id\": \"t1_C0\",\n" +
            "                  \"id\": \"R1\",\n" +
            "                  \"depth\": 1\n" +
            "                }\n" +
            "              },\n" +
            "              {\n" +
            "                \"dummy\": {}\n" +
            "              }\n" +
            "            ]\n" +
            "          }\n" +
            "        },\n" +
            "        \"body_html\": \"C0\",\n" +
            "        \"parent_id\": \"main\",\n" +
            "        \"id\": \"C0\",\n" +
            "        \"depth\": 0\n" +
            "      }\n" +
            "    },\n" +
            "    {\n" +
            "      \"data\": {\n" +
            "        \"replies\": {\n" +
            "          \"data\": {\n" +
            "            \"children\": [\n" +
            "              {\n" +
            "                \"data\": {\n" +
            "                  \"depth\": 1,\n" +
            "                  \"parent_id\": \"t1_C1\",\n" +
            "                  \"id\": \"R2\",\n" +
            "                  \"body_html\": \"R2\"\n" +
            "                }\n" +
            "              },\n" +
            "              {\n" +
            "                \"dummy\": {}\n" +
            "              }\n" +
            "            ]\n" +
            "          }\n" +
            "        },\n" +
            "        \"parent_id\": \"main\",\n" +
            "        \"id\": \"C1\",\n" +
            "        \"body_html\": \"C1\",\n" +
            "        \"depth\": 0\n" +
            "      }\n" +
            "    }\n" +
            "  ]";

    public static ArrayList<Subreddit> parseReddit(String response) {
        JSONObject jsonObject;
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

        JSONObject jsonObject;
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

        JSONArray jsonArray = new JSONArray(response);
        JSONArray commentsArray = jsonArray.getJSONObject(1).getJSONObject("data").getJSONArray("children");
        // JSONArray commentsArray = new JSONArray(reducedJson);
        Comment comment;
        String parentId = null;


        for (int i = 0; i < commentsArray.length(); i++) {
            commentsMap = new HashMap<>();
            commentsList = new ArrayList<>();
            JSONObject commentJson = commentsArray.getJSONObject(i).getJSONObject("data");
            // JSONObject commentJson = new JSONObject(reducedJson);

            // Building First Comment Object
            if (commentJson.has("body_html") && commentJson.getString("body_html") != null) {
                String mainComment = commentJson.getString("body_html");
                mainComment = getStringFromHtml(mainComment);
                String id = commentJson.getString("id");
                int depth = commentJson.getInt("depth");
                comment = new Comment(id, mainComment, depth);

                // Add main comment to the map
                commentsList.add(comment);
                commentsMap.put(TOP, commentsList);

                // Add replies (and their replies)
                addReplies(commentJson);

                listOfCommentsMap.add(commentsMap);
            }
        }
    }

    /**
     * Main recursive function to parse comment hierarchy.
     *
     * @param input Json to parse
     * @throws JSONException
     */
    private static void addReplies(JSONObject input) throws JSONException {
        if (!repliesExist(input)) {
            return;

        } else {
            addAllReplies(input);

            for (int i = 0; i < input.getJSONObject("replies").getJSONObject("data").getJSONArray("children").length() - 1; i++) {
                addReplies(input.getJSONObject("replies").getJSONObject("data").getJSONArray("children").getJSONObject(i).getJSONObject("data"));
            }
        }
    }

    /**
     * Helper for addReplies().
     *
     * @param input
     * @throws JSONException
     */

    private static void addAllReplies(JSONObject input) throws JSONException {
        JSONArray replies = input.getJSONObject("replies").getJSONObject("data").getJSONArray("children");

        // Create new list only if no existing comments at that depth
        String parentId = replies.getJSONObject(0).getJSONObject("data").getString("parent_id");
        if (commentsMap.get(parentId) == null) {
            commentsList = new ArrayList<>();
        }

        for (int i = 0; i < replies.length() - 1; i++) {

            if (replies.getJSONObject(i).getJSONObject("data").has("body_html") && replies.getJSONObject(i).getJSONObject("data").getString("body_html") != null) {
                String reply = replies.getJSONObject(i).getJSONObject("data").getString("body_html");
                parentId = replies.getJSONObject(i).getJSONObject("data").getString("parent_id");
                reply = getStringFromHtml(reply);
                int depth = replies.getJSONObject(i).getJSONObject("data").getInt("depth");
                String id = replies.getJSONObject(i).getJSONObject("data").getString("id");

                Comment comment = new Comment(id, reply, depth);
                commentsList.add(comment);
                commentsMap.put(parentId, commentsList);
            }
        }
    }

    /**
     * Helper for addReplies().
     *
     * @param input
     * @return
     */
    private static boolean repliesExist(JSONObject input) {
        try {
            if (input.getJSONObject("replies") != null) {
                return true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Helper for addReplies().
     *
     * @param input
     * @return
     */
    private static String getStringFromHtml(String input) {
        // This is to escape to html
        input = Html.fromHtml(input, Html.FROM_HTML_MODE_COMPACT).toString();

        // This is to read from above html
        input = Html.fromHtml(input, Html.FROM_HTML_MODE_COMPACT).toString();

        return input;
    }
}



