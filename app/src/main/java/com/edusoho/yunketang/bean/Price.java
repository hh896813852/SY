package com.edusoho.yunketang.bean;

import java.io.Serializable;

public class Price implements Serializable {
    public final String COIN = "coin";
    public final String RMB  = "RMB";
    public String currency;
    public float  amount;
    public float  coinAmount;
    public String coinName;

    /**
     * 带价格单位
     *
     * @return
     */
    public String getPriceWithUnit() {
        if (COIN.equals(currency)) {
            return String.format("%.2f %s", coinAmount, coinName);
        } else {
            return String.format("%s %.2f", "¥", amount);
        }
    }

    public String getPriceWithUnit(int scale) {
        if (COIN.equals(currency)) {
            return String.format("%.2f %s", coinAmount * scale, coinName);
        } else {
            return String.format("%s %.2f", "¥", amount * scale);
        }
    }

    public float getPrice() {
        if (COIN.equals((currency))) {
            return coinAmount;
        } else {
            return amount;
        }
    }

    public String getPriceUnit() {
        if (COIN.equals(currency)) {
            return coinName;
        } else {
            return "¥ ";
        }
    }
}