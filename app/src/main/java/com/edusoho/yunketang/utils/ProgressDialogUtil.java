package com.edusoho.yunketang.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by any on 16/12/22.
 */

public class ProgressDialogUtil {
    private static ProgressDialog mProgressDialog;

    public static void showProgress(Context context) {
        showProgress(context, "正在加载中...");
    }

    public static void showProgress(Context context, String msg) {
        showProgress(context, msg, true);
    }

    public static void showProgress(Context context, String msg, boolean cancelable) {
        if (isValidContext(context)) {
            if (mProgressDialog != null) {
                mProgressDialog.cancel();
            }
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(msg);
            mProgressDialog.setCancelable(cancelable);
            mProgressDialog.show();
        }
    }

    public static void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    public static boolean isShowing() {
        if (mProgressDialog != null) {
            return mProgressDialog.isShowing();
        }
        return false;
    }

    private static boolean isValidContext(Context context) {
        Activity a = (Activity) context;
        return !a.isDestroyed() && !a.isFinishing();
    }

}
