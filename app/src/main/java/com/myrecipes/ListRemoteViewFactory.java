package com.myrecipes;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.myrecipes.data.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ListRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private static List<Ingredient> items = new ArrayList<>();
    private Context context;

    public ListRemoteViewFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        Timber.d("Creating");
    }

    @Override
    public void onDataSetChanged() {
        Timber.d("onDataSetChanged");
    }

    @Override
    public void onDestroy() {
        Timber.d("onDataSetChanged");
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient ingredient = items.get(position);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_recipe_widget);
        views.setTextViewText(R.id.tv_recipe, ingredient.getName());
        return views;
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
        int id = position;
        if (!items.isEmpty() && items.size() > position) {
            id = items.get(position).getId();
        }
        return id;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public static void setItems(List<Ingredient> items) {
        ListRemoteViewFactory.items = items;

    }
}
