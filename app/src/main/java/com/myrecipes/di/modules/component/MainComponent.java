package com.myrecipes.di.modules.component;

import com.myrecipes.RecipesApplication;
import com.myrecipes.di.modules.network.ApiModule;
import com.myrecipes.di.modules.network.AppNetworkModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ApiModule.class,
        AppNetworkModule.class
})
public interface MainComponent extends AndroidInjector<RecipesApplication> {

    @Component.Factory
    interface Factory {

        MainComponent create(@BindsInstance RecipesApplication application);
    }

}