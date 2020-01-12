package com.focals.myreddit.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.focals.myreddit.network.SubExecutors;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentsActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    RecyclerView commentsRecyclerView;
    ArrayList<Comment> commentsList;

    TextView subredditNameTextView;
    TextView postTextView;
    Toolbar toolbar;
    ImageButton imageButton;
    ImageView iv_postImage;
    WebView webView;
    private SubDatabase db;
    private SubDao dao;
    Subreddit sub;
    String subRedditId;
    boolean subIsFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rv_comments);

        commentsRecyclerView = (RecyclerView) findViewById(R.id.rv_comments);
        postTextView = (TextView) findViewById(R.id.tv_postText);
        iv_postImage = (ImageView) findViewById(R.id.iv_postImage);
        webView = (WebView) findViewById(R.id.webView);


        String postId = getIntent().getStringExtra("PostId");
        String subredditName = getIntent().getStringExtra("SubredditName");
        String postText = getIntent().getStringExtra("PostText");
        String postImageUrl = getIntent().getStringExtra("ImageUrl");
        String videoUrl = getIntent().getStringExtra("VideoUrl");

        postTextView.setText(postText);

        if (postImageUrl != null) {
            Uri uri = Uri.parse(postImageUrl);
            new Picasso.Builder(this).build().load(uri).into(iv_postImage);
        } else {
            iv_postImage.setVisibility(View.GONE);
        }

        if (videoUrl != null) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(videoUrl);
        } else {
            webView.setVisibility(View.GONE);
        }


        String url = "https://api.reddit.com/r/" + subredditName + "/comments/" + postId + "/.json";

        CommentsTask commentsTask = new CommentsTask();
        commentsTask.execute(url);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        // The padding is to support RTL Layouts.
        getSupportActionBar().setTitle("   " + subredditName);

        imageButton = (ImageButton) findViewById(R.id.ib_addToFavorites);
        imageButton.setVisibility(View.VISIBLE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = SubDatabase.getInstance(this);
        dao = db.subDao();

        subRedditId = getIntent().getStringExtra("SubId");
        subIsFavorite = getIntent().getBooleanExtra("IsFavorite", false);

        if (subIsFavorite) {
            imageButton.setImageDrawable(getDrawable(android.R.drawable.ic_delete));
        } else {
            imageButton.setImageDrawable(getDrawable(android.R.drawable.ic_input_add));
        }

        bottomNav = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void addRemoveFavorite(final View view) {

        SubExecutors.getInstance().getNetworkIO().execute(new Runnable() {
            @Override
            public void run() {

                subIsFavorite = dao.isFavorite(subRedditId);

                if (subIsFavorite) {
                    dao.updateFavorite(subRedditId, false);
                } else {
                    dao.updateFavorite(subRedditId, true);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (subIsFavorite) {
                            ((ImageView) view).setImageDrawable(getDrawable(android.R.drawable.ic_input_add));
                        } else {
                            ((ImageView) view).setImageDrawable(getDrawable(android.R.drawable.ic_delete));
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.bottom_favorite) {
            setResult(2);
            finish();
        }

        if (menuItem.getItemId() == R.id.bottom_popular) {
            finish();
        }


        return false;
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

            commentsList = RedditParser.parseComments(s);

            CommentsAdapter adapter = new CommentsAdapter(commentsList);
            GridLayoutManager layoutManager = new GridLayoutManager(CommentsActivity.this, 1);

            commentsRecyclerView.setAdapter(adapter);
            commentsRecyclerView.setLayoutManager(layoutManager);
        }
    }


}
