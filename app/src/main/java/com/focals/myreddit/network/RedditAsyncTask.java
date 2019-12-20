package com.focals.myreddit.network;

import android.os.AsyncTask;

public class RedditAsyncTask extends AsyncTask <Void, Void, String> {
    @Override
    protected String doInBackground(Void... voids) {

        return NetworkUtil.getResponseFromUrl();

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
