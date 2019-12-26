package com.focals.myreddit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.focals.myreddit.R;
import com.focals.myreddit.data.Post;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.SubredditViewHolder> {

    ArrayList<Post> postsList;
    Context context;
    ClickHandler clickHandler;

    public PostsAdapter(ArrayList<Post> posts, ClickHandler clickHandler) {
        this.postsList = posts;
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public SubredditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.single_post, parent, false);

        return new SubredditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubredditViewHolder holder, int position) {

        String postText = postsList.get(position).getTitle();
        int numComments = postsList.get(position).getNumComments();

        holder.postsTextView.setText(postText);
        holder.commentsCount.setText(String.valueOf(numComments));
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public interface ClickHandler {
        public void onClick (int position, View view);
    }

    class SubredditViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView postsTextView;
        TextView commentsCount;


        public SubredditViewHolder(@NonNull View itemView) {
            super(itemView);

            postsTextView = (TextView) itemView.findViewById(R.id.tv_postText);
            commentsCount = (TextView) itemView.findViewById(R.id.tv_commentsCount);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickHandler.onClick(getAdapterPosition(), v);
        }
    }


}
