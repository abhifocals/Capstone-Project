package com.focals.myreddit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import com.focals.myreddit.network.NetworkUtil;

public class SubredditActivity extends AppCompatActivity {

    RecyclerView subredditRecyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.subreddit);

        subredditRecyclerView = (RecyclerView) findViewById(R.id.rv_posts);

        String subredditName = getIntent().getStringExtra("SubredditName");


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




        }
    }


}
