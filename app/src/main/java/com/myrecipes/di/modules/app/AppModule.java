package com.myrecipes.di.modules.app;

import android.content.Context;

import com.myrecipes.RecipesApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    @Singleton
    public Context providesContext(RecipesApplication application) {
        return application.getApplicationContext();
    }
}