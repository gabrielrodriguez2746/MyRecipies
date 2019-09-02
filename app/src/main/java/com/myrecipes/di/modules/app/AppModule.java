package com.myrecipes.di.modules.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.myrecipes.R;
import com.myrecipes.RecipesApplication;
import com.myrecipes.base.providers.ResourceProvider;

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

    @Provides
    @Singleton
    public SharedPreferences providePreferences(Context context, ResourceProvider resourceProvider) {
        return context.getSharedPreferences(
                resourceProvider.getString(R.string.my_recipes_preference), Context.MODE_PRIVATE
        );
    }
}
