package com.edusoho.yunketang.utils;

import android.util.Log;

import com.edusoho.yunketang.BuildConfig;


public class LogUtil {
    public static final String TAG = "@SYEducation";

    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static final int maxLength = 3500;

    public static void v(String msg) {
        if (DEBUG) {
            for (int index = 0; index < msg.length(); index += maxLength) {
                String sub;
                if (msg.length() <= index + maxLength) {
                    sub = msg.substring(index);
                } else {
                    sub = msg.substring(index, index + maxLength);
                }
                Log.v(TAG, sub);
            }
        }
    }

    public static void v(String tag, String msg) {
        if (DEBUG) {
            for (int index = 0; index < msg.length(); index += maxLength) {
                String sub;
                if (msg.length() <= index + maxLength) {
                    sub = msg.substring(index);
                } else {
                    sub = msg.substring(index, index + maxLength);
                }
                Log.v(TAG + "." + tag, sub);
            }
        }
    }

    public static void d(String msg) {
        if (DEBUG) {
            for (int index = 0; index < msg.length(); index += maxLength) {
                String sub;
                if (msg.length() <= index + maxLength) {
                    sub = msg.substring(index);
                } else {
                    sub = msg.substring(index, index + maxLength);
                }
                Log.d(TAG, sub);
            }
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            for (int index = 0; index < msg.length(); index += maxLength) {
                String sub;
                if (msg.length() <= index + maxLength) {
                    sub = msg.substring(index);
                } else {
                    sub = msg.substring(index, index + maxLength);
                }
                Log.d(TAG + "." + tag, sub);
            }
        }
    }

    public static void i(String msg) {
        if (DEBUG) {
            for (int index = 0; index < msg.length(); index += maxLength) {
                String sub;
                if (msg.length() <= index + maxLength) {
                    sub = msg.substring(index);
                } else {
                    sub = msg.substring(index, index + maxLength);
                }
                Log.i(TAG, sub);
            }
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            for (int index = 0; index < msg.length(); index += maxLength) {
                String sub;
                if (msg.length() <= index + maxLength) {
                    sub = msg.substring(index);
                } else {
                    sub = msg.substring(index, index + maxLength);
                }
                Log.i(TAG + "." + tag, sub);
            }
        }
    }

    public static void w(String msg) {
        if (DEBUG) {
            Log.w(TAG, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.w(TAG + "." + tag, msg);
        }
    }

    public static void w(String msg, Throwable tr) {
        if (DEBUG) {
            Log.w(TAG, msg, tr);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            Log.w(TAG + "." + tag, msg, tr);
        }
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(TAG + "." + tag, msg);
    }

    public static void e(String msg, Throwable tr) {
        Log.e(TAG, msg, tr);
    }

    public static void e(String tag, String msg, Throwable tr) {
        Log.e(TAG + "." + tag, msg, tr);
    }
}