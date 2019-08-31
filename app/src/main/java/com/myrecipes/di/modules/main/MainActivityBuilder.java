package com.myrecipes.di.modules.main;

import com.myrecipes.activities.MainActivity;
import com.myrecipes.di.modules.detail.RecipeDetailFragmentBuilder;
import com.myrecipes.di.modules.detail.RecipeMasterDetailFragmentBuilder;
import com.myrecipes.di.modules.media.StepVideoPlayerFragmentBuilder;
import com.myrecipes.di.scopes.ActivityScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityBuilder {

    @ContributesAndroidInjector(modules = {
            RecipesListFragmentBuilder.class,
            RecipeDetailFragmentBuilder.class,
            RecipeMasterDetailFragmentBuilder.class,
            StepVideoPlayerFragmentBuilder.class
    })
    @ActivityScope
    abstract MainActivity bindMainActivity();

}