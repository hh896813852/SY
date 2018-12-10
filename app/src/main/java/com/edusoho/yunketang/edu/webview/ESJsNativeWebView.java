package com.edusoho.yunketang.edu.webview;

import android.content.Context;
import android.util.AttributeSet;

import com.edusoho.yunketang.edu.base.ActionBarBaseActivity;
import com.edusoho.yunketang.edu.webview.bridgeadapter.AbstractJsBridgeAdapterWebView;
import com.tencent.smtt.sdk.WebSettings;

/**
 * Created by su on 2015/12/8.
 */
public class ESJsNativeWebView extends AbstractJsBridgeAdapterWebView<ActionBarBaseActivity> {

    public ESJsNativeWebView(Context context) {
        super(context);
    }

    public ESJsNativeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ESJsNativeWebView(Context context, AttributeSet attrs, int defstyle) {
        super(context, attrs, defstyle);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
    }

    @Override
    protected void initWebSetting(WebSettings webSettings) {
        super.initWebSetting(webSettings);
        String userAgent = webSettings.getUserAgentString();
        webSettings.setUserAgentString(userAgent.replace("Android", "Android-kuozhi;Android"));
    }
}
