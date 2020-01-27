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
    static StringBuilder stringBuilder;


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

        stringBuilder = new StringBuilder();

        // Get 1st item from static listOfCommentsMap
        commentMap = RedditParser.listOfCommentsMap.get(0);


        // Get parent comment
        Comment topComment  = commentMap.get(RedditParser.TOP).get(0);
        String parentCommentText = topComment.getText();
        String parentId = topComment.getId();
        stringBuilder.append(parentCommentText + "\n");

        // Get children of parent
        ArrayList<Comment> childrenList = commentMap.get("t1_" + parentId);

        for (int i=0; i < childrenList.size(); i++) {
            stringBuilder.append("    " + childrenList.get(i).getText() + "\n");
            getChildren(childrenList.get(i));
            stringBuilder.append("\n");
        }
        

        Log.d(logTag, stringBuilder.toString());

        System.out.println();

        holder.commentsTextView.setText(stringBuilder.toString());

    }

    private static void getChildren(Comment comment) {
        if (!childrenExist(comment)) {
            return;

        } else {
            getAllChildren(comment);

            for (int i = 0; i < commentMap.get("t1_" + comment.getId()).size(); i++) {
                getChildren(commentMap.get("t1_" + comment.getId()).get(i));
            }
        }
    }

    private static void getAllChildren(Comment comment) {
        ArrayList<Comment> children = commentMap.get("t1_"  + comment.getId());


        for (int i = 0; i < children.size(); i++) {
            stringBuilder.append("        " + children.get(i).getText() + "\n");
        }
    }


    private static boolean childrenExist(Comment comment) {
        return commentMap.get("t1_" + comment.getId()) != null;
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
