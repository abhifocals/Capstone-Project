package com.focals.myreddit.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sub")
public class Subreddit {

    String name;
    String bannerUrl;
    String publicDescription;
    int subscribers;
    boolean favorite;

    @NonNull
    @PrimaryKey
    String id;

    public Subreddit(String name, String bannerUrl, String publicDescription, int subscribers, boolean favorite, String id) {
        this.name = name;
        this.bannerUrl = bannerUrl;
        this.publicDescription = publicDescription;
        this.subscribers = subscribers;
        this.favorite = favorite;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public String getPublicDescription() {
        return publicDescription;
    }

    public int getSubscribers() {
        return subscribers;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getId() {
        return id;
    }
}
