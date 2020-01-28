package com.focals.myreddit.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.focals.myreddit.R;
import com.focals.myreddit.adapter.PopularsAdapter;
import com.focals.myreddit.database.SubDao;
import com.focals.myreddit.database.SubDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {


    PopularsAdapter popularsAdapter;
    BottomNavigationView bottomNav;
    static ProgressBar progressBar;

    boolean subIsFavorite;
    private SubDatabase db;
    private SubDao dao;
    String subRedditId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = SubDatabase.getInstance(this);
        dao = db.subDao();
    }

    /**
     * If already a Favorite, mark it as non-Favorite.
     * If not a Favorite, mark is a Favorite.
     *
     * @param view
     */

    public void addRemoveFavorite(final View view) {

        new AsyncTask() {

            boolean isFav;

            @Override
            protected Object doInBackground(Object[] objects) {

                isFav = dao.isFavorite(subRedditId);

                if (isFav) {
                    dao.updateFavorite(subRedditId, false);
                } else {
                    dao.updateFavorite(subRedditId, true);
                }

                // Update subIsFav to new state
                subIsFavorite = !isFav;

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                if (isFav) {
                    ((ImageView) view).setImageDrawable(getDrawable(android.R.drawable.ic_input_add));
                } else {
                    ((ImageView) view).setImageDrawable(getDrawable(android.R.drawable.ic_delete));
                }
            }
        }.execute();
    }

    protected void setFavButtonImage(ImageButton imageButton) {
        imageButton.setVisibility(View.VISIBLE);
        if (subIsFavorite) {
            imageButton.setImageDrawable(getDrawable(android.R.drawable.ic_delete));
        } else {
            imageButton.setImageDrawable(getDrawable(android.R.drawable.ic_input_add));
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        // Set a result code and send back to Populars Activity to Load Favorites
        if (menuItem.getItemId() == R.id.bottom_favorite) {
            setResult(2);
            finish();
        }

        if (menuItem.getItemId() == R.id.bottom_news) {
            setResult(3);
            finish();
        }

        // Send back to Populars Activity to Load Populars
        if (menuItem.getItemId() == R.id.bottom_popular) {
            setResult(4);
            finish();
        }

        return true;
    }
}

