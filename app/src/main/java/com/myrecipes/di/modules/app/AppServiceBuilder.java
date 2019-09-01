package com.myrecipes.di.modules.app;


import com.myrecipes.di.modules.main.MainServiceBuilder;

import dagger.Module;

@Module(includes = {MainServiceBuilder.class})
public abstract class AppServiceBuilder {

}
