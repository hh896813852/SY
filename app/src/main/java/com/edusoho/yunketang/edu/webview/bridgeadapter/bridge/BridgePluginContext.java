package com.edusoho.yunketang.edu.webview.bridgeadapter.bridge;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;

/**
 * Created by su on 2015/12/9.
 */
public class BridgePluginContext<T extends Activity> extends ContextWrapper {

    private T mActivity;
    private Callback mCallback;
    public BridgePluginContext(T activity)
    {
        super(activity.getBaseContext());
        this.mActivity = activity;
    }

    public T getActivity() {
        return mActivity;
    }

    public void startActivityForResult(Callback callback, Intent intent, int requestCode) {
        this.mCallback = callback;
        mActivity.startActivityForResult(intent, requestCode);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mCallback == null) {
            return;
        }

        mCallback.onActivityResult(requestCode, resultCode, data);
    }

    public interface Callback {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
