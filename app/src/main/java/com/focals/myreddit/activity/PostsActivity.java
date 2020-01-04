package com.focals.myreddit.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.focals.myreddit.R;
import com.focals.myreddit.adapter.PostsAdapter;
import com.focals.myreddit.data.Post;
import com.focals.myreddit.data.RedditParser;
import com.focals.myreddit.network.NetworkUtil;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PostsActivity extends BaseActivity implements PostsAdapter.ClickHandler {

    RecyclerView subredditRecyclerView;
    ArrayList<Post> postsList;
    TextView subredditNameView;
    int subRedditId;
    String subredditName;
    Toolbar toolbar;
    ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rv_posts);

        subredditName = getIntent().getStringExtra("SubredditName");

        subredditRecyclerView = (RecyclerView) findViewById(R.id.rv_posts);

        // Start task with correct URL here
        String url = "https://api.reddit.com/r/pics/?raw_json=1";

        SubredditTask subredditTask = new SubredditTask();
        subredditTask.execute(url);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(subredditName);

        imageButton = (ImageButton) findViewById(R.id.ib_addToFavorites);
        imageButton.setVisibility(View.VISIBLE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(int position, View view) {

        String postId = postsList.get(position).getId();

        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra("PostId", postId);
        intent.putExtra("SubredditName", subredditName);
        intent.putExtra("PostText", postsList.get(position).getTitle());

        // Start Activity w/Transition
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivityForResult(intent,100, bundle);
    }

    public void addRemoveFavorite(View view) {

        System.out.println();

        // Get Sub by Id - Add to DB

        // If Sub is a Fav: Remove from Fav List; Update Sub to be not Fav

        // If Sub is not a Fav: Add sub to Fav List; Update Sub to be Fav


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_favorite) {
            setResult(2);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2) {
            setResult(2);
            finish();
        }
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
