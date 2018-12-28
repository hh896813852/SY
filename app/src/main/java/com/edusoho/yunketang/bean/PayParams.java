package com.edusoho.yunketang.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 支付参数
 *
 * @author huhao on 2018/12/03
 */
public class PayParams implements Serializable {
    /**
     * 微信
     */
    public static final int PAY_TYPE_WECHAT = 1;
    /**
     * 支付宝
     */
    public static final int PAY_TYPE_ALIPAY = 2;

    private String orderNo;
    private int orderId;
    private String price;
    private int payType;

    // 支付宝参数
    public String orderStr;

    // 微信支付参数
    public String appid;
    public String sign;
    public String timestamp;
    public String noncestr;
    public String partnerid;
    public String prepayid;
    @SerializedName("package")
    public String packageValue;


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }
}
