package com.edusoho.yunketang.edu.bean;

/**
 * Created by JesseHuang on 2017/4/7.
 */

public enum CourseMemberRoleEnum {
    STUDENT("student"), TEACHER("teacher");

    private String mName;

    CourseMemberRoleEnum(String name) {
        this.mName = name;
    }

    @Override
    public String toString() {
        return mName;
    }
}
