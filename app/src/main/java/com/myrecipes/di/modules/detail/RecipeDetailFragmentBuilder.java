package com.myrecipes.di.modules.detail;

import com.myrecipes.di.scopes.FragmentScope;
import com.myrecipes.fragments.RecipeDetailFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RecipeDetailFragmentBuilder {

    @ContributesAndroidInjector(modules = {
            RecipeDetailRepositoryModule.class,
            RecipeDetailViewModelModule.class
    })
    @FragmentScope
    public abstract RecipeDetailFragment bindRecipeDetailFragment();
}
