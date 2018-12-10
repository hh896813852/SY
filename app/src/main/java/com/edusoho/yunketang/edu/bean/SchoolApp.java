package com.edusoho.yunketang.edu.bean;

import java.io.Serializable;

/**
 * Created by Melomelon on 2015/7/6.
 */
public class SchoolApp extends Friend implements Serializable {

    public String code;
    public String name;
    public String about;
    public String avatar;
    public String callback;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return title;
    }

    @Override
    public String getNickname() {
        return name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    @Override
    public String getSmallAvatar() {
        return avatar;
    }

    @Override
    public String getMediumAvatar() {
        return avatar;
    }

    @Override
    public String getLargeAvatar() {
        return avatar;
    }

    @Override
    public String getType() {
        return code;
    }
}
