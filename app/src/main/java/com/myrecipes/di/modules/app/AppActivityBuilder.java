package com.myrecipes.di.modules.app;


import com.myrecipes.di.modules.main.MainActivityBuilder;

import dagger.Module;

@Module(includes = {MainActivityBuilder.class})
public abstract class AppActivityBuilder {

}
