package com.edusoho.yunketang.edu.base;

/**
 * Created by JesseHuang on 2017/3/22.
 */

public interface BaseView<T> {

    void showToast(int resId);

    void showToast(String msg);

    void close();
}
