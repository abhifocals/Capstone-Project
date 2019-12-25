package com.focals.myreddit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.focals.myreddit.data.Post;
import com.focals.myreddit.data.RedditParser;
import com.focals.myreddit.network.NetworkUtil;

import java.util.ArrayList;

public class SubredditActivity extends AppCompatActivity {

    RecyclerView subredditRecyclerView;
    ArrayList<Post> postsList;
    TextView subredditNameView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.subreddit);

        String subredditName = getIntent().getStringExtra("SubredditName");

        subredditRecyclerView = (RecyclerView) findViewById(R.id.rv_posts);
        subredditNameView = (TextView) findViewById(R.id.tv_subredditName);
        subredditNameView.setText(subredditName);




        // Start task with correct URL here
        String url = "https://api.reddit.com/r/" + subredditName + "/.json";

        SubredditTask subredditTask = new SubredditTask();
        subredditTask.execute(url);
    }


    class SubredditTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return NetworkUtil.getResponseFromUrl(urls[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            postsList = RedditParser.parseRedditPosts(s);

            SubredditAdapter subredditAdapter = new SubredditAdapter(postsList);
            GridLayoutManager layoutManager = new GridLayoutManager(SubredditActivity.this, 1);

            subredditRecyclerView.setAdapter(subredditAdapter);
            subredditRecyclerView.setLayoutManager(layoutManager);

        }
    }


}
