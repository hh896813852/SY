package com.edusoho.yunketang.edu.webview;

import android.app.Activity;

import com.edusoho.yunketang.edu.plugin.JsNativeAppPlugin;
import com.edusoho.yunketang.edu.webview.bridgeadapter.AbstractJsBridgeAdapterWebView;
import com.edusoho.yunketang.edu.webview.bridgeadapter.JsBridgeAdapter;

/**
 * Created by howzhi on 15/7/15.
 */
public class ESCordovaWebViewFactory {

    private static final String TAG = "ESCordovaWebViewFactory";
    private static ESCordovaWebViewFactory factory;

    private ESCordovaWebViewFactory() {
    }

    public static void init() {
        JsBridgeAdapter jsBridgeAdapter = JsBridgeAdapter.getInstance();
        jsBridgeAdapter.init();
//        jsBridgeAdapter.addPlugin(MenuClickPlugin.class);
        jsBridgeAdapter.addPlugin(JsNativeAppPlugin.class);
        factory = new ESCordovaWebViewFactory();
    }

    public static ESCordovaWebViewFactory getFactory() {
        if (factory == null) {
            init();
        }
        return factory;
    }

    public void destory() {
        factory = null;
    }

    public AbstractJsBridgeAdapterWebView getWebView(Activity activity) {
        return new ESJsNativeWebView(activity);
    }
}
