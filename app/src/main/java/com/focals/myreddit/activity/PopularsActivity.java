package com.focals.myreddit.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.focals.myreddit.R;
import com.focals.myreddit.adapter.PopularsAdapter;
import com.focals.myreddit.data.RedditParser;
import com.focals.myreddit.data.Subreddit;
import com.focals.myreddit.network.NetworkUtil;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.blurry.Blurry;

public class PopularsActivity extends AppCompatActivity implements PopularsAdapter.ClickHandler {

    RecyclerView mainRecyclerView;
    ArrayList<Subreddit> subredditList;
    Toolbar toolbar;
    ImageView iv_appbarImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_populars);

        String popularSubreddits = "https://api.reddit.com/subreddits/popular/.json";

        new RedditAsyncTask().execute(popularSubreddits);

        mainRecyclerView = (RecyclerView) findViewById(R.id.rv_populars);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        iv_appbarImage = (ImageView) findViewById(R.id.expandedImage);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background1);

        Blurry.with(this).radius(10).sampling(2).async().from(bitmap).into(iv_appbarImage);


    }


    @Override
    public void onClickHandle(int position, View view) {

        if (view.getId() == R.id.ib_addToFavorites) {
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
