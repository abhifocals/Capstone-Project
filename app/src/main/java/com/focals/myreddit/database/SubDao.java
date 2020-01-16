package com.focals.myreddit.database;

import com.focals.myreddit.data.Subreddit;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SubDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertSubs(List<Subreddit> subs);

    @Query("SELECT favorite FROM sub WHERE id =:providedId")
    public boolean isFavorite(String providedId);

    @Query("UPDATE sub SET favorite = :isFavorite WHERE id = :providedId")
    public void updateFavorite(String providedId, boolean isFavorite);

    @Query("SELECT * FROM sub WHERE favorite = 1")
    public LiveData<List<Subreddit>> getFavorites();

    @Query("SELECT * FROM sub WHERE favorite = 1")
    public List<Subreddit> getWidgetFavorites();

    @Query("SELECT * FROM sub where name = :name")
    public Subreddit getSub(String name);

    @Query("SELECT * FROM sub")
    public List<Subreddit> getSubs();

    @Query("SELECT * FROM sub")
    public LiveData<List<Subreddit>> getLiveSubs();
}
