package com.edusoho.yunketang.edu.order.payments;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.edu.api.OrderApi;
import com.edusoho.yunketang.edu.base.BaseActivity;
import com.edusoho.yunketang.edu.bean.ErrorResult;
import com.edusoho.yunketang.edu.bean.OrderInfo;
import com.edusoho.yunketang.edu.http.HttpUtils;
import com.edusoho.yunketang.edu.http.SubscriberProcessor;
import com.edusoho.yunketang.edu.utils.Constants;
import com.edusoho.yunketang.utils.encrypt.XXTEA;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.navi.NaviLifecycle;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by DF on 2017/4/7.
 */

public class PaymentsPresenter implements PaymentsContract.Presenter {

    public static final  String ALIPAY         = "alipay";
    public static final  String WXPAY          = "wechat";
    public static final  String COIN           = "coin";
    private static final String ORDER_ID       = "id";
    private static final String TARGET_TYPE    = "targetType";
    private static final String TARGET_ID      = "targetId";
    private static final String APP_PAY        = "appPay";
    private static final String PAYMENT        = "payment";
    private static final String VIP_NUM        = "num";
    private static final String VIP_UNIT       = "unit";
    private static final String COUPON_CODE    = "couponCode";
    private static final String PAY_PASSWORD   = "payPassword";
    private static final String COIN_PAYAMOUNT = "coinPayAmount";
    private static final String PAYMENT_HTML   = "paymentHtml";
    private static final String STATUS         = "status";
    private static final String STATUS_PAID    = "paid";

    private PaymentsContract.View mView;
    private OrderInfo mOrderInfo;
    private int                   mPosition;
    private int                   mTargetId;
    private String                mTargetType;
    private float                 mPrice;

    private final LifecycleProvider<ActivityEvent> mActivityLifeProvider;

    public PaymentsPresenter(PaymentsContract.View view) {
        this.mView = view;
        mActivityLifeProvider = NaviLifecycle.createActivityLifecycleProvider((BaseActivity) view);
    }

    public PaymentsPresenter(PaymentsContract.View view, OrderInfo orderInfo, int position) {
        this(view);
        this.mOrderInfo = orderInfo;
        this.mPosition = position;
    }

    public PaymentsPresenter(PaymentsContract.View view, int targetId, String targetType, float price) {
        this(view);
        this.mTargetId = targetId;
        this.mTargetType = targetType;
        this.mPrice = price;
    }

