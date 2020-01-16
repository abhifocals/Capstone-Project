package com.focals.myreddit.activity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.focals.myreddit.R;
import com.focals.myreddit.adapter.CommentsAdapter;
import com.focals.myreddit.data.Comment;
import com.focals.myreddit.data.RedditParser;
import com.focals.myreddit.data.Subreddit;
import com.focals.myreddit.database.SubDao;
import com.focals.myreddit.database.SubDatabase;
import com.focals.myreddit.network.NetworkUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CommentsActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    RecyclerView commentsRecyclerView;
    ArrayList<Comment> commentsList;
    TextView postTextView;
    Toolbar toolbar;
    ImageButton imageButton;
    ImageView iv_postImage;
    WebView webView;
    private SubDatabase db;
    private SubDao dao;
    Subreddit sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_comments);

        // Initializing Views
        commentsRecyclerView = (RecyclerView) findViewById(R.id.rv_comments);
        postTextView = (TextView) findViewById(R.id.tv_postText);
        iv_postImage = (ImageView) findViewById(R.id.iv_postImage);
        webView = (WebView) findViewById(R.id.webView);
        imageButton = (ImageButton) findViewById(R.id.ib_addToFavorites);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Fetching from intent
        String postId = getIntent().getStringExtra("PostId");
        String subredditName = getIntent().getStringExtra("SubredditName");
        String postText = getIntent().getStringExtra("PostText");
        String postImageUrl = getIntent().getStringExtra("ImageUrl");
        String videoUrl = getIntent().getStringExtra("VideoUrl");
        subRedditId = getIntent().getStringExtra("SubId");
        subIsFavorite = getIntent().getBooleanExtra("IsFavorite", false);

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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("   " + subredditName);

        // Displaying back button in Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initializing Database
        db = SubDatabase.getInstance(this);
        dao = db.subDao();

        // Setting Image for Favorite Button
        setFavButtonImage(imageButton);

        // Setting up Bottom Navigation View
        bottomNav = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(this);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return super.onNavigationItemSelected(menuItem);
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
            commentsList = RedditParser.parseComments(s);

            // Setting up Adapter
            CommentsAdapter adapter = new CommentsAdapter(commentsList);
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
