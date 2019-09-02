package com.myrecipes.di.modules.main;

import com.myrecipes.respositories.MyRecipesRepository;
import com.myrecipes.respositories.RecipesRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RecipesRepositoryModule {

    @Binds
    @Singleton
    public abstract RecipesRepository bindRecipesRepository(MyRecipesRepository repository);

}
