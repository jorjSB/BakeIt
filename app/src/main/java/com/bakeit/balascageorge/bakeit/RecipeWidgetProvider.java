package com.bakeit.balascageorge.bakeit;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Set;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);

        Intent intent = new Intent(context, RecipesActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);


        String recipeName = WidgetConfigurationActivity.loadRecipeNamePref(context, appWidgetId);
        views.setTextViewText(R.id.recipe_name, recipeName);

        Set<String> recipeIngredients = WidgetConfigurationActivity.loadRecipeIngredientsPref(context, appWidgetId);
        ArrayList<String> ingredientsArray = new ArrayList<>();
        if(recipeIngredients != null) {
            for (String ingredient : recipeIngredients)
                ingredientsArray.add(ingredient);

//            views.setTextViewText(R.id.appwidget_text, ingredientsArray.get(0));
            views.setOnClickPendingIntent(R.id.wrapper, pendingIntent);
        }else
            views.setTextViewText(R.id.no_ingredients, context.getResources().getString(R.string.no_ingredients) );

        // Set up the collection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setRemoteAdapter(context, views, appWidgetId);
        } else {
            setRemoteAdapterV11(context, views, appWidgetId);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
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

    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static  void setRemoteAdapter(Context context, @NonNull final RemoteViews views, int appWidgetId) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setData(Uri.fromParts("appWidgetId", String.valueOf(appWidgetId), null));
        views.setRemoteAdapter(R.id.widget_list,
                intent);
    }

    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    @SuppressWarnings("deprecation")
    private static void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views, int appWidgetId) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setData(Uri.fromParts("appWidgetId", String.valueOf(appWidgetId), null));
        views.setRemoteAdapter(0, R.id.widget_list,
                intent);
    }
}

