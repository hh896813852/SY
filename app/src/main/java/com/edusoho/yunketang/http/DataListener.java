package com.edusoho.yunketang.http;

import com.edusoho.yunketang.bean.base.Message;

/**
 * Created by any on 17/6/14.
 */
public interface DataListener<T> {

    void onMessage(Message<T> message);
}
