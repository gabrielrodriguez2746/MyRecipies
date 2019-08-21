package com.myrecipes.di.modules.detail;

import androidx.lifecycle.ViewModel;

import com.myrecipes.di.keys.ViewModelKey;
import com.myrecipes.viewmodels.detail.RecipeDetailViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class RecipeDetailViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RecipeDetailViewModel.class)
    public abstract ViewModel bindRecipeDetailViewModel(RecipeDetailViewModel viewModel);

}