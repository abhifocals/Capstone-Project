package com.focals.myreddit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.focals.myreddit.data.RedditParser;
import com.focals.myreddit.data.Subreddit;
import com.focals.myreddit.network.NetworkUtil;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements MainAdapter.ClickHandler {

    RecyclerView mainRecyclerView;
    ArrayList<Subreddit> subredditList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String popularSubreddits = "https://api.reddit.com/subreddits/popular/.json";

        new RedditAsyncTask().execute(popularSubreddits);

        mainRecyclerView = (RecyclerView) findViewById(R.id.rv_main);
    }

    @Override
    public void onClickHandle(int position, View view) {

        if (view.getId() == R.id.imgbtn_addToFavorite) {
            // Add to Favorites here
        } else {
            // Launch SubredditActivity
            Intent intent = new Intent(this, SubredditActivity.class);
            String name = subredditList.get(position).getName();

            intent.putExtra("SubredditName", name);

            startActivity(intent);
        }
    }


    class RedditAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            return NetworkUtil.getResponseFromUrl(url[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            subredditList = RedditParser.parseReddit(s);

            MainAdapter mainAdapter = new MainAdapter(subredditList, MainActivity.this);
            GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 1);

            mainRecyclerView.setAdapter(mainAdapter);
            mainRecyclerView.setLayoutManager(layoutManager);

        }
    }
}
