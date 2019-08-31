package com.myrecipes.di.modules.detail;

import androidx.lifecycle.ViewModel;

import com.myrecipes.di.keys.ViewModelKey;
import com.myrecipes.viewmodels.detail.RecipeMasterDetailViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class RecipeMasterDetailViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RecipeMasterDetailViewModel.class)
    public abstract ViewModel bindRecipeMasterDetailViewModel(RecipeMasterDetailViewModel viewModel);

}