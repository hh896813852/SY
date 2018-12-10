package com.edusoho.yunketang.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.helper.PayHelper;
import com.edusoho.yunketang.helper.ToastHelper;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * @author yujinghong
 * 微信支付成功后的Activity
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PayHelper.getInstance().getWeChatAppId() == null) {
            finish();
            return;
        }
        api = WXAPIFactory.createWXAPI(this, PayHelper.getInstance().getWeChatAppId());
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        int errCode = resp.errCode;
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (errCode == 0) {
                if (PayHelper.getInstance().getCallback() != null) {
                    PayHelper.getInstance().getCallback().onPayResult(true);
                }
            } else if (errCode == -1) { // 错误
                if (PayHelper.getInstance().getCallback() != null) {
                    PayHelper.getInstance().getCallback().onPayResult(false);
                }
                if (TextUtils.isEmpty(resp.errStr)) {
                    ToastHelper.showToast(this, "请求微信支付失败，可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。");
                } else {
                    ToastHelper.showToast(this, resp.errStr);
                }
            } else if(errCode == -2) { // 用户取消
                if (PayHelper.getInstance().getCallback() != null) {
                    PayHelper.getInstance().getCallback().onPayResult(false);
                }
            }
            finish();
        }
    }
}