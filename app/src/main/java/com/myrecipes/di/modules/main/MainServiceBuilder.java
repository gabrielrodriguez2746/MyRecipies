package com.myrecipes.di.modules.main;

import com.myrecipes.IngredientsIntentService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainServiceBuilder {

    @ContributesAndroidInjector
    abstract IngredientsIntentService bindRecipesIntentService();

}
