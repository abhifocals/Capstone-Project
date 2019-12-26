package com.focals.myreddit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.focals.myreddit.data.Subreddit;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    Context context;
    ArrayList<Subreddit> subredditList;
    ClickHandler clickHandler;

    public MainAdapter(ArrayList<Subreddit> subredditList, ClickHandler clickHandler) {
        this.subredditList = subredditList;
        this.clickHandler = clickHandler;
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
        holder.numSubscribers.setText(String.valueOf(subscribers));

        if (!bannerUrl.isEmpty()) {
            new Picasso.Builder(context).build().load(bannerUrl).into(holder.subredditImage);
        }

    }

    @Override
    public int getItemCount() {
        return subredditList.size();
    }

    public interface ClickHandler {
        void onClickHandle(int position, View view);
    }


    class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView subredditImage;
        TextView subredditTitle;
        TextView subredditDesc;
        TextView numSubscribers;
        ImageButton addToFavorites;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            subredditImage = (ImageView) itemView.findViewById(R.id.iv_reddit);
            subredditTitle = (TextView) itemView.findViewById(R.id.tv_redditTitle);
            subredditDesc = (TextView) itemView.findViewById(R.id.tv_redditDesc);
            numSubscribers = (TextView) itemView.findViewById(R.id.tv_subscriberCount);
            addToFavorites = (ImageButton) itemView.findViewById(R.id.imgbtn_addToFavorite);

            subredditTitle.setOnClickListener(this);
            subredditDesc.setOnClickListener(this);
            addToFavorites.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickHandler.onClickHandle(getAdapterPosition(), v);
        }
    }

}



