package com.edusoho.yunketang.edu.model;

/**
 * Created by JesseHuang on 15/4/23.
 */
public class MessageModel {
    public int what;
    public Object obj;
    public String arg;

    public MessageModel(Object obj) {
        this.obj = obj;
    }

    public MessageModel(int what) {
        this.what = what;
    }

    public MessageModel(int what, Object obj) {
        this(obj);
        this.what = what;
    }

    public MessageModel(String arg, Object obj) {
        this(obj);
        this.arg = arg;
    }
}
