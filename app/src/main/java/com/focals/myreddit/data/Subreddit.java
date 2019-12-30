package com.focals.myreddit.data;

public class Subreddit {

    String name;
    String bannerUrl;
    String publicDescription;
    int subscribers;
    boolean favorite;

    public Subreddit(String name, String bannerUrl, String publicDescription, int subscribers, boolean favorite) {
        this.name = name;
        this.bannerUrl = bannerUrl;
        this.publicDescription = publicDescription;
        this.subscribers = subscribers;
        this.favorite = favorite;
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
}
