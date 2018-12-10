package com.edusoho.yunketang.edu.plugin;

import android.util.Log;

import com.edusoho.yunketang.edu.utils.annotations.JsAnnotation;
import com.edusoho.yunketang.edu.webview.bridgeadapter.bridge.BaseBridgePlugin;
import com.edusoho.yunketang.edu.webview.bridgeadapter.bridge.BridgeCallback;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by su on 2015/12/9.
 */
public class JsNativeAppPlugin extends BaseBridgePlugin {

    @JsAnnotation
    public void startup(JSONArray args, BridgeCallback callbackContext) throws JSONException {
        Log.d("JsNativeAppPlugin", "startup");
        callbackContext.success("");
    }

    @Override
    public String getName() {
        return "App";
    }
}
