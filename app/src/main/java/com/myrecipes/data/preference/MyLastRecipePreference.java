package com.myrecipes.data.preference;

import android.content.SharedPreferences;

import javax.inject.Inject;

public class MyLastRecipePreference extends MyPreferenceManager implements LastRecipePreference {

    private static final String LAST_RECIPE_KEY = "LAST_RECIPE_KEY";

    @Inject
    public MyLastRecipePreference(SharedPreferences sharedPreferences) {
        super(sharedPreferences);
    }

    @Override
    public int getLastSeenRecipeId() {
        return getInt(LAST_RECIPE_KEY, -1);
    }

    @Override
    public void setLastRecipeId(int id) {
        saveInt(LAST_RECIPE_KEY, id);
    }
}
