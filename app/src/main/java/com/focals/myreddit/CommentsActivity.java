package com.focals.myreddit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import com.focals.myreddit.data.Comment;
import com.focals.myreddit.data.RedditParser;
import com.focals.myreddit.network.NetworkUtil;

import java.util.ArrayList;

public class CommentsActivity extends AppCompatActivity {

    RecyclerView commentsRecyclerView;
    ArrayList<Comment> commentsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.comments);

        commentsRecyclerView = (RecyclerView) findViewById(R.id.rv_comments);

        String postId = getIntent().getStringExtra("PostId");
        String subredditName = getIntent().getStringExtra("SubredditName");

        String url = "https://api.reddit.com/r/" + subredditName + "/comments/" + postId + "/.json";


        CommentsTask commentsTask = new CommentsTask();
        commentsTask.execute(url);
    }






    class CommentsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NetworkUtil.getResponseFromUrl(strings[0]);
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            commentsList = RedditParser.parseComments(s);




        }
    }


}
