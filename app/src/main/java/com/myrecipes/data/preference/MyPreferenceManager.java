package com.myrecipes.data.preference;

import android.content.SharedPreferences;

public abstract class MyPreferenceManager {

    private SharedPreferences sharedPreferences;

    public MyPreferenceManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    protected int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    protected void saveInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

}
