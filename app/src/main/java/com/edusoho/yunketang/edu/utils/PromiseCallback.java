package com.edusoho.yunketang.edu.utils;

/**
 * Created by howzhi on 15/8/25.
 */
public interface PromiseCallback<T> {

    Promise invoke(T obj);
}
