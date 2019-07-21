package com.myrecipes.base.providers;

import android.content.Context;

import javax.inject.Inject;

public class AppResourceProvider implements ResourceProvider {

    private Context context;

    @Inject
    public AppResourceProvider(Context context) {
        this.context = context;
    }

    @Override
    public String getString(int id) {
        return context.getResources().getString(id);
    }
}
