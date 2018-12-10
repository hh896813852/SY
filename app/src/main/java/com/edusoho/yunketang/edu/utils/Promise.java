package com.edusoho.yunketang.edu.utils;


import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by howzhi on 15/8/25.
 */
public class Promise {

    private Object mLastResolveObj;

    private Queue<PromiseCallback> mInvokeQueue;

    public Promise()
    {
        mInvokeQueue = new ArrayDeque<PromiseCallback>();
    }

    public Promise then(PromiseCallback callback) {
        if (mLastResolveObj != null) {
            callback.invoke(mLastResolveObj);
            return this;
        }
        if (callback != null) {
            mInvokeQueue.add(callback);
        }

        return this;
    }

    public Promise next(PromiseCallback callback) {
        if (mLastResolveObj != null) {
            return callback.invoke(mLastResolveObj);
        }

        if (callback != null) {
            mInvokeQueue.add(callback);
        }

        return this;
    }

    public void resolve(Object obj) {
        PromiseCallback callback = mInvokeQueue.poll();
        if (callback == null) {
            mLastResolveObj = obj;
            return;
        }
        Promise promise = callback.invoke(obj);
        if (promise == null) {
            promise = this;
        }
        promise.then(mInvokeQueue.poll());
    }
}
