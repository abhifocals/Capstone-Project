package com.focals.myreddit.database;

import com.focals.myreddit.data.Subreddit;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

@Dao
public interface SubDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void Subs(List<Subreddit> subs);

}
