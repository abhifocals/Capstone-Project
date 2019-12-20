package com.focals.myreddit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new RedditAsyncTask().execute();

    }





    class RedditAsyncTask extends AsyncTask<Void, Void, String> {
        HttpURLConnection httpURLConnection = null;

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://www.reddit.com/r/php/search.json?q=oop");


                httpURLConnection = (HttpURLConnection) url.openConnection();


                Scanner scanner = new Scanner(httpURLConnection.getInputStream());

                if (scanner.hasNext()) {
                    return scanner.next();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            finally {
                httpURLConnection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

}
