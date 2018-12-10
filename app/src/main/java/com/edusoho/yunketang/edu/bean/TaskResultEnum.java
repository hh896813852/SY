package com.edusoho.yunketang.edu.bean;

/**
 * Created by JesseHuang on 2017/4/27.
 */

public enum TaskResultEnum {
    FINISH("finish"), START("start");

    private String mName;

    TaskResultEnum(String name) {
        this.mName = name;
    }

    @Override
    public String toString() {
        return mName;
    }
}
