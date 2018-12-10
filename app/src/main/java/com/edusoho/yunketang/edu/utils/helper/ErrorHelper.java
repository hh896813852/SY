package com.edusoho.yunketang.edu.utils.helper;

import android.util.SparseArray;

/**
 * Created by JesseHuang on 2017/5/29.
 */

public class ErrorHelper {

    public static final int UNKNOW_ERROR            = 0;
    public static final int API_NOT_FOUND           = 1;
    public static final int BAD_REQUEST             = 2;
    public static final int API_TOO_MANY_CALLS      = 3;
    public static final int INVALID_CREDENTIAL      = 4;
    public static final int EXPIRED_CREDENTIAL      = 5;
    public static final int BANNED_CREDENTIAL       = 6;
    public static final int INTERNAL_SERVER_ERROR   = 7;
    public static final int INTERNAL_TIMEOUT_OR_BAD = 8;
    public static final int JSON_ERROR              = 100;

    private static SparseArray<String> mCodeMapper = new SparseArray<>();

    static {
        mCodeMapper.put(UNKNOW_ERROR, "未知错误");
        mCodeMapper.put(API_NOT_FOUND, "接口不存在");
        mCodeMapper.put(BAD_REQUEST, "请求出错");
        mCodeMapper.put(API_TOO_MANY_CALLS, "访问次数过多");
        mCodeMapper.put(INVALID_CREDENTIAL, "账号登录已过期或在其他设备登录");
        mCodeMapper.put(EXPIRED_CREDENTIAL, "账号登录已过期或在其他设备登录");
        mCodeMapper.put(BANNED_CREDENTIAL, "账号已被封锁");
        mCodeMapper.put(INTERNAL_SERVER_ERROR, "网校服务器存在异常");
        mCodeMapper.put(INTERNAL_TIMEOUT_OR_BAD, "您网络暂时无法连接，请稍后重试");
        mCodeMapper.put(JSON_ERROR, "数据获取失败");
    }

    public static String getErrorMessage(int code) {
        return mCodeMapper.get(code);
    }

    public static String getMessage(int code) {
        return mCodeMapper.get(code) + " - " + code;
    }

    public static String getMessage() {
        return mCodeMapper.get(0);
    }
}
