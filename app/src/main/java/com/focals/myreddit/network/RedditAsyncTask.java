package com.focals.myreddit.network;

import android.os.AsyncTask;

import com.focals.myreddit.data.RedditParser;

public class RedditAsyncTask extends AsyncTask <Void, Void, String> {
    @Override
    protected String doInBackground(Void... voids) {
        return NetworkUtil.getResponseFromUrl(NetworkUtil.searchUrl);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        RedditParser.parseRedditPosts(s);

    }
}
