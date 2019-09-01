package com.myrecipes.di.modules.main;

import com.myrecipes.RecipesIntentService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainServiceBuilder {

    @ContributesAndroidInjector
    abstract RecipesIntentService bindRecipesIntentService();

}