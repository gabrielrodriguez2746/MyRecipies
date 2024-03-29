package com.myrecipes.di.component;

import com.myrecipes.RecipesApplication;
import com.myrecipes.di.modules.app.AppActivityBuilder;
import com.myrecipes.di.modules.app.AppConfigurationModule;
import com.myrecipes.di.modules.app.AppDataBaseModule;
import com.myrecipes.di.modules.app.AppModule;
import com.myrecipes.di.modules.app.AppPreferenceModule;
import com.myrecipes.di.modules.app.AppServiceBuilder;
import com.myrecipes.di.modules.app.FactoryModule;
import com.myrecipes.di.modules.main.IngredientsRepositoryModule;
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
        ApiModule.class,
        AppActivityBuilder.class,
        AppServiceBuilder.class,
        AppConfigurationModule.class,
        AppDataBaseModule.class,
        AppModule.class,
        AppNetworkModule.class,
        AppPreferenceModule.class,
        IngredientsRepositoryModule.class,
        FactoryModule.class,
        RecipesRepositoryModule.class
})
public interface MainComponent extends AndroidInjector<RecipesApplication> {

    @Component.Factory
    interface Factory {

        MainComponent create(@BindsInstance RecipesApplication application);
    }

}
