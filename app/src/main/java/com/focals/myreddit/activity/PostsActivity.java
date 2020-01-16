package com.focals.myreddit.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

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

        // Fetching Posts
        String url = "https://api.reddit.com/r/" + subredditName + "/?raw_json=1";
        SubredditTask subredditTask = new SubredditTask();
        subredditTask.execute(url);
        progressBar.setVisibility(View.VISIBLE);

        // Setting ActionBar and its Title
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("  " + subredditName);

        // Displaying Back Button in Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setting up Listener for Bottom Navigation View
        bottomNav.setOnNavigationItemSelectedListener(this);

        // Setting Image for Favorite Button
        setFavButtonImage(imageButton);
    }


    /**
     * Handles clicks of all views except Favorite Button.
     *
     * @param position
     * @param view
     */
    @Override
    public void onClick(int position, View view) {

        String postId = postsList.get(position).getId();

        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra("PostId", postId)
                .putExtra("SubredditName", subredditName)
                .putExtra("PostText", postsList.get(position).getTitle())
                .putExtra("ImageUrl", postsList.get(position).getImageUrl())
                .putExtra("VideoUrl", postsList.get(position).getVideoUrl())
                .putExtra("SubId", subRedditId)
                .putExtra("IsFavorite", subIsFavorite);

        // Start Activity w/Transition
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivityForResult(intent, 100, bundle);
    }

    /**
     * Click action for add/remove favorite button in Toolbar
     *
     * @param view
     */
    public void addRemoveFavorite(final View view) {
        super.addRemoveFavorite(view);
    }


    /**
     * Receive code from FavoritesActivity and send back to Populars to load Favorites
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
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
        return super.onNavigationItemSelected(menuItem);
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

            // Parsing response
            postsList = RedditParser.parseRedditPosts(s);

            // Setting up Adapter
            PostsAdapter postsAdapter = new PostsAdapter(postsList, PostsActivity.this);
            GridLayoutManager layoutManager = new GridLayoutManager(PostsActivity.this, 1);
            subredditRecyclerView.setAdapter(postsAdapter);
            subredditRecyclerView.setLayoutManager(layoutManager);
        }
    }
}
