package com.edusoho.yunketang.edu.base;

/**
 * Created by JesseHuang on 2017/3/22.
 */

public interface BasePresenter {

    /**
     * 初始化数据
     */
    void subscribe();

    /**
     * 保存数据，用于onPause()
     */
    void unsubscribe();
}
