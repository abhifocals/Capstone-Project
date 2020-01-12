package com.focals.myreddit.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.focals.myreddit.R;
import com.focals.myreddit.adapter.PopularsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.focals.myreddit.activity.PopularsActivity.FAVORITES;

public class BaseActivity extends AppCompatActivity {


    PopularsAdapter popularsAdapter;
    BottomNavigationView bottomNav;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
