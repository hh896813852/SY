package com.edusoho.yunketang.edu.bean;


import com.edusoho.yunketang.bean.School;
import com.edusoho.yunketang.bean.User;

import java.io.Serializable;

public class UserResult implements Serializable {
    public User user;
    public String token;
    public School site;

    public UserResult() {

    }

    public UserResult(User user, String token, School site) {
        this.user = user;
        this.token = token;
        this.site = site;
    }
}
