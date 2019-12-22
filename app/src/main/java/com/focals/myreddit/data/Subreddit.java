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
}
