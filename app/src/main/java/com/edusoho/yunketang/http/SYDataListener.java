package com.edusoho.yunketang.http;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.base.core.ActivityManager;
import com.edusoho.yunketang.bean.User;
import com.edusoho.yunketang.bean.base.Message;
import com.edusoho.yunketang.helper.ToastHelper;
import com.edusoho.yunketang.ui.MainTabActivity;
import com.edusoho.yunketang.ui.login.LoginActivity;

/**
 * Created by any on 17/6/14.
 */

public class SYDataListener<T> implements DataListener<T> {

    public void onSuccess(T data) {

    }

    public void onFail(int status, String failMessage) {
        if (!TextUtils.isEmpty(failMessage)) {
            ToastHelper.showToast(SYApplication.getInstance().getApplicationContext(), failMessage);
        }
    }

    public void onMessage(Message<T> message) {
        if (message.status == 1) {
            onSuccess(message.data);
        } else {
            onFail(message.status, message.msg);
            // token失效
            if (message.retcode == 401) {
                User user = SYApplication.getInstance().getUser();
                if (user != null) {
                    SYApplication.getInstance().setUser(null);
                    Activity activity = ActivityManager.getTopActivity();
                    if (activity != null) {
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                        if (!(activity instanceof MainTabActivity)) {
                            activity.finish();
                        }
                    }
                }
            }
        }
    }
}
