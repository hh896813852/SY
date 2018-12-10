package com.edusoho.yunketang.edu.webview.bridgeadapter;

import android.app.Activity;

import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by su on 2015/12/11.
 */
public class BridgeWebChromeClient extends WebChromeClient {

    protected AbstractJsBridgeAdapterWebView mWebview;
    protected Activity mActivity;

    public BridgeWebChromeClient(AbstractJsBridgeAdapterWebView webView) {
        this.mWebview = webView;
        init();
    }

    private void init() {
        mActivity = mWebview.getActitiy();
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {

        try {
            JSONArray params = new JSONArray(defaultValue);
            String targetName = params.getString(0);
            String method = params.getString(1);

            Object execResult = JsBridgeAdapter.getInstance().executeAnsy(targetName, method, message);

            result.confirm(execResult == null ? "" : execResult.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        mActivity.setTitle(title);
    }
}
