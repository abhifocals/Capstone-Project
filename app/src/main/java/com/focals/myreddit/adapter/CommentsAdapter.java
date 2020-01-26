package com.focals.myreddit.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.focals.myreddit.R;
import com.focals.myreddit.data.Comment;
import com.focals.myreddit.data.RedditParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    Context context;
    ArrayList<Comment> commentsList;
    static ArrayList<Comment> childrenList;
    static HashMap<String, ArrayList<Comment>> commentMap;
    static String logTag = "Testing";


    public CommentsAdapter(ArrayList<Comment> commentsList) {
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.single_comment, parent, false);

        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {


        // Get 1st item from static listOfCommentsMap
        commentMap = RedditParser.listOfCommentsMap.get(0);


        // Get parent comment
        String parentCommentText = commentMap.get("mainParent").get(0).getText();
        String parentId = commentMap.get("mainParent").get(0).getId();
        Log.d(logTag, parentCommentText);



        // Get children of parent
        ArrayList<Comment> childrenList = commentMap.get(parentId);

        for (int i=0; i < childrenList.size(); i++) {
            Log.d(logTag,  "  " + childrenList.get(i).getText());
            getChildren(childrenList.get(i));
        }






        //  Print Text of each comment.  Add tab multiple of each comment.

        System.out.println();












































        holder.commentsTextView.setText(commentsList.get(position).getCommentBody());

    }

    private static void getChildren(Comment comment) {
        if (!childrenExist(comment)) {
            return;

        } else {
            getAllChildren(comment);

            for (int i = 0; i < commentMap.get(comment.getId()).size(); i++) {
                getChildren(commentMap.get(comment.getId()).get(i));
            }
        }
    }

    private static void getAllChildren(Comment comment) {
        ArrayList<Comment> children = commentMap.get(comment.getId());


        for (int i = 0; i < children.size(); i++) {

            Log.d(logTag,  "    " + children.get(i).getText());

        }
    }


    private static boolean childrenExist(Comment comment) {
        return commentMap.get(comment.getId()) != null;
    }













    @Override
    public int getItemCount() {
        return RedditParser.listOfCommentsMap.size();
    }


    class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentsTextView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            commentsTextView = (TextView) itemView.findViewById(R.id.tv_comment);
        }
    }

}
