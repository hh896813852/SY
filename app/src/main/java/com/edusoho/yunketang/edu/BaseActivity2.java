package com.edusoho.yunketang.edu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.bean.UserRole;
import com.edusoho.yunketang.bean.json.GsonEnumTypeAdapter;
import com.edusoho.yunketang.edu.bean.ErrorResult;
import com.edusoho.yunketang.edu.bean.MessageEvent;
import com.edusoho.yunketang.edu.bean.RequestUrl;
import com.edusoho.yunketang.edu.factory.FactoryManager;
import com.edusoho.yunketang.edu.factory.UtilFactory;
import com.edusoho.yunketang.edu.factory.provider.AppSettingProvider;
import com.edusoho.yunketang.edu.utils.CommonUtil;
import com.edusoho.yunketang.edu.utils.VolleySingleton;
import com.edusoho.yunketang.utils.ProgressDialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

/**
 * Created by JesseHuang on 15/5/6.
 * 一般用于NoActionBar的theme
 */
public class BaseActivity2 extends AppCompatActivity {
    public static final String TAG  = "ActionBarBaseActivity";
    public static final String BACK = "返回";
    protected BaseActivity2 mActivity;
    public Gson gson;
    protected Context         mContext;
    public SYApplication app;
    public ActionBar mActionBar;
    protected FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mContext = this;
        initActivity();
        EventBus.getDefault().removeAllStickyEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initActivity() {
        app = SYApplication.getInstance();
        mActionBar = getSupportActionBar();
        mFragmentManager = getSupportFragmentManager();
        app.setDisplay(this);

        gson = new GsonBuilder()
                .registerTypeAdapter(UserRole.class, new GsonEnumTypeAdapter<>(UserRole.NO_SUPPORT))
                .create();
    }

    public void hideActionBar() {
        if (mActionBar == null) {
            return;
        }
        mActionBar.hide();
    }

    public void showActionBar() {
        if (mActionBar == null) {
            return;
        }
        mActionBar.show();
    }

    public void ajaxPost(final RequestUrl requestUrl) {
        VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return requestUrl.getParams();
            }
        };
        stringRequest.setTag(requestUrl.url);

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void ajaxPostWithLoading(final RequestUrl requestUrl, final Response.Listener<String> responseListener, final Response.ErrorListener errorListener, String loadingText) {
        if (!TextUtils.isEmpty(loadingText)) {
            ProgressDialogUtil.showProgress(mActivity, loadingText);
        } else {
            ProgressDialogUtil.showProgress(mActivity);
        }
        app.postUrl(requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ProgressDialogUtil.hideProgress();
                responseListener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressDialogUtil.hideProgress();
                if (errorListener != null) {
                    errorListener.onErrorResponse(error);
                } else {
                    CommonUtil.longToast(mContext, "网络连接不可用或请求失败");
                }
            }
        });
    }

    public void ajaxPostMultiUrl(final RequestUrl requestUrl, final Response.Listener<String> responseListener, final Response.ErrorListener errorListener, int method) {
//        app.postMultiUrl(requestUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                responseListener.onResponse(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if (errorListener != null) {
//                    errorListener.onErrorResponse(error);
//                } else {
//                    CommonUtil.longToast(mContext, getResources().getString(R.string.request_fail_text));
//                }
//            }
//        }, method);
    }

    public void ajaxPost(final RequestUrl requestUrl, final Response.Listener<String> responseListener, final Response.ErrorListener errorListener) {
        app.postUrl(requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                responseListener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorListener != null) {
                    errorListener.onErrorResponse(error);
                } else if (error instanceof NoConnectionError) {
                    CommonUtil.longToast(mContext, "网络连接不可用或请求失败");
                }
            }
        });
    }

    public void ajaxGet(final String url, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        app.getUrl(new RequestUrl(url), responseListener, errorListener);
    }

    public void ajaxGet(final RequestUrl requestUrl, final Response.Listener<String> responseListener, final Response.ErrorListener errorListener) {
        app.getUrl(requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                responseListener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorListener != null) {
                    errorListener.onErrorResponse(error);
                } else {
                    CommonUtil.longToast(mContext, "网络连接不可用或请求失败");
                }
            }
        });
    }


    public void runService(String serviceName) {
//        app.mEngine.runService(serviceName, mActivity, null);
    }

    public <T> T parseJsonValue(String json, TypeToken<T> typeToken) {
        T value = null;
        try {
            value = mActivity.gson.fromJson(
                    json, typeToken.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }

        return value;
    }

    public <T> T handleJsonValue(String response, TypeToken<T> typeToken) {
        ErrorResult result = parseJsonValue(response, new TypeToken<ErrorResult>() {
        });
        if (result != null && result.error != null) {
            CommonUtil.longToast(mActivity, result.error.message);
            return null;
        } else {
            return parseJsonValue(response, typeToken);
        }
    }

    protected AppSettingProvider getAppSettingProvider() {
        return FactoryManager.getInstance().create(AppSettingProvider.class);
    }

    protected UtilFactory getUtilFactory() {
        return FactoryManager.getInstance().create(UtilFactory.class);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveStickyNessage(MessageEvent messageEvent) {
        if (messageEvent.getType() == MessageEvent.REWARD_POINT_NOTIFY) {
            CommonUtil.shortCenterToast(mContext, messageEvent.getMessageBody().toString());
        }
    }
}
