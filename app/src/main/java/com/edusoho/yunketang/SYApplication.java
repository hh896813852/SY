package com.edusoho.yunketang;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.view.Display;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.edusoho.yunketang.base.core.ActivityManager;
import com.edusoho.yunketang.bean.User;
import com.edusoho.yunketang.edu.bean.RequestUrl;
import com.edusoho.yunketang.edu.utils.ToastUtils;
import com.edusoho.yunketang.edu.utils.VolleySingleton;
import com.edusoho.yunketang.helper.AppPreferences;
import com.edusoho.yunketang.utils.JsonUtil;
import com.edusoho.yunketang.utils.RequestUtil;
import com.edusoho.yunketang.utils.volley.StringVolleyRequest;
import com.gensee.fastsdk.GenseeLive;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.shuyu.gsyvideoplayer.cache.CacheFactory;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.tencent.smtt.sdk.QbSdk;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.UnknownHostException;

import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;
import tv.danmaku.ijk.media.exo2.ExoPlayerCacheManager;

/**
 * @author huhao on 2018/7/4
 */
public class SYApplication extends MultiDexApplication {
    private User user;
    private static SYApplication application;
    public String host = SYConstants.HTTP_URL_ONLINE;
    public int courseType = 1; // 1、上元在线 2、上元会计
    public String token;
    public String domain = "www.233863.com";
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        registerActivityLifecycle();
        // 设置GSYVideoPlayer内核和缓存方式
        PlayerFactory.setPlayManager(Exo2PlayerManager.class);     // EXO模式
        CacheFactory.setCacheManager(ExoPlayerCacheManager.class); // exo缓存模式，支持m3u8，只支持exo
        // threetenabp初始化（日历控件有使用）
        AndroidThreeTen.init(this);
        QbSdk.initX5Environment(getBaseContext(), null);
        // 关闭Android9.0弹出框（Detected problems with API compatibility）
        closeAndroidPDialog();
    }
    public static SYApplication getInstance() {
        return application;
    }
    public User getUser() {
        if (user == null) {
            String json = AppPreferences.getUserInfo();
            if (!TextUtils.isEmpty(json)) {
                user = JsonUtil.fromJson(json, User.class);
            }
        }
        return user;
    }
    public void reSaveUser() {
        String json = JsonUtil.toJson(this.user);
        // 保存
        AppPreferences.setUserInfo(json);
    }
    public void setUser(User user) {
        if (user != null) {
            this.user = user;
            // 保存
            AppPreferences.setUserInfo(JsonUtil.toJson(user));
        } else {
            this.user = null;
            // 清除
            AppPreferences.clearUserInfo();
        }
    }

    /**
     * 是否登录
     */
    public boolean isLogin() {
        return getUser() != null && getUser().syjyUser != null;
    }

    /**
     * 是否存在对应平台的token
     */
    public boolean hasTokenInOtherPlatform(int courseType) {
        this.courseType = courseType;
        return !((courseType == 1 && !TextUtils.isEmpty(user.syzxToken)) || (courseType == 2 && !TextUtils.isEmpty(user.sykjToken)));
    }

    /**
     * 是否在对应平台注册了
     */
    public boolean isRegisterInOtherPlatform(int courseType) {
        this.courseType = courseType;
        return (courseType == 1 && user.isRegisterSyzx) || (courseType == 2 && user.isRegisterSykj);
    }

    /**
     * 设置baseUrl
     */
    public void setHost(String host) {
        this.host = host;
        if (host.equals(SYConstants.HTTP_URL_ONLINE)) {
            if (user != null) {
                this.token = user.syzxToken;
            }
            this.courseType = 1;
            this.domain = "www.233863.com";
            this.schoolHost = "http://www.233863.com/mapi_v2/";
        }
        if (host.equals(SYConstants.HTTP_URL_ACCOUNTANT)) {
            if (user != null) {
                this.token = user.sykjToken;
            }
            this.courseType = 2;
            this.domain = "www.sykjxy.com";
            this.schoolHost = "http://www.sykjxy.com/mapi_v2/";
        }
    }

    /**
     * activity管理
     */
    private void registerActivityLifecycle() {
        ActivityLifecycleCallbacks lifecycleCallbacks = new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ActivityManager.addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityManager.removeActivity(activity);
            }
        };
        registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    /**
     * 关闭Android9.0弹出框（Detected problems with API compatibility）
     */
    private void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int screenW;
    public int screenH;

    public void setDisplay(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        this.screenH = display.getHeight();
        this.screenW = display.getWidth();
    }

    public void startUpdateWebView(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }

    public String schoolHost = "http://www.233863.com/mapi_v2/";

    public RequestUrl bindUrl(String url, boolean addToken) {
        StringBuffer sb = new StringBuffer(schoolHost);
        sb.append(url);
        RequestUrl requestUrl = new RequestUrl(sb.toString());

        if (addToken) {
            requestUrl.heads.put("token", token);
        }
        return requestUrl;
    }

    public Request<String> postUrl(final RequestUrl requestUrl, final Response.Listener<String> responseListener, final Response.ErrorListener errorListener) {
        VolleySingleton.getInstance(this).getRequestQueue();
        StringVolleyRequest request = processorStringVolleyRequest(requestUrl, responseListener, errorListener, Request.Method.POST);
        request.setCacheMode(StringVolleyRequest.CACHE_AUTO);
        request.setTag(requestUrl.url);
        return VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    /**
     * volley get 请求
     *
     * @param requestUrl       url、参数、header等信息
     * @param responseListener 返回response信息
     * @param errorListener    错误信息
     */
    public void getUrl(final RequestUrl requestUrl, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        VolleySingleton.getInstance(this).getRequestQueue();
        StringVolleyRequest request = processorStringVolleyRequest(requestUrl, responseListener, errorListener, Request.Method.GET);
        request.setCacheMode(StringVolleyRequest.CACHE_AUTO);
        request.setTag(requestUrl.url);
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private StringVolleyRequest processorStringVolleyRequest(
            final RequestUrl requestUrl,
            final Response.Listener<String> responseListener,
            final Response.ErrorListener errorListener,
            int method
    ) {
        return new StringVolleyRequest(method, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = RequestUtil.handleRequestError(response);
                } catch (RequestUtil.RequestErrorException re) {
                }
                responseListener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getCause() instanceof UnknownHostException) {
                    errorListener.onErrorResponse(error);
                    return;
                }
                if (error instanceof NoConnectionError) {
                    errorListener.onErrorResponse(error);
                    ToastUtils.show(getApplicationContext(), "您网络暂时无法连接，请稍后重试");
                    return;
                }
                if (error instanceof TimeoutError) {
                    errorListener.onErrorResponse(error);
                    return;
                }
                if (error.networkResponse == null) {
                    return;
                }
                try {
                    if (TextUtils.isEmpty(RequestUtil.handleRequestError(error.networkResponse.data))) {
                        return;
                    }
                } catch (RequestUtil.RequestErrorException re) {
                }

                if (errorListener == null) {
                    return;
                }
                errorListener.onErrorResponse(error);
            }
        });
    }
}
