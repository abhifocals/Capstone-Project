package com.focals.myreddit.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.focals.myreddit.R;
import com.focals.myreddit.data.Post;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.SubredditViewHolder> implements ExoPlayer.EventListener {

    ArrayList<Post> postsList;
    Context context;
    ClickHandler clickHandler;
    ExoPlayer exoPlayer;
    MediaSource mediaSource;

    public PostsAdapter(ArrayList<Post> posts, ClickHandler clickHandler) {
        this.postsList = posts;
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public SubredditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = null;
        view = inflater.inflate(R.layout.single_post, parent, false);

        return new SubredditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubredditViewHolder holder, int position) {

        String postText = postsList.get(position).getTitle();
        int numComments = postsList.get(position).getNumComments();

        holder.postsTextView.setText(postText);
        holder.commentsCount.setText("Comments: " + String.valueOf(numComments));


        if (postsList.get(position).getVideoUrl() != null) {

            String url = "https://www.redditmedia.com/mediaembed/ekk069";

            Uri uri = Uri.parse(url);

            holder.webView.getSettings().setJavaScriptEnabled(true);
            holder.webView.loadUrl(url);

            holder.exoPlayerView.setVisibility(View.GONE);

//            initializePlayer(holder);
//            prepareMediaSource(uri, holder);
        }


//        if (postsList.get(position).getImageUrl() != null) {
//            Uri uri = Uri.parse(postsList.get(position).getImageUrl());
//            new Picasso.Builder(context).build().load(uri).into(holder.iv_postImage);
//        } else {
//            holder.iv_postImage.setVisibility(View.GONE);
//        }
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }


    private void initializePlayer(SubredditViewHolder holder) {

        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector(), new DefaultLoadControl());

        exoPlayer.addListener(this);
        holder.exoPlayerView.setPlayer(exoPlayer);
    }


    private void prepareMediaSource(Uri uri, SubredditViewHolder holder) {

        mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(context, context.getResources().getString(R.string.app_name)),
                new DefaultExtractorsFactory(), null, null);

        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);
    }

    public interface ClickHandler {
        public void onClick(int position, View view);
    }

    class SubredditViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView postsTextView;
        TextView commentsCount;
        //        ImageView iv_postImage;
        PlayerView exoPlayerView;
        WebView webView;


        public SubredditViewHolder(@NonNull View itemView) {
            super(itemView);

            postsTextView = (TextView) itemView.findViewById(R.id.tv_postText);
            commentsCount = (TextView) itemView.findViewById(R.id.tv_commentsCount);
//            iv_postImage = (ImageView) itemView.findViewById(R.id.iv_postImage);
            exoPlayerView = (PlayerView) itemView.findViewById(R.id.exoPlayerView);
            webView = (WebView) itemView.findViewById(R.id.webView);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickHandler.onClick(getAdapterPosition(), v);
        }
    }


}
