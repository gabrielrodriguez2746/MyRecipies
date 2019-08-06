package com.myrecipes.di.component;

import com.myrecipes.RecipesApplication;
import com.myrecipes.di.modules.app.AppActivityBuilder;
import com.myrecipes.di.modules.app.AppConfigurationModule;
import com.myrecipes.di.modules.app.AppModule;
import com.myrecipes.di.modules.app.FactoryModule;
import com.myrecipes.di.modules.main.RecipesRepositoryModule;
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
        AppConfigurationModule.class,
        ApiModule.class,
        AppActivityBuilder.class,
        AppNetworkModule.class,
        AppModule.class,
        FactoryModule.class,
        RecipesRepositoryModule.class
})
public interface MainComponent extends AndroidInjector<RecipesApplication> {

    @Component.Factory
    interface Factory {

        MainComponent create(@BindsInstance RecipesApplication application);
    }

}