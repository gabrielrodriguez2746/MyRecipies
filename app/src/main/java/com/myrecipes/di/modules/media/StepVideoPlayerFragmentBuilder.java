package com.myrecipes.di.modules.media;

import com.myrecipes.di.modules.detail.RecipeDetailRepositoryModule;
import com.myrecipes.di.scopes.FragmentScope;
import com.myrecipes.fragments.StepVideoPlayerFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class StepVideoPlayerFragmentBuilder {

    @ContributesAndroidInjector(
            modules = {
                    RecipeDetailRepositoryModule.class,
                    RecipeStepViewModelModule.class
            }
    )
    @FragmentScope
    public abstract StepVideoPlayerFragment bindStepVideoPlayerFragment();
}
