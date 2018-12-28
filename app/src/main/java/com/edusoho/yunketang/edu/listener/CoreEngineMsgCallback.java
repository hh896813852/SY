package com.edusoho.yunketang.edu.listener;


import com.edusoho.yunketang.edu.model.MessageModel;

/**
 * Created by JesseHuang on 15/4/23.
 */
public interface CoreEngineMsgCallback {
    void invoke(MessageModel obj);
}
