package com.focals.myreddit.database;

import android.app.Application;

import com.focals.myreddit.data.Subreddit;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class SubViewModel extends AndroidViewModel {

    LiveData<List<Subreddit>> favoriteSubs;
    LiveData<List<Subreddit>> subs;
    SubDatabase db;
    SubDao dao;

    public SubViewModel(@NonNull Application application) {
        super(application);

        db = SubDatabase.getInstance(application.getApplicationContext());
        dao = db.subDao();

        favoriteSubs = dao.getFavorites();
        subs = dao.getSubs();
    }

    public LiveData<List<Subreddit>> getFavoriteSubs() {
        return favoriteSubs;
    }

    public LiveData<List<Subreddit>> getSubs() {
        return subs;
    }
}