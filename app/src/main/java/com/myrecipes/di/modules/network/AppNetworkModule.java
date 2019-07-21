package com.myrecipes.di.modules.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myrecipes.data.deserializer.RecipiesWrapperDeserializer;
import com.myrecipes.data.models.RecipesWrapper;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

@Module(includes = {
        AppClientModule.class,
        AppInterceptorsModule.class
})
public class AppNetworkModule {

    @Provides
    @Reusable
    public Gson provideGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(RecipesWrapper.class, new RecipiesWrapperDeserializer());
        return builder.create();
    }
}
