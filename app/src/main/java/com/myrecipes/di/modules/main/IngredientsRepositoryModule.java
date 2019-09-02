package com.myrecipes.di.modules.main;

import com.myrecipes.respositories.IngredientsRepository;
import com.myrecipes.respositories.MyIngredientsRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class IngredientsRepositoryModule {

    @Binds
    @Singleton
    public abstract IngredientsRepository bindIngredientsRepository(MyIngredientsRepository repository);

}
