package com.focals.myreddit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    Context context;


    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_subreddit, parent);

        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {




    }

    @Override
    public int getItemCount() {
        return 0;
    }


    class MainViewHolder extends RecyclerView.ViewHolder {

        ImageView subredditImage;
        TextView subredditTitle;
        TextView subredditDesc;
        TextView commentsCount;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            subredditImage = (ImageView) itemView.findViewById(R.id.iv_reddit);
            subredditTitle = (TextView) itemView.findViewById(R.id.tv_redditTitle);
            subredditDesc = (TextView) itemView.findViewById(R.id.tv_redditDesc);
            commentsCount = (TextView) itemView.findViewById(R.id.tv_commentsCount);
        }
    }

}



