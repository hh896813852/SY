package com.edusoho.yunketang.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.bean.User;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;

/**
 * 访问网络的工具包
 *
 * @author any
 */
public class HttpUtil {

    public static final int HTTP_OK = 200;
    private static final int TIME_OUT_DEFAULT = 30 * 1000;

    private static String error = "error:";

    /**
     * get访问返回字符串
     */
    public static String httpGet(String url) {
        return httpGet(url, TIME_OUT_DEFAULT, null);
    }

    /**
     * get访问返回字符串
     */
    public static String httpGet(String url, Map<String, String> header) {
        return httpGet(url, TIME_OUT_DEFAULT, header);
    }

    /**
     * get访问返回字符串
     */
    public static String httpGet2(String url, Map<String, String> header) {
        return httpGet(url, TIME_OUT_DEFAULT, header);
    }

    /**
     * get访问返回字符串
     */
    public static String httpGet(String url, int timeout, Map<String, String> header) {
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            if (header != null && header.size() > 0) {
                for (String key : header.keySet()) {
                    conn.setRequestProperty(key, header.get(key));
                }
            }
            conn.setConnectTimeout(timeout);
            int code = conn.getResponseCode();
            if (code == HTTP_OK) {
                return getResponseString(conn.getInputStream());
            } else {
                LogUtil.e("Net Error:", code + "");
                return error + code + ",errorMsg:" + conn.getResponseMessage();
            }
        } catch (Exception e) {
            LogUtil.e("NetError", e);
            return null;
        }
    }

    /**
     * post访问返回字符串
     */
    public static String httpPost(String url, Map<String, Object> myParams, Map<String, String> header) {
        return httpPost(url, myParams, TIME_OUT_DEFAULT, null, header);
    }

    /**
     * post访问返回字符串
     */
    public static String httpPost(String url, Map<String, Object> myParams, int timeout) {
        return httpPost(url, myParams, timeout, null, null);
    }

    /**
     * post访问返回字符串
     */
    public static String httpPost(String url, Map<String, Object> myParams, String cookie, Map<String, String> header) {
        return httpPost(url, myParams, TIME_OUT_DEFAULT, cookie, header);
    }

    /**
     * post访问返回字符串
     */
    public static String httpPost(String url, Map<String, Object> myParams, int timeout, String cookie, Map<String, String> header) {
        try {
            byte[] data = getRequestData(myParams, "UTF-8").toString().getBytes();//获得请求体
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("POST");
            if (cookie != null) {
                conn.setRequestProperty("Cookie", cookie);
            }
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", data.length + "");
            if (header != null && header.size() > 0) {
                for (String key : header.keySet()) {
                    conn.setRequestProperty(key, header.get(key));
                }
            }
            conn.getOutputStream().write(data);
            conn.setConnectTimeout(timeout);
            int code = conn.getResponseCode();
            if (code == HTTP_OK) {
                return getResponseString(conn.getInputStream());
            } else {
                LogUtil.e("Net Error:", code + "");
                return "{\"code\":" + code + "}";
            }
        } catch (Exception e) {
            LogUtil.e("NetError", e);
            return null;
        }
    }

    /**
     * json post访问返回字符串
     */
    public static String jsonPost(String url, Object json, Map<String, String> header) {
        if (json instanceof String) {
            return jsonPost(url, (String) json, TIME_OUT_DEFAULT, header);
        } else {
            return jsonPost(url, JsonUtil.toJson(json), TIME_OUT_DEFAULT, header);
        }
    }

    /**
     * json post访问返回字符串
     */
    public static String jsonPost(String url, String json) {
        return jsonPost(url, json, TIME_OUT_DEFAULT, null);
    }

    /**
     * json post访问返回字符串
     */
    public static String jsonPost(String url, Map json) {
        return jsonPost(url, JsonUtil.toJson(json), TIME_OUT_DEFAULT, null);
    }

    /**
     * json post访问返回字符串
     */
    public static String jsonPost(String url, String json, int timeout, Map<String, String> header) {
        try {
            byte[] data = json.getBytes();//获得请求体
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestProperty("content-type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            if (header != null && header.size() > 0) {
                for (String key : header.keySet()) {
                    conn.setRequestProperty(key, header.get(key));
                }
            }
            conn.setDoOutput(true);
            conn.setConnectTimeout(timeout);
            conn.getOutputStream().write(data);
            int code = conn.getResponseCode();
            if (code == HTTP_OK) {
                return getResponseString(conn.getInputStream());
            } else {
                LogUtil.e("Net Error:", code + "");
                return error + code;
            }
        } catch (Exception e) {
            LogUtil.e("NetError", e);
            return "";
        }
    }

    private static String getResponseString(InputStream inputStream) throws IOException {
//		BufferedReader re = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
//		String line = null;
//		StringBuffer rst = new StringBuffer(100);
//		while ((line = re.readLine()) != null) {
//			rst.append(line);
//		}
//		return rst.toString();

        StringBuffer rst = new StringBuffer(100);
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) > 0) {
            String tmp = new String(buffer, 0, length, "UTF-8");
            rst.append(tmp);
        }
        return rst.toString();
    }

    /*
     * Function : 封装请求体信息 Param : params请求体内容，encode编码格式
     */
    public static StringBuffer getRequestData(Map<String, Object> params, String encode) throws Exception {
        StringBuffer stringBuffer = new StringBuffer(); // 存储封装好的请求体信息
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (!TextUtils.isEmpty(entry.getKey()) && !TextUtils.isEmpty(entry.getValue().toString())) {
                    stringBuffer.append(entry.getKey()).append("=").append(URLEncoder.encode(String.valueOf(entry.getValue()), encode)).append("&");
                }
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1); // 删除最后的一个"&"
        }
        return stringBuffer;
    }

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    /**
     * 上传多张图片及参数
     *
     * @param reqUrl URL地址
     * @param params 参数
     * @param files  图片路径
     */
    public static Observable<String> sendMultipart(String reqUrl, Map<String, String> params, Map<String, File> files) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        return Observable.create(subscriber -> {
            MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
            multipartBodyBuilder.setType(MultipartBody.FORM);
            //遍历map中所有参数到builder
            if (params != null) {
                for (String key : params.keySet()) {
                    multipartBodyBuilder.addFormDataPart(key, params.get(key));
                }
            }
            //遍历paths中所有图片绝对路径到builder
            if (files != null) {
                for (String key : files.keySet()) {
                    multipartBodyBuilder.addFormDataPart(key, files.get(key).getName(), RequestBody.create(MEDIA_TYPE_PNG, files.get(key)));
                }
            }
            //构建请求体
            RequestBody requestBody = multipartBodyBuilder.build();

            User loginUser = SYApplication.getInstance().getUser();
            Request.Builder requestBuilder = new Request.Builder()
                    .header("token", loginUser.syjyToken)
                    .url(reqUrl)
                    .post(requestBody);
            Request request = requestBuilder.build();

            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    subscriber.onError(e);
                    subscriber.onCompleted();
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String str = response.body().string();
                    subscriber.onNext(str);
                    subscriber.onCompleted();
                    call.cancel();
                }
            });
        });
    }

    public static Observable<String> doDelete(String reqUrl, String token) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        return Observable.create(subscriber -> {
            FormBody formBody = new FormBody.Builder().build();
            Request.Builder builder = new Request.Builder().url(reqUrl).delete(formBody);
            builder.addHeader("X-Auth-Token", token);
            builder.addHeader("Accept", "application/vnd.edusoho.v2+json");
            Request request = builder.build();

            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    subscriber.onError(e);
                    subscriber.onCompleted();
                    call.cancel();
                    Log.e("111", "失败onFailure: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String str = response.body().string();
                    subscriber.onNext(str);
                    subscriber.onCompleted();
                    call.cancel();
                }
            });
        });
    }

    public static Observable<String> okJsonPost(String url, String json) {
        OkHttpClient okHttpClient = new OkHttpClient();
        return Observable.create(subscriber -> {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    subscriber.onError(e);
                    subscriber.onCompleted();
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String str = response.body().string();
                    subscriber.onNext(str);
                    subscriber.onCompleted();
                    call.cancel();
                }
            });
        });
    }

    public static Observable<String> okFormPost(String url, Map<String, String> params) {
        OkHttpClient okHttpClient = new OkHttpClient();
        return Observable.create(subscriber -> {
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            //遍历map中所有参数到builder
            if (params != null) {
                for (String key : params.keySet()) {
                    formBodyBuilder.add(key, params.get(key));
                }
            }
            Request request = new Request.Builder()
                    .addHeader("X-Auth-Token", SYApplication.getInstance().token)
                    .addHeader("Accept", "application/vnd.edusoho.v2+json")
                    .url(url)
                    .post(formBodyBuilder.build())
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    subscriber.onError(e);
                    subscriber.onCompleted();
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String str = response.body().string();
                    subscriber.onNext(str);
                    subscriber.onCompleted();
                    call.cancel();
                }
            });
        });
    }
}