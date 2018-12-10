package com.edusoho.yunketang.edu.order.alipay;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.edu.base.BaseActivity;
import com.edusoho.yunketang.edu.bean.MessageEvent;
import com.edusoho.yunketang.edu.order.payments.PaymentsPresenter;
import com.edusoho.yunketang.ui.course.CoursePlayerActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * Created by DF on 2017/4/12.
 * 跳转进支付宝界面
 */

public class AlipayActivity extends BaseActivity {

    public static final  int    CODE_PAYMENT            = 1;
    public static final  int    CODE_WXPAY_FINISH       = 2;
    private static final String PAYMENT                 = "payment";
    private static final String WECHAT_URL              = "wechat_url";
    private static final String TARGET_ID               = "targetId";
    private static final String URL_DATA                = "urlData";
    private static final String TARGET_TYPE             = "targetType";
    private static final String ORDER_SN                = "order_sn";
    private static final String ALIPAY_CALLBACK_SUCCESS = "alipayCallback?1";
    private static final String ALIPAY_CALLBACK_FAIL    = "alipayCallback?0";
    private static final String PAYMENT_CALLBACK        = "/pay/center/success/show";

    private String  mPayment;
    private WebView webView;
    private String  mData;
    private String  mUrl;
    private int     mTargetId;
    private String  mTargetType;
    private String  mOrderSn;

    public static void launch(Context context, String data, int targetId, String targetType, String orderSn, String payment) {
        Intent intent = new Intent(context, AlipayActivity.class);
        intent.putExtra(URL_DATA, data);
        intent.putExtra(TARGET_ID, targetId);
        intent.putExtra(TARGET_TYPE, targetType);
        intent.putExtra(ORDER_SN, orderSn);
        intent.putExtra(PAYMENT, payment);
        context.startActivity(intent);
    }

    public static void launchForResult(BaseActivity activity, String url, String orderSn, String payment) {
        Intent intent = new Intent(activity, AlipayActivity.class);
        intent.putExtra(PAYMENT, payment);
        intent.putExtra(ORDER_SN, orderSn);
        intent.putExtra(WECHAT_URL, url);
        activity.startActivityForResult(intent, CODE_PAYMENT);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay);
        initView();
        mPayment = getIntent().getStringExtra(PAYMENT);
        if (PaymentsPresenter.ALIPAY.equals(mPayment)) {
            mData = getIntent().getStringExtra(URL_DATA);
            mTargetId = getIntent().getIntExtra(TARGET_ID, 0);
            mOrderSn = getIntent().getStringExtra(ORDER_SN);
            mTargetType = getIntent().getStringExtra(TARGET_TYPE);
            initAlipayData();
        } else if (PaymentsPresenter.WXPAY.equals(mPayment)) {
            mUrl = getIntent().getStringExtra(WECHAT_URL);
            mOrderSn = getIntent().getStringExtra(ORDER_SN);
            initWxPayData(mUrl);
        }
    }

    private void initView() {
        webView = findViewById(R.id.wv);
    }

    private void initWxPayData(final String wxPayUrl) {
        webView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setWebContentsDebuggingEnabled(true);
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("wechat", url);
                if (url.contains("weixin://wap/pay?")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    webView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setResult(CODE_WXPAY_FINISH);
                            updateOrderView();
                            finish();
                        }
                    }, 1000);
                } else {
                    HashMap<String, String> extraHeaders = new HashMap<>();
                    extraHeaders.put("Referer", SYApplication.getInstance().host);
                    view.loadUrl(url, extraHeaders);
                }
                return true;
            }
        });

        webView.loadUrl(wxPayUrl);
    }

    private void initAlipayData() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // ------  对alipays:相关的scheme处理 -------
                Log.d("alipay", url);
                if (url.startsWith("alipays:") || url.startsWith("alipay")) {
                    try {
                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getBaseContext().startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                if (url.contains(ALIPAY_CALLBACK_SUCCESS)) {
                    showToast(R.string.join_success);
                    onRedirect();
                    return true;
                }
                if (url.contains(PAYMENT_CALLBACK) || url.contains(ALIPAY_CALLBACK_FAIL)) {
                    finish();
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadDataWithBaseURL(null, mData, "text/html", "utf-8", null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
        }
    }

    private void onRedirect() {
        updateOrderView();
        if ("classroom".equals(this.mTargetType)) {
//            EdusohoApp.app.mEngine.runNormalPlugin("ClassroomActivity", this, new PluginRunCallback() {
//                @Override
//                public void setIntentDate(Intent startIntent) {
//                    startIntent.putExtra(Const.CLASSROOM_ID, mTargetId);
//                }
//            });
        } else {
            Intent intent = new Intent(this, CoursePlayerActivity.class);
            intent.putExtra(CoursePlayerActivity.COURSE_PROJECT_ID, mTargetId);
        }
        finishAffinity();
    }

    private void updateOrderView() {
        EventBus.getDefault().postSticky(new MessageEvent<>(mOrderSn, MessageEvent.UPDATE_ORDER_VIEW));
    }
}
