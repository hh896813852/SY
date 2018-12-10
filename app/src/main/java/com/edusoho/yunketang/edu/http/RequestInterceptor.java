package com.edusoho.yunketang.edu.http;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by JesseHuang on 2017/4/18.
 */

public class RequestInterceptor implements Interceptor {

    private Map<String, String> mHeaderMaps = new TreeMap<>();

    public RequestInterceptor(Map<String, String> headerMaps) {
        this.mHeaderMaps = headerMaps;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder request = chain.request().newBuilder();
        if (mHeaderMaps.size() > 0) {
            for (Map.Entry<String, String> entry : mHeaderMaps.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return chain.proceed(request.build());
    }
}
