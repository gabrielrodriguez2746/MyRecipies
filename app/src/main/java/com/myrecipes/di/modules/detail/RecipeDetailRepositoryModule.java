package com.myrecipes.di.modules.detail;

import com.myrecipes.di.scopes.FragmentScope;
import com.myrecipes.respositories.RecipeDetailRepository;
import com.myrecipes.respositories.MyRecipeDetailRepository;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RecipeDetailRepositoryModule {

    @Binds
    @FragmentScope
    public abstract RecipeDetailRepository bindDetailRecipeRepository(MyRecipeDetailRepository repository);
}