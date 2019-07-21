package com.myrecipes.di.modules.network;

import com.google.gson.Gson;
import com.myrecipes.R;
import com.myrecipes.base.providers.ResourceProvider;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppClientModule {

    @Provides
    @Reusable
    public OkHttpClient provideMovieClient(Set<Interceptor> interceptors) {

        long READ_TIME_OUT = 5L;
        long CONNECT_TIME_OUT = 5L;

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(READ_TIME_OUT, TimeUnit.SECONDS);
        builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS);
        for (Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }
        return builder.build();
    }

    @Provides
    @Singleton
    public Retrofit provideMovieService(OkHttpClient httpClient, ResourceProvider provider, Gson gson) {
        return new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(provider.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

}