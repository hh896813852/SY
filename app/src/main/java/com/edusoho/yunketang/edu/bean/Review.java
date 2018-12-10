package com.edusoho.yunketang.edu.bean;

import com.edusoho.yunketang.bean.User;

import java.io.Serializable;

/**
 * Created by JesseHuang on 2017/4/4.
 */

public class Review implements Serializable {
    public int id;
    public int courseId;
    public String createdTime;
    public String updatedTime;
    public float rating;
    public User user;
    public String content;
}
