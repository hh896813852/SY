package com.edusoho.yunketang.edu.http;

import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.utils.StringUtils;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by JesseHuang on 2017/4/18.
 */

public class HttpUtils {

    public static final String X_AUTH_TOKEN_KEY = "X-Auth-Token";
    public static final String AUTH_TOKEN_KEY = "Auth-Token";
    public static final String MAPI_V2_TOKEN_KEY = "token";
    private static HttpUtils mInstance;
    private static HttpUtils mOldInstance;
    public String mBaseUrl;
    private Map<String, String> mHeaderMaps = new TreeMap<>();

    public static HttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (HttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new HttpUtils();
                }
            }
        }
        mInstance.mBaseUrl = "";
        mInstance.mHeaderMaps.clear();
        return mInstance;
    }

    public static HttpUtils getOldInstance() {
        if (mOldInstance == null) {
            synchronized (HttpUtils.class) {
                if (mOldInstance == null) {
                    mOldInstance = new HttpUtils();
                }
            }
        }
        mOldInstance.mBaseUrl = "";
        mOldInstance.mHeaderMaps.clear();
        return mOldInstance;
    }

    /**
     * 老接口 /api/
     *
     * @return
     */
    public HttpUtils baseOnApi() {
        mBaseUrl = SYApplication.getInstance().host + "api/";
        return mOldInstance;
    }

    public HttpUtils setBaseUrl(String url) {
        mBaseUrl = url + "/";
        return mOldInstance;
    }

    public <T> T createApi(final Class<T> clazz) {
        if ("".equals(mBaseUrl) || mBaseUrl == null) {
            return RetrofitClient.getInstance(mHeaderMaps).create(clazz);
        } else {
            return RetrofitClient.getInstance(mBaseUrl, mHeaderMaps).create(clazz);
        }
    }

    public HttpUtils addTokenHeader(String token) {
        if (!StringUtils.isEmpty(token)) {
            mHeaderMaps.put(X_AUTH_TOKEN_KEY, token);
        }
        return this;
    }
}
