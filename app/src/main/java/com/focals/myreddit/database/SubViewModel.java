package com.focals.myreddit.database;

import android.app.Application;

import com.focals.myreddit.data.Subreddit;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class SubViewModel extends AndroidViewModel {

    private final LiveData<List<Subreddit>> favoriteSubs;
    private final LiveData<List<Subreddit>> subs;
    List<Subreddit> favorites;

    public SubViewModel(@NonNull Application application) {
        super(application);

        SubDatabase db = SubDatabase.getInstance(application.getApplicationContext());
        SubDao dao = db.subDao();

        favoriteSubs = dao.getFavorites();
        subs = dao.getLiveSubs();
    }

    public LiveData<List<Subreddit>> getFavoriteSubs() {
        return favoriteSubs;
    }

    public LiveData<List<Subreddit>> getLiveSubs() {
        return subs;
    }


//    public LiveData<List<Subreddit>> getSubs() {
//        return subs;
//    }
//
//    public List<Subreddit> getFavorites() {
//        return favorites;
//    }
//
//    public void updateFavorite(String subId, boolean status) {
//        dao.updateFavorite(subId, status);
//    }
}