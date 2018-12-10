package com.edusoho.yunketang.bean.seting;


public class PaymentSetting {
    private Boolean alipayEnabled;
    private Boolean wxpayEnabled;

    public Boolean getAlipayEnabled() {
        if (alipayEnabled == null) {
            return false;
        }
        return alipayEnabled;
    }

    public Boolean getWxpayEnabled() {
        if (wxpayEnabled == null) {
            return false;
        }
        return wxpayEnabled;
    }

    public boolean isNull() {
        return this.alipayEnabled == null && this.wxpayEnabled == null;
    }
}
