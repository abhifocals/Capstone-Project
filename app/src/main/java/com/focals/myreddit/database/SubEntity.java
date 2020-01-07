package com.focals.myreddit.database;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sub")
public class SubEntity {

    String name;
    String bannerUrl;
    String publicDescription;
    int subscribers;
    boolean favorite;

    @PrimaryKey
    int subId;

    public SubEntity(String name, String bannerUrl, String publicDescription, int subscribers, boolean favorite, int subId) {
        this.name = name;
        this.bannerUrl = bannerUrl;
        this.publicDescription = publicDescription;
        this.subscribers = subscribers;
        this.favorite = favorite;
        this.subId = subId;
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

    public int getSubId() {
        return subId;
    }
}
