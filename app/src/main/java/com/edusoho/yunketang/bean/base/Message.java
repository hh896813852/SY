package com.edusoho.yunketang.bean.base;

/**
 * Created by any on 17/6/14.
 */

public class Message<T> {
    public int retcode;
    public int status;
    public String msg;
    public T data;
}
