package com.focals.myreddit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import com.focals.myreddit.network.RedditAsyncTask;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    RecyclerView mainRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new RedditAsyncTask().execute();

        mainRecyclerView = (RecyclerView) findViewById(R.id.rv_main);

        MainAdapter mainAdapter = new MainAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);

        mainRecyclerView.setAdapter(mainAdapter);
        mainRecyclerView.setLayoutManager(layoutManager);
    }
}
