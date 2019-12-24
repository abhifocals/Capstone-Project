package com.focals.myreddit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

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

        new RedditAsyncTask().execute();

        mainRecyclerView = (RecyclerView) findViewById(R.id.rv_main);
    }

    @Override
    public void onClickHandle(int position) {

        // Launch RedditActivity

        Intent intent = new Intent(this,  PostsActivity.class);
        startActivity(intent);
    }


    class RedditAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return NetworkUtil.getResponseFromUrl(NetworkUtil.searchUrl);
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
