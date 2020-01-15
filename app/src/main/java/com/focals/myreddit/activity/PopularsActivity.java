package com.focals.myreddit.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.focals.myreddit.R;
import com.focals.myreddit.adapter.PopularsAdapter;
import com.focals.myreddit.data.RedditParser;
import com.focals.myreddit.data.Subreddit;
import com.focals.myreddit.database.SubDao;
import com.focals.myreddit.database.SubDatabase;
import com.focals.myreddit.database.SubViewModel;
import com.focals.myreddit.network.NetworkUtil;
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

    private RecyclerView mainRecyclerView;
    private static ArrayList<Subreddit> subredditList;
    private Toolbar toolbar;
    private GridLayoutManager layoutManager;
    private static boolean SHOWING_FAVS;
    private static SubDao dao;
    public static ArrayList<Subreddit> FAVORITES = new ArrayList<>();
    private SubViewModel subViewModel;
    private static TextView tv_noInternet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_populars);

        // Initializing Views
        mainRecyclerView = findViewById(R.id.rv_populars);
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
        tv_noInternet = findViewById(R.id.tv_noInternet);

        // Initializing Database, DAO, and ViewModel
        SubDatabase db = SubDatabase.getInstance(this);
        dao = db.subDao();
        subViewModel = ViewModelProviders.of(this).get(SubViewModel.class);

        // Setting Toolbar as the ActionBar
        setSupportActionBar(toolbar);

        // Setting up BottomNavigationView
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(this);

        // Fetching Popular Subreddits
        String popularSubreddits = "https://api.reddit.com/subreddits/popular/.json";
        new RedditAsyncTask().execute(popularSubreddits);
        progressBar.setVisibility(View.VISIBLE);

        // Setting up Adapter
        setupAdapter();

        // Observing Favorite List
        subViewModel.getFavoriteSubs().observe(this, new Observer<List<Subreddit>>() {
            @Override
            public void onChanged(List<Subreddit> subreddits) {
                Log.d("Test", "Favorites: " + subreddits.size());
                FAVORITES = (ArrayList) subreddits;
                updateAdapter();

            }
        });

        // Observing Popular List
        subViewModel.getLiveSubs().observe(this, new Observer<List<Subreddit>>() {
            @Override
            public void onChanged(List<Subreddit> subreddits) {
                if (!subreddits.isEmpty()) {
                    subredditList = (ArrayList) subreddits;
                    updateAdapter();
                }
            }
        });
    }

    /**
     * Gets the current sub (based on what screen user is on), and set its fav state.
     *
     * @param position
     * @param view
     */

    @Override
    public void onClickHandle(int position, View view) {
        Subreddit sub = getCurrentSub(SHOWING_FAVS, position);

        if (view.getId() == R.id.ib_addToFavorites) {
            updateSubFavStateInDatabase(sub);
        } else {
            launchPostsActivity(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2) {
            SHOWING_FAVS = true;
        }
    }

    /**
     * Bottom Navigation View click handler.
     *
     * @param menuItem
     * @return
     */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        // Handle Favorite Click
        if (menuItem.getItemId() == R.id.bottom_favorite) {
            SHOWING_FAVS = true;
            updateAdapter();
        }

        // Handle Popular Click
        if (menuItem.getItemId() == R.id.bottom_popular) {
            SHOWING_FAVS = false;
            updateAdapter();
        }

        return false;
    }

    static class RedditAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            return NetworkUtil.getResponseFromUrl(url[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);

            // Handle No Internet/Response
            if (s == null) {
                tv_noInternet.setVisibility(View.VISIBLE);
            } else {
                // Parse Response and insert into Database
                subredditList = RedditParser.parseReddit(s);
                insertToDatabase();
            }
        }
    }


    //////////// Helpers ////////////

    private void setupAdapter() {
        layoutManager = new GridLayoutManager(PopularsActivity.this, 1);
        popularsAdapter = new PopularsAdapter(subredditList, PopularsActivity.this, SHOWING_FAVS);
        mainRecyclerView.setAdapter(popularsAdapter);
        mainRecyclerView.setLayoutManager(layoutManager);
    }


    private Subreddit getCurrentSub(boolean showingFavScreen, final int position) {
        Subreddit sub;

        if (showingFavScreen) {
            sub = FAVORITES.get(position);
        } else {
            sub = subredditList.get(position);
        }
        return sub;
    }

    private static void updateSubFavStateInDatabase(final Subreddit sub) {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                if (dao.isFavorite(sub.getId())) {
                    dao.updateFavorite(sub.getId(), false);
                } else {
                    dao.updateFavorite(sub.getId(), true);
                }
                return null;
            }
        }.execute();
    }

    private void launchPostsActivity(int position) {
        Intent intent = new Intent(this, PostsActivity.class);
        String name = subredditList.get(position).getName();

        intent.putExtra("SubredditName", name);
        intent.putExtra("SubId", subredditList.get(position).getId());
        intent.putExtra("IsFavorite", subredditList.get(position).isFavorite());

        // Start Activity w/Transition
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivityForResult(intent, 1, bundle);
    }

    private void showNoFavoritesMessageIfRequired() {
        if (FAVORITES.isEmpty()) {
            tv_noInternet.setText("Your Favorites List is empty! Add some favorites.");
            tv_noInternet.setVisibility(View.VISIBLE);
        }
    }

    private void hideErrorView() {
        tv_noInternet.setVisibility(View.INVISIBLE);
    }

    private void updateAdapter() {

        if (SHOWING_FAVS) {
            showNoFavoritesMessageIfRequired();
            popularsAdapter.setSubredditList(FAVORITES);
        } else {
            hideErrorView();
            popularsAdapter.setSubredditList(subredditList);
        }
    }

    private static void insertToDatabase() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                dao.insertSubs(subredditList);
                return null;
            }
        }.execute();
    }
}
