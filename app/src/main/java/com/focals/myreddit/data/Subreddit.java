package com.focals.myreddit.data;

public class Subreddit {

    String name;
    String bannerUrl;
    String publicDescription;
    int subscribers;

    public Subreddit(String name, String bannerUrl, String publicDescription, int subscribers) {
        this.name = name;
        this.bannerUrl = bannerUrl;
        this.publicDescription = publicDescription;
        this.subscribers = subscribers;
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
