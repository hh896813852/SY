package com.edusoho.yunketang.edu.webview.bridgeadapter;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.edusoho.yunketang.edu.webview.bridgeadapter.bridge.BaseBridgePlugin;
import com.edusoho.yunketang.edu.webview.bridgeadapter.bridge.BridgeCallback;
import com.edusoho.yunketang.edu.webview.bridgeadapter.bridge.IBridgePlugin;
import com.edusoho.yunketang.edu.webview.bridgeadapter.bridge.PluginManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by su on 2015/12/8.
 */
public class JsBridgeAdapter {

    private static Object mLock = new Object();
    private static JsBridgeAdapter instance;
    private PluginManager mPluginManager;
    private ArrayList<Class> mPluginList;
    private Stack<WeakReference<AbstractJsBridgeAdapterWebView>> mWebViewQueue;

    private static final String TAG = "JsBridgeAdapter";

    public JsBridgeAdapter() {
        mWebViewQueue = new Stack<>();
        mPluginManager = new PluginManager();
    }

    public static JsBridgeAdapter getInstance() {
        synchronized (mLock) {
            if (instance == null) {
                instance = new JsBridgeAdapter();
            }
        }
        return instance;
    }

    public void init() {
        mPluginList = new ArrayList<>();
    }

    public void addPlugin(Class<? extends BaseBridgePlugin> pluginClass) {
        mPluginList.add(pluginClass);
    }

    public AbstractJsBridgeAdapterWebView getWebView() {
        if (mWebViewQueue.empty()) {
            return null;
        }

        return mWebViewQueue.peek().get();
    }

    public void registPlugin(AbstractJsBridgeAdapterWebView webView) {
        mWebViewQueue.push(new WeakReference<AbstractJsBridgeAdapterWebView>(webView));
        mPluginManager.registPluginList(webView, mPluginList);
    }

    public void unResgit(AbstractJsBridgeAdapterWebView webView) {
        if (!mWebViewQueue.empty()) {
            mWebViewQueue.pop();
        }
        mPluginManager.unRegistPlugin(webView.getUUId());
    }

    private IBridgePlugin getBridgePlugin(String targetName) {
        if (mWebViewQueue.isEmpty()) {
            return null;
        }
        WeakReference<AbstractJsBridgeAdapterWebView> webViewWeakReference = mWebViewQueue.peek();
        return mPluginManager.getPlugin(webViewWeakReference.get().getUUId(), targetName);
    }

    @JavascriptInterface
    public void exec(String callbackId, String targetName, String method, String args) {
        Log.d(TAG, String.format("type:%s m:%s c:%s", targetName, method, callbackId));
        IBridgePlugin nativeBridge = getBridgePlugin(targetName);
        try {
            nativeBridge.execute(method, new JSONArray(args), new BridgeCallback(callbackId, this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Object executeAnsy(String targetName, String method, String args) {
        Log.d(TAG, String.format("type:%s m%s", targetName, method));
        Object result = null;
        IBridgePlugin nativeBridge = getBridgePlugin(targetName);
        if (nativeBridge == null) {
            return "";
        }
        try {
            result = nativeBridge.executeAnsy(method, new JSONArray(args));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
