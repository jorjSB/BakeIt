package com.bakeit.balascageorge.bakeit;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory{
    private static final String TAG = "WidgetDataProvider";
    private int appWidgetId;

    List<String> mCollection = new ArrayList<>();
    Context mContext = null;

    public WidgetDataProvider(Context context, Intent intent) {

        appWidgetId = Integer.valueOf(intent.getData().getSchemeSpecificPart());
        mContext = context;
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mCollection.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                android.R.layout.simple_list_item_1);
        view.setTextViewText(android.R.id.text1, mCollection.get(position));
        return view;
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
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void initData() {

        mCollection.clear();

        Set<String> recipeIngredients = WidgetConfigurationActivity.loadRecipeIngredientsPref(mContext, appWidgetId);

        if(recipeIngredients != null)
            for (String ingredient : recipeIngredients)
                mCollection.add(ingredient);

    }
}
