package com.focals.myreddit;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SubredditAdapter extends RecyclerView.Adapter<SubredditAdapter.SubredditViewHolder> {


    @NonNull
    @Override
    public SubredditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SubredditViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class SubredditViewHolder extends RecyclerView.ViewHolder {

        public SubredditViewHolder(@NonNull View itemView) {
            super(itemView);
        }



    }


}
