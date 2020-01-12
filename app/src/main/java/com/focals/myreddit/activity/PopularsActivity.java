package com.focals.myreddit.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.focals.myreddit.R;
import com.focals.myreddit.adapter.PopularsAdapter;
import com.focals.myreddit.data.RedditParser;
import com.focals.myreddit.data.Subreddit;
import com.focals.myreddit.database.SubDao;
import com.focals.myreddit.database.SubDatabase;
import com.focals.myreddit.database.SubViewModel;
import com.focals.myreddit.network.NetworkUtil;
import com.focals.myreddit.network.SubExecutors;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PopularsActivity extends BaseActivity implements PopularsAdapter.ClickHandler, BottomNavigationView.OnNavigationItemSelectedListener {

    RecyclerView mainRecyclerView;
    ArrayList<Subreddit> subredditList;
    Toolbar toolbar;
    GridLayoutManager layoutManager;
    static boolean SHOWING_FAVS;
    private SubDatabase db;
    private SubDao dao;
    private Subreddit currentSub;
    public static ArrayList<Subreddit> FAVORITES = new ArrayList<>();
    SubViewModel subViewModel;
    private String subViewing;
    TextView tv_noInternet;
    BottomNavigationItemView bottom_favorite;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_populars);

        String popularSubreddits = "https://api.reddit.com/subreddits/popular/.json";

        new RedditAsyncTask().execute(popularSubreddits);

        mainRecyclerView = (RecyclerView) findViewById(R.id.rv_populars);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        tv_noInternet = (TextView) findViewById(R.id.tv_noInternet);

        setSupportActionBar(toolbar);

        db = SubDatabase.getInstance(this);
        dao = db.subDao();


        bottomNav = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(this);


        subViewModel = ViewModelProviders.of(this).get(SubViewModel.class);

        subViewModel.getFavoriteSubs().observe(this, new Observer<List<Subreddit>>() {
            @Override
            public void onChanged(List<Subreddit> subreddits) {
                Log.d("Test", "Favorites: " + subreddits.size());
                FAVORITES = (ArrayList) subreddits;

                if (SHOWING_FAVS) {
                    popularsAdapter.setSubredditList(FAVORITES);
                }
            }
        });

        progressBar.setVisibility(View.VISIBLE);
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

    private Subreddit getCurrentSub(boolean showingFavScreen, final int position) {
        Subreddit sub = null;

        if (showingFavScreen) {
            sub = FAVORITES.get(position);
        } else {
            sub = subredditList.get(position);
        }

        return sub;
    }

    /**
     * Add/Removes sub from Fav List. Sets sub's Fav State. Notifies the adapter.
     *
     * @param sub
     */

    private void setFavoriteState(final Subreddit sub) {

        SubExecutors.getInstance().getNetworkIO().execute(new Runnable() {
            @Override
            public void run() {

                if (dao.isFavorite(sub.getId())) {
                    dao.updateFavorite(sub.getId(), false);
                } else {
                    dao.updateFavorite(sub.getId(), true);
                }

                final ArrayList<Subreddit> subreddits = (ArrayList) dao.getSubs();

                subredditList = subreddits;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (SHOWING_FAVS) {
                            popularsAdapter.setSubredditList(FAVORITES);
                        } else {
                            popularsAdapter.setSubredditList(subreddits);
                        }

                    }
                });
            }
        });
    }


    private void launchPostsActivity(int position) {
        Intent intent = new Intent(this, PostsActivity.class);

        String name = subredditList.get(position).getName();

        intent.putExtra("SubredditName", name);
        intent.putExtra("SubId", subredditList.get(position).getId());
        intent.putExtra("IsFavorite", subredditList.get(position).isFavorite());
        subViewing = subredditList.get(position).getId();

        // Start Activity w/Transition
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivityForResult(intent, 1, bundle);
    }

    private void showNoFavoritesMessage() {
        if (FAVORITES.isEmpty()) {
            tv_noInternet.setText("Your Favorites List is empty! Add some favorites.");
            tv_noInternet.setVisibility(View.VISIBLE);
        }
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

            showNoFavoritesMessage();


            SHOWING_FAVS = true;
            popularsAdapter.setSubredditList(FAVORITES);
            return;
        }


        // Get updated list here

        SubExecutors.getInstance().getNetworkIO().execute(new Runnable() {
            @Override
            public void run() {
                final ArrayList<Subreddit> subreddits = (ArrayList) dao.getSubs();
                subredditList = subreddits;
            }
        });

        popularsAdapter.setSubredditList(subredditList);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.bottom_favorite) {
            showNoFavoritesMessage();


            // This is used to determine if Fav list or Sub list should be used in getCurrentSub()
            SHOWING_FAVS = true;

            popularsAdapter.setSubredditList(FAVORITES);
        }

        if (menuItem.getItemId() == R.id.bottom_popular) {
            popularsAdapter.setSubredditList(subredditList);
            tv_noInternet.setVisibility(View.INVISIBLE);
            SHOWING_FAVS = false;
        }

        return false;
    }

    class RedditAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            return NetworkUtil.getResponseFromUrl(url[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressBar.setVisibility(View.INVISIBLE);

            if (s == null) {
                tv_noInternet.setVisibility(View.VISIBLE);
                return;
            }

            subredditList = RedditParser.parseReddit(s);

            // Insert subs in Database
            insertToDatabase();

            popularsAdapter = new PopularsAdapter(subredditList, PopularsActivity.this, SHOWING_FAVS);
            layoutManager = new GridLayoutManager(PopularsActivity.this, 1);

            mainRecyclerView.setAdapter(popularsAdapter);
            mainRecyclerView.setLayoutManager(layoutManager);

            SHOWING_FAVS = false;
        }
    }

    private void insertToDatabase() {

        SubExecutors.getInstance().getNetworkIO().execute(new Runnable() {
            @Override
            public void run() {
                dao.insertSubs(subredditList);
            }
        });
    }
}
