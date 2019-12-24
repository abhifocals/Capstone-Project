package com.focals.myreddit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.focals.myreddit.data.Subreddit;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    Context context;
    ArrayList<Subreddit> subredditList;

    public MainAdapter(ArrayList<Subreddit> subredditList) {
        this.subredditList = subredditList;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_subreddit, parent, false);

        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {

        String title = subredditList.get(position).getName();
        String bannerUrl = subredditList.get(position).getBannerUrl();
        String desc = subredditList.get(position).getPublicDescription();
        int subscribers = subredditList.get(position).getSubscribers();


        holder.subredditTitle.setText(title);
        holder.subredditDesc.setText(desc);
        holder.commentsCount.setText(String.valueOf(subscribers));

    }

    @Override
    public int getItemCount() {
        return subredditList.size();
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



