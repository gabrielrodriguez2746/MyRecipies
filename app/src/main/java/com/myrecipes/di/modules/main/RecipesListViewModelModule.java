package com.myrecipes.di.modules.main;

import androidx.lifecycle.ViewModel;

import com.myrecipes.di.keys.ViewModelKey;
import com.myrecipes.viewmodels.main.RecipesListViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class RecipesListViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RecipesListViewModel.class)
    public abstract ViewModel bindPouplarMoviesViewModel(RecipesListViewModel viewModel);
}