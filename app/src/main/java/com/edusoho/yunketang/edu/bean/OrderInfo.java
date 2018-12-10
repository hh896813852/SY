package com.edusoho.yunketang.edu.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by DF on 2017/4/11.
 */

public class OrderInfo implements Serializable {
    public static final String RMB  = "RMB";
    public static final String COIN = "Coin";

    public int          targetId;
    public String       targetType;
    public float        totalPrice;
    public float        coinPayAmount;
    public AccountBean  account;
    public int          hasPayPassword;
    public String       coinName;
    public String       priceType;
    @SerializedName("cashRate")
    public float        coinRate;
    public int          fullCoinPayable;
    public String       title;
    public List<Coupon> availableCoupons;
    public float        sumPrice;
    public String       unitType;
    public int          duration;
    public String       buyType;

    public static class AccountBean implements Serializable {
        public String id;
        public String userId;
        public float  cash;
    }

    public static class Coupon implements Serializable {
        public static final String MINUS    = "minus";
        public static final String DISCOUNT = "discount";
        public String  id;
        public String  code;
        public String  type;
        /**
         * 打折：折扣数，优惠券：优惠金额
         */
        @SerializedName("rate")
        public float   value;
        public String  userId;
        public String  deadline;
        public String  targetType;
        public int     targetId;
        public boolean isSelector;

        /**
         * 返回优惠券数额
         *
         * @param orderInfo
         * @return 优惠券：数额，打折券：打折数量
         */
        public float getValue(OrderInfo orderInfo) {
            if (MINUS.equals(type)) {
                if (RMB.equals(orderInfo.priceType)) {
                    return value;
                } else if (COIN.equals(orderInfo.priceType)) {
                    return value * orderInfo.coinRate;
                }
            } else if (DISCOUNT.equals(type)) {
                return value;
            }
            return 0f;
        }

        public String toString(OrderInfo orderInfo) {
            if (MINUS.equals(type)) {
                if (COIN.equals(orderInfo.priceType)) {
                    return String.format("减 %.2f %s", getValue(orderInfo), orderInfo.coinName);
                } else {
                    return String.format("减 ¥ %.2f", getValue(orderInfo));
                }
            } else {
                return String.format("打 %.2f 折", getValue(orderInfo));
            }
        }

        public String toCouponString(OrderInfo orderInfo) {
            if (MINUS.equals(type)) {
                if (COIN.equals(orderInfo.priceType)) {
                    return String.format("%.2f %s", getValue(orderInfo), orderInfo.coinName);
                } else {
                    return String.format("¥ %.2f", getValue(orderInfo));
                }
            } else {
                return String.format("%.2f 折", getValue(orderInfo));
            }
        }
    }

    public float getPrice() {
        if (RMB.equals(priceType)) {
            return totalPrice;
        } else if (COIN.equals(priceType)) {
            return totalPrice;
        }
        return 0f;
    }

    public void check(Coupon coupon) {
        if (coupon == null) {
            sumPrice = totalPrice;
            return;
        }
        if (Coupon.MINUS.equals(coupon.type)) {
            sumPrice = totalPrice - coupon.getValue(this);
            sumPrice = sumPrice >= 0 ? sumPrice : 0;
        } else if (Coupon.DISCOUNT.equals(coupon.type)) {
            sumPrice = totalPrice * coupon.getValue(this) * 10 / 100;
        }
    }

    public float getSumPrice() {
        if (sumPrice == 0) {
            sumPrice = totalPrice;
        }
        return sumPrice;
    }

    public String getPriceWithUnit() {
        if (RMB.equals(priceType)) {
            return String.format("¥ %.2f", totalPrice);
        } else if (COIN.equals(priceType)) {
            return String.format("%.2f %s", totalPrice, coinName);
        }
        return "";
    }

    public String getSumPriceWithUnit() {
        if (RMB.equals(priceType)) {
            return String.format("¥ %.2f", sumPrice);
        } else if (COIN.equals(priceType)) {
            return String.format("%.2f %s", sumPrice, coinName);
        }
        return "";
    }

    public float getSumPriceByType(String type) {
        float sumPrice = 0f;
        if (RMB.equals(type)) {
            if (COIN.equals(priceType)) {
                sumPrice = Float.valueOf(new DecimalFormat("#.00").format(getDecimalDivide(getSumPrice() + "", coinRate + "")));
            } else {
                sumPrice = getSumPrice();
            }
        } else if (COIN.equals(type)) {
            if (COIN.equals(priceType)) {
                sumPrice = getSumPrice();
            } else {
                sumPrice = Float.valueOf(new DecimalFormat("#.00").format(getDecimalMultiply(getSumPrice() + "", coinRate + "")));
            }
        }
        return sumPrice;
    }

    public String getSumPriceByTypeWithUnit(String type) {
        if (RMB.equals(type)) {
            if (COIN.equals(priceType)) {
                return String.format("¥ %.2f", getSumPrice() / coinRate);
            } else {
                return String.format("¥ %.2f", getSumPrice());
            }
        } else if (COIN.equals(type)) {
            if (COIN.equals(priceType)) {
                return String.format("%.2f %s", getSumPrice(), coinName);
            } else {
                return String.format("%.2f %s", getSumPrice() * coinRate, coinName);
            }
        }
        return "";
    }

    public static float getDecimalMultiply(String num1, String num2) {
        return new BigDecimal(num1).multiply(new BigDecimal(num2)).floatValue();
    }

    public static float getDecimalDivide(String num1, String num2) {
        return new BigDecimal(num1).divide(new BigDecimal(num2)).floatValue();
    }
}
