package com.focals.myreddit.activity;

import android.app.ActivityOptions;
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
     * Gets the current sub (based on what screen user is on) and set its fav state.
     *
     * @param position
     * @param view
     */

    @Override
    public void onClickHandle(int position, View view) {

        int viewId = view.getId();
        Subreddit sub = getCurrentSub(SHOWING_FAVS, position);

        if (viewId == R.id.ib_addToFavorites) {
            setFavoriteState(sub);
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
     * Add/Removes sub from Fav List. Sets sub's Fav State. Notifies the adapter.
     * @param sub
     */

    private void setFavoriteState(Subreddit sub) {

        if (sub.isFavorite()) {
            sub.setFavorite(false);
            FAVORITES.remove(sub);
        } else {
            sub.setFavorite(true);
            FAVORITES.add(sub);
        }
        popularsAdapter.notifyDataSetChanged();
    }

    private void launchPostsActivity(int position) {
        Intent intent = new Intent(this, PostsActivity.class);
        String name = subredditList.get(position).getName();

        intent.putExtra("SubredditName", name);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);

        startActivityForResult(intent, 1, options.toBundle());
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
