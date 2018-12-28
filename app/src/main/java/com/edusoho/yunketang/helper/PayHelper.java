package com.edusoho.yunketang.helper;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.edusoho.yunketang.bean.PayParams;
import com.edusoho.yunketang.utils.JsonUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.Map;

/**
 * @author huhao on 2018/12/03
 */
public class PayHelper {

    private Context context;
    private PayParams params;
    private PayResultCallback callback;
    private String appId;

    private static PayHelper instance;

    private PayHelper() {
    }

    public static PayHelper getInstance() {
        if (instance == null) {
            instance = new PayHelper();
        }
        return instance;
    }

    public PayResultCallback getCallback() {
        return callback;
    }

    public void removeCallback() {
        callback = null;
    }

    /**
     * 支付
     */
    public void pay(Context context, PayParams params, PayResultCallback callback) {
        this.context = context;
        this.params = params;
        this.callback = callback;
        logic();
    }

    private void logic() {
        switch (params.getPayType()) {
            case PayParams.PAY_TYPE_ALIPAY:
                callAlipay();
                break;
            case PayParams.PAY_TYPE_WECHAT:
                callWeChat();
                break;
        }
    }

    public String getWeChatAppId() {
        return appId;
    }

    /**
     * 微信
     */
    private void callWeChat() {
        appId = params.appid;
        IWXAPI api = WXAPIFactory.createWXAPI(context, appId);
        api.registerApp(appId);
        if (!api.isWXAppInstalled()) {
            onFail("您尚未安装微信，请安装后再试！");
        } else {
            PayReq req = new PayReq();
            req.appId = params.appid;
            req.partnerId = params.partnerid;
            req.prepayId = params.prepayid;
            req.nonceStr = params.noncestr;
            req.timeStamp = params.timestamp;
            req.packageValue = params.packageValue;
            req.sign = params.sign;
            api.sendReq(req);
        }
    }

    /**
     * 支付宝
     */
    private void callAlipay() {
        new Thread() {
            @Override
            public void run() {
                PayTask task = new PayTask((Activity) context);
                Map<String, String> result = task.payV2(params.orderStr, true);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 支付宝回调
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Map<String, String> payResult = (Map<String, String>) msg.obj;
                String resultStatus = payResult.get("resultStatus");
                if (resultStatus.equals("9000")) {
                    // 支付成功
                    onSuccess();
                } else if (resultStatus.equals("8000")) {
                    // 支付结果确认中
                    onFail("支付结果确认中");
                } else if (TextUtils.equals(resultStatus, "6001")) {
                    // 支付取消
                    onFail(null);
                } else {
                    onFail("支付失败:" + payResult.get("memo"));
                }
            }
        }
    };

    public void onSuccess() {
        if (callback != null) {
            callback.onPayResult(true);
        }
    }

    public void onFail(String errorMsg) {
        if (errorMsg != null) {
            ToastHelper.showToast(context, errorMsg);
        }
        if (callback != null) {
            callback.onPayResult(false);
        }
    }

    /**
     * 支付结果回调
     */
    public interface PayResultCallback {
        void onPayResult(boolean isSuccess);
    }
}