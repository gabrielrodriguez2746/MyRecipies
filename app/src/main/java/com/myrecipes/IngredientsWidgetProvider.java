package com.myrecipes;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.myrecipes.activities.MainActivity;
import com.myrecipes.data.models.Recipe;

import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                Recipe recipe, int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views;
        Timber.d("updating widget :: %s", recipe.toString());
        if (recipe.getIngredients().isEmpty()) {
            views = new RemoteViews(context.getPackageName(), R.layout.item_recipes_empty_widget);
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.iv_icon, pendingIntent);
        } else {
            views = new RemoteViews(context.getPackageName(), R.layout.item_recipes_widget);
            ListRemoteViewFactory.setItems(recipe.getIngredients());
            Intent intent = new Intent(context, ListWidgetsService.class);
            views.setTextViewText(R.id.tv_title, recipe.getName());
            views.setRemoteAdapter(R.id.lv_recipes, intent);

        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager,
                                        Recipe recipe, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipe, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        IngredientsIntentService.startActionUpdateRecipes(context);
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

