package com.edusoho.yunketang.edu.order.payments;


import com.edusoho.yunketang.edu.base.BasePresenter;
import com.edusoho.yunketang.edu.base.BaseView;
import com.edusoho.yunketang.edu.bean.OrderInfo;

/**
 * Created by DF on 2017/4/7.
 */

interface PaymentsContract {

    interface View extends BaseView<Presenter> {

        void showLoadDialog(boolean isShow);

        void onAlipay(String data, String orderSn);

        void onWxPay(String url, String orderSn);

        void onPayFinish();

        void updateOrderList(String orderSn);

        void showWxPayNotSupportToast();

        void onDisplay(OrderInfo orderInfo);
    }

    interface Presenter extends BasePresenter {

        void createOrderAndPay(final String payment, String password, float orderPrice);

        void payOrderByCoin(String orderSn, String coinAmount, String password);

        void payOrderByRMB(String gateway, String orderSn);
    }

}
