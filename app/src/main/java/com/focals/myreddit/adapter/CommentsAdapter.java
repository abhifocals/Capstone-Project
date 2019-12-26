package com.focals.myreddit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        holder.commentsTextView.setText(commentsList.get(position).getCommentBody());

    }


    @Override
    public int getItemCount() {
        return commentsList.size();
    }


    class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentsTextView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            commentsTextView = (TextView) itemView.findViewById(R.id.tv_comment);
        }
    }

}
