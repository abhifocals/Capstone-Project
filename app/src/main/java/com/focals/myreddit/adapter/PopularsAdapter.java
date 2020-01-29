package com.focals.myreddit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.focals.myreddit.R;
import com.focals.myreddit.data.Subreddit;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PopularsAdapter extends RecyclerView.Adapter<PopularsAdapter.MainViewHolder> {

    private Context context;
    private ArrayList<Subreddit> subredditList;
    private final ClickHandler clickHandler;

    public PopularsAdapter(ArrayList<Subreddit> subredditList, ClickHandler clickHandler, boolean showingFavorites) {
        this.subredditList = subredditList;
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_popular, parent, false);

        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {

        String title = subredditList.get(position).getName();
        String bannerUrl = subredditList.get(position).getBannerUrl();
        String desc = subredditList.get(position).getPublicDescription();
        int subscribers = subredditList.get(position).getSubscribers();
        String subId = subredditList.get(position).getId();

        holder.subredditTitle.setText(title);
        holder.subredditDesc.setText(desc);
        holder.numSubscribers.setText(context.getResources().getString(R.string.subscriber_count_label) + subscribers);

        if (!bannerUrl.isEmpty()) {
            new Picasso.Builder(context).build().load(bannerUrl).into(holder.subredditImage);
        }

        // Display +/- depending on Fav Status

        boolean isFav = subredditList.get(position).isFavorite();

        if (isFav) {
            holder.addToFavorites.setImageDrawable(context.getDrawable(android.R.drawable.ic_delete));
        } else {
            holder.addToFavorites.setImageDrawable(context.getDrawable(android.R.drawable.ic_input_add));
        }
    }

    @Override
    public int getItemCount() {

        if (subredditList != null) {
            return subredditList.size();
        } else {
            return 0;
        }
    }

    public void setSubredditList(ArrayList<Subreddit> subredditList) {
        this.subredditList = subredditList;
        notifyDataSetChanged();
    }

    public interface ClickHandler {
        void onClickHandle(int position, View view);
    }


    class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView subredditImage;
        final TextView subredditTitle;
        final TextView subredditDesc;
        final TextView numSubscribers;
        final ImageButton addToFavorites;

        MainViewHolder(@NonNull View itemView) {
            super(itemView);

            subredditImage = itemView.findViewById(R.id.iv_subredditImage);
            subredditTitle = itemView.findViewById(R.id.tv_subredditTitle);
            subredditDesc = itemView.findViewById(R.id.tv_subredditDesc);
            numSubscribers = itemView.findViewById(R.id.tv_subscriberCount);
            addToFavorites = itemView.findViewById(R.id.ib_addToFavorites);

            subredditTitle.setOnClickListener(this);
            subredditDesc.setOnClickListener(this);
            addToFavorites.setOnClickListener(this);
            subredditImage.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            clickHandler.onClickHandle(getAdapterPosition(), v);
        }
    }
}



