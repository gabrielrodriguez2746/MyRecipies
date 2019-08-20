package com.myrecipes.di.modules.media;

import androidx.lifecycle.ViewModel;

import com.myrecipes.di.keys.ViewModelKey;
import com.myrecipes.viewmodels.media.RecipeStepViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class RecipeStepViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RecipeStepViewModel.class)
    public abstract ViewModel bindRecipeStepViewModelModuleViewModel(RecipeStepViewModel viewModel);

}