package com.edusoho.yunketang.edu.http;

import com.edusoho.yunketang.SYApplication;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JesseHuang on 2017/4/18.
 */

public class RetrofitClient {

    private static Retrofit.Builder   retrofitBuilder;
    private static RequestInterceptor mRequestInterceptor;

    public static Retrofit getInstance(Map<String, String> headerMaps) {
        if (retrofitBuilder == null) {
            synchronized (RetrofitClient.class) {
                if (retrofitBuilder == null) {
                    retrofitBuilder = new Retrofit.Builder()
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
                }
            }
        }
        headerMaps.put("Accept", "application/vnd.edusoho.v2+json");
        mRequestInterceptor = new RequestInterceptor(headerMaps);
        retrofitBuilder.baseUrl(getBaseUrl()).client(getClient());
        return retrofitBuilder.build();
    }

    public static Retrofit getInstance(String baseUrl, Map<String, String> headerMaps) {
        if (retrofitBuilder == null) {
            retrofitBuilder = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        }
        if (headerMaps != null) {
            mRequestInterceptor = new RequestInterceptor(headerMaps);
        }
        retrofitBuilder.baseUrl(baseUrl).client(getClient());
        return retrofitBuilder.build();
    }

    private static OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(mRequestInterceptor)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public static String getBaseUrl() {
        return SYApplication.getInstance().host + "api/";
    }
}
