package com.edusoho.yunketang.edu.bean;


import java.io.Serializable;

/**
 * 站内通知类型
 */
public class Notification implements Serializable {

    public String msgNo;
    public String title;
    public String content;
    public String type;
    public long createdTime;
}
