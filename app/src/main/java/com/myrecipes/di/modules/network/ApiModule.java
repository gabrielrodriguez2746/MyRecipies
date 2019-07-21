package com.myrecipes.di.modules.network;

import com.myrecipes.rest.RecipesService;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import retrofit2.Retrofit;

@Module
public class ApiModule {

    @Provides
    @Reusable
    RecipesService provideMoviesService(Retrofit retrofit) {
        return retrofit.create(RecipesService.class);
    }
}
