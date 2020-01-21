package com.focals.myreddit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.focals.myreddit.R;
import com.focals.myreddit.data.Comment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    Context context;
    ArrayList<Comment> commentsList;


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
        holder.commentsWebView.loadData(commentsList.get(position).getCommentBody(), "text/html", "UTF-8");
    }


    @Override
    public int getItemCount() {
        return commentsList.size();
    }


    class CommentViewHolder extends RecyclerView.ViewHolder {
        WebView commentsWebView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            commentsWebView = (WebView) itemView.findViewById(R.id.tv_comment);
        }
    }

}
