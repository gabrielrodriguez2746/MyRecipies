package com.myrecipes.di.modules.main;

import com.myrecipes.di.scopes.FragmentScope;
import com.myrecipes.fragments.RecipesListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RecipesListFragmentBuilder {

    @ContributesAndroidInjector(modules = {
            RecipesListViewModelModule.class
    })
    @FragmentScope
    public abstract RecipesListFragment bindRecipesListFragment();

}
