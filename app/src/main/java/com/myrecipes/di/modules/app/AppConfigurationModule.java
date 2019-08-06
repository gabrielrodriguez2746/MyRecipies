package com.myrecipes.di.modules.app;

import com.myrecipes.base.providers.AppResourceProvider;
import com.myrecipes.base.providers.ResourceProvider;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module
public abstract class AppConfigurationModule {

    @Binds
    @Reusable
    public abstract ResourceProvider bindResourceProvider(AppResourceProvider provider);
}