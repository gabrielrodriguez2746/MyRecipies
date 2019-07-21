package com.myrecipes.base.interceptors;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeadersInterceptor implements Interceptor {

    @Inject
    public HeadersInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        builder.header("Accept", "application/json");
        builder.header("Content-Type", "application/json");
        builder.method(request.method(), request.body());
        return chain.proceed(builder.build());
    }
}
