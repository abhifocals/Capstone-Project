package com.focals.myreddit.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.focals.myreddit.data.Subreddit;
import com.focals.myreddit.database.SubDao;
import com.focals.myreddit.database.SubDatabase;

import java.util.List;

public class SubWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new SubWidgetFactory(this);
    }
}

class SubWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    Context context;

    SubDatabase db;
    SubDao dao;
    List<Subreddit> favs;

    public SubWidgetFactory(Context context) {
        this.context = context;

        db = SubDatabase.getInstance(context);
        dao = db.subDao();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        favs = dao.getWidgetFavorites();

        Log.d("Test", "Favorite Count for Widget: " + favs.size());
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return favs.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        //TODO






        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
