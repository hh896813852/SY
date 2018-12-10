package com.edusoho.yunketang.utils.volley;


import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.edu.bean.RequestUrl;
import com.edusoho.yunketang.edu.utils.AppUtils;

import java.util.Map;

/**
 * Created by howzhi on 15/7/9.
 */
public abstract class BaseVolleyRequest<T> extends Request<T> {

    private static final String TAG = "BaseVolleyRequest";
    private static final int DEFUALT_TIME_OUT = 10 * 1000;

    protected static final int CACHE_MAX_AGE = 604800;

    public static final int CACHE_AUTO = 0001;
    public static final int CACHE_ALWAYS = 0002;
    public static final int CACHE_NONE = 0003;

    public static final int ALWAYS_USE_CACHE = 0010;
    public static final int AUTO_USE_CACHE = 0020;

    public static final String PARSE_RESPONSE = "parseResponse";

    protected Response.Listener<T> mListener;
    protected Response.ErrorListener mErrorListener;
    protected RequestUrl mRequestUrl;
    protected int mIsCache = CACHE_NONE;
    protected int mCacheUseMode = AUTO_USE_CACHE;
    private RequestLocalManager mRequestLocalManager;

    public BaseVolleyRequest(int method, RequestUrl requestUrl, Response.Listener<T> listener,
                             Response.ErrorListener errorListener) {
        super(method, requestUrl.url, errorListener);
        this.mRequestUrl = requestUrl;
        mListener = listener;
        mErrorListener = errorListener;
        initRequest(method);
        mRequestLocalManager = RequestLocalManager.getManager();
        this.setRetryPolicy(new DefaultRetryPolicy(DEFUALT_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    protected void initRequest(int method) {
        if (method == Method.GET) {
            mIsCache = CACHE_ALWAYS;
        }
    }

    public void setCacheUseMode(int mode) {
        this.mCacheUseMode = mode;
    }

    public void setCacheMode(int mode) {
        this.mIsCache = mode;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mRequestUrl.isMuiltkeyParams() ? mRequestUrl.getMuiltKeyParams() : mRequestUrl.getParams();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = mRequestUrl.getHeads();
        headers.put("Cookie", mRequestLocalManager.getCookie());
        return headers;
    }

    @Override
    protected void deliverResponse(T response) {
        setTag(null);
        mListener.onResponse(response);
    }

    @Override
    public String getCacheKey() {
        if (mCacheUseMode == ALWAYS_USE_CACHE && mIsCache != CACHE_NONE) {
            return super.getCacheKey();
        }
        if (!PARSE_RESPONSE.equals(getTag()) && AppUtils.isNetConnect(SYApplication.getInstance())) {
            return null;
        }
        return super.getCacheKey();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String cookie = response.headers.get("Set-Cookie");
//        String pointInfo = response.headers.get("Reward-Point-Notify");
//        if (pointInfo != null) {
//            try {
//                String pointResponse = URLDecoder.decode(pointInfo, "UTF-8");
//                if (pointResponse != null) {
//                    EventBus.getDefault().postSticky(new MessageEvent<>(pointResponse, MessageEvent.REWARD_POINT_NOTIFY));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        if (cookie != null) {
            mRequestLocalManager.setCookie(cookie);
        }
        Cache.Entry cache = handleResponseCache(response);

        setTag(PARSE_RESPONSE);
        return Response.success(getResponseData(response), cache);
    }

    protected abstract T getResponseData(NetworkResponse response);

    protected Cache.Entry handleResponseCache(NetworkResponse response) {

        switch (mIsCache) {
            case CACHE_ALWAYS:
                return parseResponseCache(response);
            case CACHE_AUTO:
                return HttpHeaderParser.parseCacheHeaders(response);
            case CACHE_NONE:
        }
        return null;
    }

    private Cache.Entry parseResponseCache(NetworkResponse response) {
        Map<String, String> map = response.headers;
        map.put("Cache-Control", String.format("max-age=%d,stale-while-revalidate=%d", 0, CACHE_MAX_AGE));
        NetworkResponse networkResponse = new NetworkResponse(response.statusCode, response.data, map, true);
        Cache.Entry cache = HttpHeaderParser.parseCacheHeaders(networkResponse);
        return cache;
    }

    protected static class RequestLocalManager {

        public String cookie = "";
        private static RequestLocalManager instance;

        public static RequestLocalManager getManager() {
            synchronized (RequestLocalManager.class) {
                if (instance == null) {
                    instance = new RequestLocalManager();
                }
            }
            return instance;
        }

        public String getCookie() {
            return cookie;
        }

        public void setCookie(String value) {
            cookie = value;
        }
    }
}
