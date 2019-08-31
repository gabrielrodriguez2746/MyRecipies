package com.myrecipes.di.modules.detail;

import com.myrecipes.di.scopes.FragmentScope;
import com.myrecipes.fragments.RecipeDetailFragment;
import com.myrecipes.fragments.RecipeMasterDetailFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RecipeMasterDetailFragmentBuilder {

    @ContributesAndroidInjector(modules = {
            RecipeDetailRepositoryModule.class,
            RecipeMasterDetailViewModelModule.class
    })
    @FragmentScope
    public abstract RecipeMasterDetailFragment bindRecipeMasterDetailFragment();
}
