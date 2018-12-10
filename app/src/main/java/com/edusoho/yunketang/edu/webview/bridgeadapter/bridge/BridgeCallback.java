package com.edusoho.yunketang.edu.webview.bridgeadapter.bridge;

import android.text.TextUtils;

import com.edusoho.yunketang.edu.webview.bridgeadapter.JsBridgeAdapter;


/**
 * Created by su on 2015/12/9.
 */
public class BridgeCallback {

    private String mCallbackId;
    private String mError;
    private JsBridgeAdapter mJsBridgeAdapter;

    public BridgeCallback(String callbackId, JsBridgeAdapter jsBridgeAdapter) {
        this.mCallbackId = callbackId;
        this.mJsBridgeAdapter = jsBridgeAdapter;
    }

    public void success(Object message) {
        formatMessage("success", message);
    }

    public void error(Object message) {
        formatMessage("error", message);
    }

    protected void formatMessage(String type, Object message) {
        String args = message == null || TextUtils.isEmpty(message.toString()) ? "''" : "'" + message.toString() + "'";
        mJsBridgeAdapter.getWebView().invokeCallback(mCallbackId, type, args);
    }
}
