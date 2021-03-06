package com.focals.myreddit.activity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.focals.myreddit.R;
import com.focals.myreddit.adapter.CommentsAdapter;
import com.focals.myreddit.data.Comment;
import com.focals.myreddit.data.RedditParser;
import com.focals.myreddit.network.NetworkUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CommentsActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private RecyclerView commentsRecyclerView;
    private ImageView iv_postImage;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_comments);

        // Initializing Views
        commentsRecyclerView = findViewById(R.id.rv_comments);
        TextView postTextView = findViewById(R.id.tv_postText);
        iv_postImage = findViewById(R.id.iv_postImage);
        webView = findViewById(R.id.webView);
        ImageButton imageButton = findViewById(R.id.ib_addToFavorites);
        progressBar = findViewById(R.id.progressBar);

        // Fetching from intent
        String postId = getIntent().getStringExtra(getString(R.string.post_id));
        String subredditName = getIntent().getStringExtra(getString(R.string.sub_name));
        String postText = getIntent().getStringExtra(getString(R.string.post_text));
        String postImageUrl = getIntent().getStringExtra(getString(R.string.image_url));
        String videoUrl = getIntent().getStringExtra(getString(R.string.video_url));
        subRedditId = getIntent().getStringExtra(getString(R.string.sub_id));

        if (savedInstanceState != null) {
            subIsFavorite = savedInstanceState.getBoolean(getString(R.string.is_favorite));
        } else {
            subIsFavorite = getIntent().getBooleanExtra(getString(R.string.is_favorite), false);
        }

        // Setting content in views
        postTextView.setText(postText);
        setImage(postImageUrl);
        setVideo(videoUrl);

        // Fetching Comments
        String url = "https://api.reddit.com/r/" + subredditName + "/comments/" + postId + "/.json";
        CommentsTask commentsTask = new CommentsTask();
        commentsTask.execute(url);
        progressBar.setVisibility(View.VISIBLE);

        // Setting Toolbar as ActionBar and its text [Padding is for RTL layouts]
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("   " + subredditName);

        // Displaying back button in Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setting Image for Favorite Button
        setFavButtonImage(imageButton);

        // Setting up Bottom Navigation View
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(this);
    }

    /**
     * Upates Database and Image for Favorite Button.
     *
     * @param view View clicked.
     */
    public void addRemoveFavorite(final View view) {
        super.addRemoveFavorite(view);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return super.onNavigationItemSelected(menuItem);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(getString(R.string.is_favorite), subIsFavorite);
    }


    class CommentsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NetworkUtil.getResponseFromUrl(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);

            // Parsing Response
            RedditParser.parseComments(s);

            // Setting up Adapter
            CommentsAdapter adapter = new CommentsAdapter();
            GridLayoutManager layoutManager = new GridLayoutManager(CommentsActivity.this, 1);
            commentsRecyclerView.setAdapter(adapter);
            commentsRecyclerView.setLayoutManager(layoutManager);
        }
    }

    //////////// Helpers ////////////

    private void setVideo(String videoUrl) {
        if (videoUrl != null) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(videoUrl);
        } else {
            webView.setVisibility(View.GONE);
        }
    }

    private void setImage(String postImageUrl) {
        if (postImageUrl != null) {
            Uri uri = Uri.parse(postImageUrl);
            new Picasso.Builder(this).build().load(uri).into(iv_postImage);
        } else {
            iv_postImage.setVisibility(View.GONE);
        }
    }
}
