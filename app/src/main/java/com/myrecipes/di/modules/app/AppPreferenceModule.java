package com.myrecipes.di.modules.app;

import com.myrecipes.data.preference.LastRecipePreference;
import com.myrecipes.data.preference.MyLastRecipePreference;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class AppPreferenceModule {

    @Binds
    @Singleton
    public abstract LastRecipePreference bindLastRecipePreference(MyLastRecipePreference manager);

}
