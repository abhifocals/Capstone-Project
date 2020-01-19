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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

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
    private static TextView tv_error;
    private FirebaseAnalytics firebaseAnalytics;
    private AdView adView;
    private static final String TEST_BANNER_UNIT_ID = "ca-app-pub-9671217180587470~6356005035";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_populars);

        // Initializing Views
        mainRecyclerView = findViewById(R.id.rv_populars);
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
        tv_error = findViewById(R.id.tv_error);
        adView = findViewById(R.id.adView);

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

        // Show Progress Bar
        showProgressBar();

        // Setting up Adapter
        setupAdapter();

        // Observing Favorite List
        observeFavorites();

        // Observing Popular List
        observePopulars();

        // Initializing Firebase Analytics
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Displaying Ad
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // Initializing Ads SDK:
        MobileAds.initialize(this, TEST_BANNER_UNIT_ID);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * If Fav button is clicked, gets the current sub (based on what screen user is on), and updates database.
     * Else Starts Posts Activity.
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
        Log.d("Test", "Back Populars.");

        if (resultCode == 2) {
            SHOWING_FAVS = true;
        } else {
            SHOWING_FAVS = false;
        }

        // This is to show no fav screen if no change is made in Posts/Comments,
        // resulting in  no ViewModel update.
        updateAdapter();
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

            // Handle No Internet/Response
            if (s == null) {
                tv_error.setVisibility(View.VISIBLE);
            } else {
                // Parse Response and insert into Database
                ArrayList<Subreddit> subs = RedditParser.parseReddit(s);
                insertToDatabase(subs);
            }
        }
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        mainRecyclerView.setVisibility(View.VISIBLE);
    }


    //////////// Helpers ////////////

    private void observeFavorites() {
        subViewModel.getFavoriteSubs().observe(this, new Observer<List<Subreddit>>() {
            @Override
            public void onChanged(List<Subreddit> subreddits) {
                Log.d("Test", "Observing Favs: " + subreddits.size());
                FAVORITES = (ArrayList) subreddits;

                // Empty Fav message displayed via onNavigationItemSelected and onActivityResult
                if (subreddits == null || !subreddits.isEmpty()) {
                    updateAdapter();
                }
            }
        });
    }

    private void observePopulars() {
        subViewModel.getLiveSubs().observe(this, new Observer<List<Subreddit>>() {
            @Override
            public void onChanged(List<Subreddit> subreddits) {
                if (!subreddits.isEmpty()) {
                    Log.d("Test", "Observing Popular: " + subreddits.size());
                    subredditList = (ArrayList) subreddits;
                    updateAdapter();
                }
            }
        });
    }

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

        intent.putExtra(getString(R.string.sub_name), name);
        intent.putExtra(getString(R.string.sub_id), subredditList.get(position).getId());
        intent.putExtra(getString(R.string.is_favorite), subredditList.get(position).isFavorite());

        // Start Activity w/Transition
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivityForResult(intent, 1, bundle);

        // Log event to Firebase Analytics
        Bundle eventBundle = new Bundle();
        eventBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void showNoFavoritesMessageIfRequired() {
        if (FAVORITES.isEmpty()) {
            tv_error.setVisibility(View.VISIBLE);
            tv_error.setText(getString(R.string.empty_fav_list_message));
        }
    }

    private void hideErrorView() {
        tv_error.setVisibility(View.INVISIBLE);
    }

    private void updateAdapter() {
        if (SHOWING_FAVS) {
            showNoFavoritesMessageIfRequired();
            Log.d("Test", "Updating Adapter w/Fav. Count: " + FAVORITES.size());
            popularsAdapter.setSubredditList(FAVORITES);
        } else {
            hideErrorView();
            popularsAdapter.setSubredditList(subredditList);
        }

        hideProgressBar();
    }

    private static void insertToDatabase(final ArrayList<Subreddit> subs) {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                dao.insertSubs(subs);
                return null;
            }
        }.execute();
    }
}
