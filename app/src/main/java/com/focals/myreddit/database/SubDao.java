package com.focals.myreddit.database;

import com.focals.myreddit.data.Subreddit;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import static android.icu.text.MessagePattern.ArgType.SELECT;

@Dao
public interface SubDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertSubs(List<Subreddit> subs);

    @Query("SELECT * FROM sub where id = :id")
    public Subreddit getSubById(String id);

}
