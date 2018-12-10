package com.edusoho.yunketang.edu.utils;

/**
 * Created by JesseHuang on 2017/4/6.
 */

public class StringUtils {

    /**
     * space is also return false
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    public static String isCheckNull(String str) {
        return str == null ? "" : str;
    }
}
