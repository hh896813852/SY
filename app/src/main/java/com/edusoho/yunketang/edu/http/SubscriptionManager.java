package com.edusoho.yunketang.edu.http;

import rx.Subscription;
import rx.internal.util.SubscriptionList;

/**
 * Created by JesseHuang on 2017/5/31.
 */

public class SubscriptionManager {

    private static SubscriptionList mSubscriptionList;

    static {
        mSubscriptionList = new SubscriptionList();
    }

    public static void add(Subscription subscription) {
        mSubscriptionList.add(subscription);
    }

    public static void remove(Subscription subscription) {
        mSubscriptionList.remove(subscription);
    }

    public static void unsubscribeAll() {
        //mSubscriptionList.unsubscribe();
    }
}
