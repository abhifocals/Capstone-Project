package com.focals.myreddit.data;

import android.os.AsyncTask;
import android.util.JsonReader;

import com.focals.myreddit.network.RedditAsyncTask;

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


            for (int i=0; i < subredditsArray.length(); i++) {

                JSONObject subredditJson = subredditsArray.getJSONObject(i).getJSONObject("data");

                String name = subredditJson.getString("display_name");
                String bannerlUrl = subredditJson.getString("banner_img");
                String publicDescription = subredditJson.getString("public_description");
                int subscribers = subredditJson.getInt("subscribers");

                Subreddit subreddit = new Subreddit(name, bannerlUrl, publicDescription, subscribers);

                subredditsList.add(subreddit);

            }



            System.out.println();



















        } catch (JSONException e) {
            e.printStackTrace();
        }


















    }






}
