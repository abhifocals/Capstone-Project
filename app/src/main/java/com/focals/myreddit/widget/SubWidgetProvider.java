package com.focals.myreddit.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.focals.myreddit.R;
import com.focals.myreddit.database.SubDao;
import com.focals.myreddit.database.SubDatabase;

/**
 * Implementation of App Widget functionality.
 */
public class SubWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = "SubWidgetProvider";
        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.sub_widget);
//        views.setTextViewText(R.id.appwidget_text, widgetText);


        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.sub_widget);
        Intent intent = new Intent(context,  SubWidgetService.class);
        rv.setRemoteAdapter(R.id.lv_widget, intent);














        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

