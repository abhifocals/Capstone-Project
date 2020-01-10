package com.focals.myreddit.database;

import android.content.Context;

import com.focals.myreddit.data.Subreddit;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Subreddit.class}, version = 1, exportSchema = false)
public abstract class SubDatabase extends RoomDatabase {

        private static final Object LOCK = new Object();
        private static final String DATABASE_NAME = "subs";
        private static SubDatabase instance;


        public static SubDatabase getInstance(Context context) {
            if (instance == null) {
                synchronized (LOCK) {
                    // TODO remove main thread
                    instance = Room.databaseBuilder(context.getApplicationContext(), SubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
                }

            }
            return instance;
        }

        public abstract SubDao subDao();
}
