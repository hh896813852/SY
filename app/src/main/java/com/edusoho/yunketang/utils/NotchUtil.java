package com.edusoho.yunketang.utils;

import android.content.Context;

import java.lang.reflect.Method;

public class NotchUtil {

    public static int getNotchHeight(Context context) {
        if (OSUtils.isEmui()) {
            if (hasNotchInHuawei(context)) {
                return getNotchSize(context)[1];
            }
        }
        return ScreenUtil.getStatusBarHeight(context);
    }

    /**
     * 判断该华为手机是否刘海屏
     */
    private static boolean hasNotchInHuawei(Context context) {
        boolean hasNotch = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method hasNotchInScreen = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            if (hasNotchInScreen != null) {
                hasNotch = (boolean) hasNotchInScreen.invoke(HwNotchSizeUtil);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNotch;
    }

    /**
     * 获取华为手机刘海尺寸：width、heightint
     * [0]值为刘海宽度int；[1]值为刘海高度
     */
    private static int[] getNotchSize(Context context) {
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            LogUtil.e("test", "getNotchSize ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            LogUtil.e("test", "getNotchSize NoSuchMethodException");
        } catch (Exception e) {
            LogUtil.e("test", "getNotchSize Exception");
        } finally {
            return ret;
        }
    }
}
