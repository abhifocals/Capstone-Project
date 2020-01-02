package com.focals.myreddit.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.focals.myreddit.R;
import com.focals.myreddit.adapter.PopularsAdapter;
import com.focals.myreddit.data.RedditParser;
import com.focals.myreddit.data.Subreddit;
import com.focals.myreddit.network.NetworkUtil;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PopularsActivity extends BaseActivity implements PopularsAdapter.ClickHandler {

    RecyclerView mainRecyclerView;
    ArrayList<Subreddit> subredditList;
    Toolbar toolbar;
    GridLayoutManager layoutManager;
    static boolean SHOWING_FAVS;

    public static ArrayList<Subreddit> FAVORITES = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_populars);

        String popularSubreddits = "https://api.reddit.com/subreddits/popular/.json";

        new RedditAsyncTask().execute(popularSubreddits);

        mainRecyclerView = (RecyclerView) findViewById(R.id.rv_populars);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
    }

    /**
     * Gets the current sub. Then:
     * If Sub is Fav (or on Fav Screen, meaning Sub is Fav), remove from Fav list. Else add to Fav List.
     *
     * @param position
     * @param view
     */

    @Override
    public void onClickHandle(int position, View view) {

        int viewId = view.getId();
        Subreddit sub = getCurrentSub(SHOWING_FAVS, position);

        if (viewId == R.id.ib_addToFavorites) {
            if (sub.isFavorite()) {
                setFavoriteState(sub, false);
            } else {
                setFavoriteState(sub, true);
            }
        } else {
            launchPostsActivity(position);
        }
    }


    /**
     * Retreives the current sub from either Fav list or Popular list.
     *
     * @param showingFavScreen
     * @param position
     * @return
     */

    private Subreddit getCurrentSub(boolean showingFavScreen, int position) {
        Subreddit sub;

        if (showingFavScreen) {
            sub = FAVORITES.get(position);
        } else {
            sub = subredditList.get(position);
        }

        return sub;
    }

    /**
     * Adds or Removes sub from Fav list.
     * Updates the adapter.
     *
     * @param sub
     * @param newFavState
     */

    private void setFavoriteState(Subreddit sub, boolean newFavState) {
        sub.setFavorite(newFavState);

        if (newFavState) {
            FAVORITES.add(sub);
        } else {
            FAVORITES.remove(sub);
        }

        popularsAdapter.notifyDataSetChanged();
    }

    private void launchPostsActivity(int position) {
        Intent intent = new Intent(this, PostsActivity.class);
        String name = subredditList.get(position).getName();

        intent.putExtra("SubredditName", name);

        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_favorite) {
            // This is used to determine if Fav list or Sub list should be used in getCurrentSub()
            SHOWING_FAVS = true;

            popularsAdapter.setSubredditList(FAVORITES);

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This handles callback from PostsActivity when Favorites is selected from Menu.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2) {
            SHOWING_FAVS = true;
            popularsAdapter.setSubredditList(FAVORITES);
        }
    }

    class RedditAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            return NetworkUtil.getResponseFromUrl(url[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            subredditList = RedditParser.parseReddit(s);

            popularsAdapter = new PopularsAdapter(subredditList, PopularsActivity.this, SHOWING_FAVS);
            layoutManager = new GridLayoutManager(PopularsActivity.this, 1);

            mainRecyclerView.setAdapter(popularsAdapter);
            mainRecyclerView.setLayoutManager(layoutManager);

        }
    }
}
