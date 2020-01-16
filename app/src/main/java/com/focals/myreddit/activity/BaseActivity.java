package com.focals.myreddit.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.focals.myreddit.adapter.PopularsAdapter;
import com.focals.myreddit.database.SubDao;
import com.focals.myreddit.database.SubDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

    public void addRemoveFavorite(final View view) {

        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {

                subIsFavorite = dao.isFavorite(subRedditId);

                if (subIsFavorite) {
                    dao.updateFavorite(subRedditId, false);
                } else {
                    dao.updateFavorite(subRedditId, true);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                if (subIsFavorite) {
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
}