    @Override
    public void subscribe() {
        if (mOrderInfo != null) {
            mView.onDisplay(mOrderInfo);
        } else {
            HttpUtils.getInstance()
                    .addTokenHeader(TextUtils.isEmpty(SYApplication.getInstance().token) ? SYApplication.getInstance().getUser().syzxToken : SYApplication.getInstance().token)
                    .createApi(OrderApi.class)
                    .postOrderInfo(mTargetType, mTargetId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(mActivityLifeProvider.<OrderInfo>bindToLifecycle())
                    .subscribe(new SubscriberProcessor<OrderInfo>() {

                        @Override
                        public void onError(ErrorResult.Error error) {
                            mView.showToast(error.message);
                            mView.close();
                        }

                        @Override
                        public void onNext(OrderInfo orderInfo) {
                            mOrderInfo = orderInfo;
                            mOrderInfo.totalPrice = mPrice;
                            //完成未支付订单标价类型：RMB
                            mOrderInfo.priceType = OrderInfo.RMB;
                            mView.onDisplay(orderInfo);
                        }
                    });
        }
    }

    @Override
    public void createOrderAndPay(final String payment, String password, float orderPrice) {
        Map<String, String> map = createParameter(payment, password, orderPrice);
        HttpUtils.getInstance()
                .addTokenHeader(TextUtils.isEmpty(SYApplication.getInstance().token) ? SYApplication.getInstance().getUser().syzxToken : SYApplication.getInstance().token)
                .createApi(OrderApi.class)
                .createOrder(map)
                .subscribeOn(Schedulers.io())
                .compose(mActivityLifeProvider.<JsonObject>bindToLifecycle())
                .flatMap(new Func1<JsonObject, Observable<JsonObject>>() {
                    @Override
                    public Observable<JsonObject> call(JsonObject jsonObject) {
                        if (jsonObject != null) {
                            String orderId = jsonObject.get(ORDER_ID).getAsString();
                            return HttpUtils.getInstance()
                                    .addTokenHeader(TextUtils.isEmpty(SYApplication.getInstance().token) ? SYApplication.getInstance().getUser().syzxToken : SYApplication.getInstance().token)
                                    .createApi(OrderApi.class)
                                    .goPay(orderId, mOrderInfo.targetType, payment);
                        } else {
                            mView.showLoadDialog(false);
                            mView.showToast(R.string.pay_fail);
                            return null;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberProcessor<JsonObject>() {
                    @Override
                    public void onError(String error) {
                        mView.showLoadDialog(false);
                        mView.showToast(error);
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        responseDeal(jsonObject, jsonObject.get("sn").getAsString(), payment);
                    }
                });
    }

    @Override
    public void payOrderByCoin(final String orderSn, String coinAmount, String password) {
        HttpUtils.getInstance()
                .addTokenHeader(TextUtils.isEmpty(SYApplication.getInstance().token) ? SYApplication.getInstance().getUser().syzxToken : SYApplication.getInstance().token)
                .createApi(OrderApi.class)
                .payOrderByCoin("Coin", "purchase", orderSn, coinAmount, XXTEA.encryptToBase64String(password, "EduSoho"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mActivityLifeProvider.<JsonObject>bindToLifecycle())
                .subscribe(new SubscriberProcessor<JsonObject>() {

                    @Override
                    public void onError(ErrorResult.Error error) {
                        mView.showToast(error.message);
                        mView.showLoadDialog(false);
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        if (STATUS_PAID.equals(jsonObject.get(STATUS).getAsString())) {
                            mView.showLoadDialog(false);
                            mView.showToast(R.string.join_success);
                            mView.updateOrderList(orderSn);
                            mView.onPayFinish();
                        }
                    }
                });
    }

    @Override
    public void payOrderByRMB(final String gateway, final String orderSn) {
        HttpUtils.getInstance()
                .addTokenHeader(TextUtils.isEmpty(SYApplication.getInstance().token) ? SYApplication.getInstance().getUser().syzxToken : SYApplication.getInstance().token)
                .createApi(OrderApi.class)
                .payOrderByRMB(gateway, "purchase", orderSn, "Y")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mActivityLifeProvider.<JsonObject>bindToLifecycle())
                .subscribe(new SubscriberProcessor<JsonObject>() {

                    @Override
                    public void onError(ErrorResult.Error error) {
                        mView.showToast(error.message);
                        mView.showLoadDialog(false);
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        responseDeal(jsonObject, orderSn, gateway);
                    }
                });
    }

    private void responseDeal(JsonObject jsonObject, String orderSn, String payment) {
        mView.showLoadDialog(false);
        if (STATUS_PAID.equals(jsonObject.get(STATUS).getAsString())) {
            mView.showToast(R.string.join_success);
            mView.updateOrderList(orderSn);
            mView.onPayFinish();
            return;
        }
        if (ALIPAY.equals(payment) || Constants.TradeGetway.ALIPAY.equals(payment)) {
            String data = jsonObject.get(PAYMENT_HTML).getAsString();
            Pattern p = Pattern.compile("post");
            Matcher m = p.matcher(data);
            data = m.replaceFirst("get");
            mView.onAlipay(data, orderSn);
        } else if (WXPAY.equals(payment) || Constants.TradeGetway.WECHAT_PAY.equals(payment)) {
            if (jsonObject.get("paymentUrl") == null) {
                mView.showWxPayNotSupportToast();
            } else {
                mView.onWxPay(jsonObject.get("paymentUrl").getAsString(), orderSn);
            }
        }
    }

    @NonNull
    private Map<String, String> createParameter(String payment, String password, float orderPrice) {
        Map<String, String> map = new HashMap<>();
        if (mPosition != -1 && mOrderInfo.availableCoupons != null && mOrderInfo.availableCoupons.size() > 0) {
            map.put(COUPON_CODE, mOrderInfo.availableCoupons.get(mPosition).code);
        }
        if (COIN.equals(payment)) {
            map.put(COIN_PAYAMOUNT, orderPrice + "");
            password = XXTEA.encryptToBase64String(password, "EduSoho");
            map.put(PAY_PASSWORD, password);
        }
        map.put(TARGET_TYPE, mOrderInfo.targetType);
        map.put(TARGET_ID, mOrderInfo.targetId + "");
        map.put(APP_PAY, "Y");
        map.put(PAYMENT, payment);
        if ("vip".equals(mOrderInfo.targetType)) {
            map.put(VIP_NUM, mOrderInfo.duration + "");
            map.put(VIP_UNIT, mOrderInfo.unitType);
        }
        return map;
    }

    @Override
    public void unsubscribe() {
    }
}
