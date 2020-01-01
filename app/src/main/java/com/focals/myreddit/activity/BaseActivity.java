package com.focals.myreddit.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.focals.myreddit.R;
import com.focals.myreddit.adapter.PopularsAdapter;

import static com.focals.myreddit.activity.PopularsActivity.FAVORITES;

public class BaseActivity extends AppCompatActivity {

    boolean showingFavorites;
    PopularsAdapter popularsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = new MenuInflater(this);

        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_favorite) {
            showingFavorites = true;

            popularsAdapter.setSubredditList(FAVORITES);

        }


        return super.onOptionsItemSelected(item);
    }
}
