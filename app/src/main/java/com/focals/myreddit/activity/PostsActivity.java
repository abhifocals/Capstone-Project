package com.focals.myreddit.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Resources;
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
        subredditName = getIntent().getStringExtra(getString(R.string.sub_name));
        subRedditId = getIntent().getStringExtra(getString(R.string.sub_id));


        if (savedInstanceState != null) {
            subIsFavorite = savedInstanceState.getBoolean(getString(R.string.is_favorite));
        } else {
            subIsFavorite = getIntent().getBooleanExtra(getString(R.string.is_favorite), false);
        }

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
     * Launches CommentsActivity.
     *
     * @param position
     * @param view
     */
    @Override
    public void onClick(int position, View view) {

        String postId = postsList.get(position).getId();

        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra(getString(R.string.post_id), postId)
                .putExtra(getString(R.string.sub_name), subredditName)
                .putExtra(getString(R.string.post_text), postsList.get(position).getTitle())
                .putExtra(getString(R.string.image_url), postsList.get(position).getImageUrl())
                .putExtra(getString(R.string.video_url), postsList.get(position).getVideoUrl())
                .putExtra(getString(R.string.sub_id), subRedditId)
                .putExtra(getString(R.string.is_favorite), subIsFavorite);

        // Start Activity w/Transition
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivityForResult(intent, 100, bundle);
    }

    /**
     * Upates Database and Image for Favorite Button.
     *
     * @param view
     */
    public void addRemoveFavorite(final View view) {
        super.addRemoveFavorite(view);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(getString(R.string.is_favorite), subIsFavorite);
    }


    /**
     * Receive code from FavoritesActivity and send back to Populars to load Favorites.
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
            finish();
        } else if (resultCode == 3) {
            setResult(3);
            finish();
        } else if (resultCode == 4) {
            setResult(4);
            finish();
        }
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
