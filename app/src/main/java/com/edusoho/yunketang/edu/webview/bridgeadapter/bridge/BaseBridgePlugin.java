package com.edusoho.yunketang.edu.webview.bridgeadapter.bridge;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.edusoho.yunketang.edu.utils.annotations.JsAnnotation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by howzhi on 15/4/17.
 */
public class BaseBridgePlugin<T extends Activity> implements IBridgePlugin {

    public static final String TAG = "BaseBridgePlugin";

    private HashMap<String, Method> mMethodList;
    protected Context mContext;
    protected BridgePluginContext mPluginContext;
    protected T mActivity;

    public BaseBridgePlugin() {
        super();
        initMethods();
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void initialize(BridgePluginContext pluginContext) {
        this.mPluginContext = pluginContext;
        mActivity = (T) pluginContext.getActivity();
        mContext = mActivity.getBaseContext();
    }

    @Override
    public Object executeAnsy(String action, JSONArray args) {
        CallbackStatus callbackStatus = invoke(action, args, null);
        return callbackStatus.getMessage();
    }

    @Override
    public boolean execute(
            String action, JSONArray args, BridgeCallback callbackContext) throws JSONException {
        CallbackStatus callbackStatus = invoke(action, args, callbackContext);

        Object message = callbackStatus.getMessage();
        switch (callbackStatus.getStatus()) {
            case CallbackStatus.ERROR:
                callbackContext.error(message);
                break;
            case CallbackStatus.SUCCESS:
                if (message != null) {
                    Log.d(TAG, "success:" + action);
                    callbackContext.success(message);
                }
                break;
            case CallbackStatus.ASYN:
        }

        return true;
    }

    private void initMethods() {
        mMethodList = new HashMap<String, Method>();
        try {
            Method[] methods = this.getClass().getMethods();
            for (Method method : methods) {
                method.setAccessible(true);
                JsAnnotation annotation = method.getAnnotation(JsAnnotation.class);
                if (annotation != null) {
                    mMethodList.put(method.getName(), method);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    public CallbackStatus invoke(String action, JSONArray args, BridgeCallback callbackContext) {
        CallbackStatus callbackStatus = new CallbackStatus();
        Method method = mMethodList.get(action);
        if (method != null) {
            try {
                Object object =  method.invoke(this, args, callbackContext);
                callbackStatus.setSuccess(object);
            } catch (Exception e) {
                e.printStackTrace();
                callbackStatus.setError(getInvokeError(e));
            }
        }

        return callbackStatus;
    }

    private JSONObject getInvokeError(Exception error) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("error", error.getMessage());
        } catch (Exception e) {
        }

        return jsonObject;
    }

    protected String[] JsonArrayToStringArray(JSONArray images) {
        int length = images.length();
        String[] strs = new String[length];
        try {
            for (int i = 0; i < length; i++) {
                strs[i] = images.getString(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return strs;
    }
}
