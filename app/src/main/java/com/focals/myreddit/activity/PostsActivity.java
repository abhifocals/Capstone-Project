package com.focals.myreddit.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.focals.myreddit.R;
import com.focals.myreddit.adapter.PostsAdapter;
import com.focals.myreddit.data.Post;
import com.focals.myreddit.data.RedditParser;
import com.focals.myreddit.database.SubDao;
import com.focals.myreddit.database.SubDatabase;
import com.focals.myreddit.network.NetworkUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PostsActivity extends BaseActivity implements PostsAdapter.ClickHandler, BottomNavigationView.OnNavigationItemSelectedListener {

    private RecyclerView subredditRecyclerView;
    private ArrayList<Post> postsList;
    private String subredditName;
    private Toolbar toolbar;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_posts);

        // Initializing Views
        subredditRecyclerView = findViewById(R.id.rv_posts);
        toolbar = findViewById(R.id.toolbar);
        imageButton = findViewById(R.id.ib_addToFavorites);
        bottomNav = findViewById(R.id.bottomNav);
        progressBar = findViewById(R.id.progressBar);

        // Initializing Database
        SubDatabase db = SubDatabase.getInstance(this);
        SubDao dao = db.subDao();

        // Getting info from PopularsActivity
        subredditName = getIntent().getStringExtra("SubredditName");
        subRedditId = getIntent().getStringExtra("SubId");
        subIsFavorite = getIntent().getBooleanExtra("IsFavorite", false);

        // Start task to fetch Posts
        String url = "https://api.reddit.com/r/" + subredditName + "/?raw_json=1";
        SubredditTask subredditTask = new SubredditTask();
        subredditTask.execute(url);
        progressBar.setVisibility(View.VISIBLE);

        // Set ActionBar and its Title
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("  " + subredditName);

        // Display Back Button in Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setup Listener for Bottom Navigation View
        bottomNav.setOnNavigationItemSelectedListener(this);

        // Set Correct State for Add/Remove Fav Button
        imageButton.setVisibility(View.VISIBLE);
        if (subIsFavorite) {
            imageButton.setImageDrawable(getDrawable(android.R.drawable.ic_delete));
        } else {
            imageButton.setImageDrawable(getDrawable(android.R.drawable.ic_input_add));
        }
    }

    @Override
    public void onClick(int position, View view) {

        String postId = postsList.get(position).getId();

        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra("PostId", postId);
        intent.putExtra("SubredditName", subredditName);
        intent.putExtra("PostText", postsList.get(position).getTitle());
        intent.putExtra("ImageUrl", postsList.get(position).getImageUrl());
        intent.putExtra("VideoUrl", postsList.get(position).getVideoUrl());
        intent.putExtra("SubId", subRedditId);
        intent.putExtra("IsFavorite", subIsFavorite);

        // Start Activity w/Transition
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivityForResult(intent, 100, bundle);
    }

    public void addRemoveFavorite(final View view) {
        super.addRemoveFavorite(view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2) {
            setResult(2);
        }

        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        // Set a result code and send back to Populars Activity to Load Favorites
        if (menuItem.getItemId() == R.id.bottom_favorite) {
            setResult(2);
            finish();
        }

        // Send back to Populars Activity to Load Populars
        if (menuItem.getItemId() == R.id.bottom_popular) {
            finish();
        }

        return false;
    }

    class SubredditTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return NetworkUtil.getResponseFromUrl(urls[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);

            postsList = RedditParser.parseRedditPosts(s);

            PostsAdapter postsAdapter = new PostsAdapter(postsList, PostsActivity.this);
            GridLayoutManager layoutManager = new GridLayoutManager(PostsActivity.this, 1);
            subredditRecyclerView.setAdapter(postsAdapter);
            subredditRecyclerView.setLayoutManager(layoutManager);
        }
    }

}
