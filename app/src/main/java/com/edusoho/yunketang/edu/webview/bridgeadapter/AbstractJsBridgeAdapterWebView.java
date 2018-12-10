package com.edusoho.yunketang.edu.webview.bridgeadapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.ViewGroup;

import com.edusoho.yunketang.edu.webview.bridgeadapter.bridge.BridgePluginContext;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;


/**
 * Created by howzhi on 15/7/15.
 */
public abstract class AbstractJsBridgeAdapterWebView<T extends Activity> extends WebView {

    protected boolean mIsBackIng;
    protected int mOverScrollY;
    private T mActivity;
    private Handler mUIHandler;
    private BridgePluginContext<T> mPluginContext;

    public AbstractJsBridgeAdapterWebView(Context context) {
        super(context);
        init(context);
    }

    public AbstractJsBridgeAdapterWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void handleDestroy() {
        JsBridgeAdapter.getInstance().unResgit(this);
    }

    public AbstractJsBridgeAdapterWebView(Context context, AttributeSet attrs, int defstyle) {
        super(context, attrs, defstyle);
        init(context);
    }

    public BridgePluginContext getBridgePluginContext() {
        return mPluginContext;
    }

    public int getUUId() {
        return hashCode();
    }

    protected void init(Context context) {
        if (!(context instanceof Activity)) {
            throw new RuntimeException("context must be Activity");
        }

        mActivity = (T) context;
        mPluginContext = new BridgePluginContext(mActivity);
        setBackgroundColor(Color.TRANSPARENT);
        initWebSetting(getSettings());

        mUIHandler = new Handler(Looper.getMainLooper());
        JsBridgeAdapter js2Native = JsBridgeAdapter.getInstance();
        js2Native.registPlugin(this);
        addJavascriptInterface(js2Native, "webViewBridgeAdapter");
    }

    protected void initWebSetting(WebSettings webSettings) {
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        setScrollBarStyle(SCROLLBARS_OUTSIDE_OVERLAY);
    }

    public boolean isGoBack() {
        return mIsBackIng;
    }

    public void setGoBackStatus(boolean status) {
        this.mIsBackIng = status;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ViewGroup parent = (ViewGroup) getParent();
            return parent.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        mOverScrollY = scrollY;
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    public void runOnMainThread(Runnable r) {
        mUIHandler.post(r);
    }

    public void execJsScript(final String script) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                loadUrl(new StringBuffer("javascript:").append(script).toString());
            }
        });
    }

    public void invokeCallback(String callbackId, String type, String args) {
        execJsScript(String.format("javascript:jsBridgeAdapter.invokeCallback('%s','%s', %s)", callbackId, type, formatString(args)));
    }

    private String formatString(String input) {
        return input.replaceAll("\\\\r", "").replaceAll("\\\\n", "");
    }

    public T getActitiy() {
        return mActivity;
    }
}
