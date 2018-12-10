package com.edusoho.yunketang.edu.utils;

public class DigitalUtils {

    /**
     * 是否整数
     *
     * @param number
     * @return
     */
    public static String removeZero(double number) {
        if (number == (int) number) {
            return (int) number + "";
        } else {
            return number + "";
        }
    }
}
