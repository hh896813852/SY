//package com.sy.syedu.wxapi;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import com.tencent.mm.opensdk.modelbase.BaseReq;
//import com.tencent.mm.opensdk.modelbase.BaseResp;
//import com.tencent.mm.opensdk.openapi.IWXAPI;
//import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
//import com.tencent.mm.opensdk.openapi.WXAPIFactory;
//
//public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {
//    // IWXAPI：第三方APP和微信通信的接口
//    private IWXAPI api;
//    private static final String APP_ID = "wxcc0c6bcf26892f1c";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        api = WXAPIFactory.createWXAPI(this, APP_ID, false);
//        api.handleIntent(getIntent(), this);
//    }
//
//    // 如果分享的时候，该已经开启，那么微信开始这个activity时，会调用onNewIntent，所以这里要处理微信的返回结果
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//        api.handleIntent(intent, this);
//    }
//
//    // 微信发送请求到第三方应用时，会回调到该方法
//    @Override
//    public void onReq(BaseReq arg0) {
//    }
//
//    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
//    // 比如：在微信完成文本分享操作后，回调第三方APP
//    @Override
//    public void onResp(BaseResp resp) {
//        switch (resp.errCode) {
//            case BaseResp.ErrCode.ERR_OK:
//                //发送成功
//                break;
//            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                //发送取消
//                break;
//            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                //发送被拒绝
//                break;
//            default:
//                //发送返回
//                break;
//        }
//        // 关闭页面
//        this.finish();
//    }
//}
