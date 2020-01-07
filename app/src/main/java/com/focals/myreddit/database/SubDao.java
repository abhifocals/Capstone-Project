package com.focals.myreddit.database;

import com.focals.myreddit.data.Subreddit;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import static android.icu.text.MessagePattern.ArgType.SELECT;

@Dao
public interface SubDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertSubs(List<Subreddit> subs);

    @Query("SELECT * FROM sub WHERE id = :providedId")
    public Subreddit getSubById(String providedId);

    @Query("SELECT favorite FROM sub WHERE id =:providedId")
    public boolean isFavorite(String providedId);

    @Query("UPDATE sub SET favorite = :isFavorite WHERE id = :providedId")
    public void updateFavorite(String providedId, boolean isFavorite);

    @Query("SELECT * FROM sub WHERE favorite")
    public List<Subreddit> getFavorites();
}
