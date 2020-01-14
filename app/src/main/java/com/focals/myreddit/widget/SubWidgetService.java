package com.focals.myreddit.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.focals.myreddit.R;
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

        if (favs.isEmpty()) {
            return 1;
        }

        return favs.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.single_widget_item);

        if (favs.isEmpty()) {
            rv.setTextViewText(R.id.tv_widgetText, "You have no Favorites!");
            return rv;
        }

        String favSubName = favs.get(position).getName();
        rv.setTextViewText(R.id.tv_widgetText, favSubName);
        return rv;
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
