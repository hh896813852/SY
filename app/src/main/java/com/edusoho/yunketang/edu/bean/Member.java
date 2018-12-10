package com.edusoho.yunketang.edu.bean;

import com.edusoho.yunketang.bean.User;

import java.io.Serializable;

/**
 * Created by JesseHuang on 2017/4/1.
 */

public class Member implements Serializable {
    public int id;
    public String courseId;
    public String deadline;
    public User user;
}
