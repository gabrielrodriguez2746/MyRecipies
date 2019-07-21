package com.myrecipes.base.interceptors;

import com.myrecipes.BuildConfig;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

public class LogsInterceptor implements Interceptor {

    @Inject
    public LogsInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                message -> Timber.tag("Server").e(message)
        );
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.NONE;
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY;
        }
        loggingInterceptor.setLevel(level);
        return loggingInterceptor.intercept(chain);
    }
}
