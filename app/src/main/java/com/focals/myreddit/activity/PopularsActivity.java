package com.focals.myreddit.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.focals.myreddit.R;
import com.focals.myreddit.adapter.PopularsAdapter;
import com.focals.myreddit.data.RedditParser;
import com.focals.myreddit.data.Subreddit;
import com.focals.myreddit.network.NetworkUtil;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PopularsActivity extends AppCompatActivity implements PopularsAdapter.ClickHandler {

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
            // Launch PostsActivity
            Intent intent = new Intent(this, PostsActivity.class);
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

            PopularsAdapter popularsAdapter = new PopularsAdapter(subredditList, PopularsActivity.this);
            GridLayoutManager layoutManager = new GridLayoutManager(PopularsActivity.this, 1);

            mainRecyclerView.setAdapter(popularsAdapter);
            mainRecyclerView.setLayoutManager(layoutManager);

        }
    }
}
