package com.edusoho.yunketang.edu.bean;


import java.io.Serializable;

/**
 * 网校公告类型
 */
public class Announcement implements Serializable {

    public int    id;
    public int    userId;
    public String targetType;
    public String url;
    public String startTime;
    public String endTime;
    public int    targetId;
    public String content;
    public String createdTime;
    public String updatedTime;

}
