package com.focals.myreddit;

import android.os.AsyncTask;
import android.os.Bundle;

import com.focals.myreddit.data.RedditParser;
import com.focals.myreddit.network.NetworkUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    RecyclerView mainRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new RedditAsyncTask().execute();

        mainRecyclerView = (RecyclerView) findViewById(R.id.rv_main);

        MainAdapter mainAdapter = new MainAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);

        mainRecyclerView.setAdapter(mainAdapter);
        mainRecyclerView.setLayoutManager(layoutManager);
    }


    class RedditAsyncTask extends AsyncTask <Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return NetworkUtil.getResponseFromUrl(NetworkUtil.searchUrl);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            RedditParser.parseReddit(s);

        }
    }
}
