package com.myrecipes.di.modules.main;

import com.myrecipes.activities.MainActivity;
import com.myrecipes.di.scopes.ActivityScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityBuilder {

    @ContributesAndroidInjector(modules = {
            RecipesListFragmentBuilder.class,
    })
    @ActivityScope
    abstract MainActivity bindMainActivity();

}