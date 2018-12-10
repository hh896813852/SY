package com.edusoho.yunketang.edu.bean;

import android.os.Bundle;

import com.edusoho.yunketang.edu.utils.InputUtils;

/**
 * Created by JesseHuang on 15/4/23.
 */
public class WidgetMessage {
    public MessageType type;
    public Bundle data;
    public Object target;
    public InputUtils.NormalCallback callback;

    public WidgetMessage(MessageType type, Bundle body) {
        this.type = type;
        this.data = body;
    }

    public WidgetMessage(
            MessageType type, Bundle body, InputUtils.NormalCallback callback) {
        this(type, body);
        this.callback = callback;
    }
}
