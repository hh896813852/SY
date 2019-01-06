package com.edusoho.yunketang.utils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 访问网络的工具包
 *
 * @author huhao
 */
public class OkHttpUtil {
    private static final int TIME_OUT_DEFAULT = 30; // 秒
    private static String error = "error:";

    public static String httpGet(String url, int timeout, Map<String, String> header) {
        try {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                    .readTimeout(timeout, TimeUnit.MILLISECONDS)
                    .build();

            Request.Builder request = new Request.Builder().url(url);
            if (header != null && header.size() > 0) {
                for (String key : header.keySet()) {
                    request.addHeader(key, header.get(key));
                }
            }
            Call call = okHttpClient.newCall(request.build());
            Response response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                LogUtil.e("Net Error:", response.code() + "");
                return error + response.code() + ",errorMsg:" + response.message();
            }
        } catch (IOException e) {
            LogUtil.e("NetError", e);
            return null;
        }
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String jsonPost(String url, String json, int timeout, Map<String, String> header) {
        try {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                    .readTimeout(timeout, TimeUnit.MILLISECONDS)
                    .build();

            RequestBody body = RequestBody.create(JSON, json);
            Request.Builder request = new Request.Builder().url(url).post(body);
            if (header != null && header.size() > 0) {
                for (String key : header.keySet()) {
                    request.addHeader(key, header.get(key));
                }
            }
            Call call = okHttpClient.newCall(request.build());
            Response response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                LogUtil.e("Net Error:", response.code() + "");
                return "{\"code\":" + response.code() + ",errorMsg:" + response.message() + "}";
            }
        } catch (IOException e) {
            LogUtil.e("NetError", e);
            return null;
        }
    }
}