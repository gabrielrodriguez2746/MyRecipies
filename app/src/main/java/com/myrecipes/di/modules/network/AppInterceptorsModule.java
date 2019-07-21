package com.myrecipes.di.modules.network;

import com.myrecipes.base.interceptors.HeadersInterceptor;
import com.myrecipes.base.interceptors.LogsInterceptor;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;
import dagger.multibindings.IntoSet;
import okhttp3.Interceptor;

@Module
public abstract class AppInterceptorsModule {

    @Binds
    @IntoSet
    @Reusable
    public abstract Interceptor bindHeaderInterceptors(HeadersInterceptor interceptor);

    @Binds
    @IntoSet
    @Reusable
    public abstract Interceptor bindLoggingInterceptors(LogsInterceptor interceptor);

}
