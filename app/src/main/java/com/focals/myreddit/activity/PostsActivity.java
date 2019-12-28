package com.focals.myreddit.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.focals.myreddit.R;
import com.focals.myreddit.adapter.PostsAdapter;
import com.focals.myreddit.data.Post;
import com.focals.myreddit.data.RedditParser;
import com.focals.myreddit.network.NetworkUtil;

import java.util.ArrayList;

public class PostsActivity extends AppCompatActivity  implements PostsAdapter.ClickHandler {

    RecyclerView subredditRecyclerView;
    ArrayList<Post> postsList;
    TextView subredditNameView;
    int subRedditId;
    String subredditName;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rv_posts);

        subredditName = getIntent().getStringExtra("SubredditName");

        subredditRecyclerView = (RecyclerView) findViewById(R.id.rv_posts);
//        subredditNameView = (TextView) findViewById(R.id.tv_subredditName);
//        subredditNameView.setText(subredditName);


        // Start task with correct URL here
        String url = "https://api.reddit.com/r/" + subredditName + "/.json";

        SubredditTask subredditTask = new SubredditTask();
        subredditTask.execute(url);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(subredditName);
    }

    @Override
    public void onClick(int position, View view) {

        String postId = postsList.get(position).getId();

        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra("PostId", postId);
        intent.putExtra("SubredditName", subredditName);
        intent.putExtra("PostText", postsList.get(position).getTitle());

        startActivity(intent);
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

            PostsAdapter postsAdapter = new PostsAdapter(postsList, PostsActivity.this);
            GridLayoutManager layoutManager = new GridLayoutManager(PostsActivity.this, 1);

            subredditRecyclerView.setAdapter(postsAdapter);
            subredditRecyclerView.setLayoutManager(layoutManager);

        }
    }


}
