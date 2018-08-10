package com.rachitgoyal.itunesmusicplayer.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.rachitgoyal.itunesmusicplayer.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rachit Goyal on 13/06/18
 */

class RetrofitSingleton {

    private static final String BASE_URL = "http://itunes.apple.com/";
    private static final int TIMEOUT = 30;

    private static Retrofit mRetrofit;

    private RetrofitSingleton() {
        if (mRetrofit != null) {
            throw new RuntimeException("Use getInstance() method to get the singleton instance.");
        }
    }

    static Retrofit getInstance(Context context) {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(createOkHttpClient(context))
                    .build();
        }
        return mRetrofit;
    }

    private static OkHttpClient createOkHttpClient(Context context) {
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(TIMEOUT, TimeUnit.SECONDS).readTimeout(TIMEOUT, TimeUnit.SECONDS).writeTimeout(TIMEOUT, TimeUnit.SECONDS);
        httpClient.addNetworkInterceptor(getInterceptor(context));

        File httpCacheDirectory = new File(context.getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        httpClient.cache(cache);

        return httpClient.build();
    }

    private static Interceptor getInterceptor(final Context context) {
        return new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                if (Utils.isNetworkConnected(context)) {
                    int maxAge = 60 * 60;
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                } else {
                    int maxStale = 60 * 60 * 24 * 7;
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
            }
        };
    }
}
