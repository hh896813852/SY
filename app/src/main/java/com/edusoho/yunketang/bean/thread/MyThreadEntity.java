package com.edusoho.yunketang.bean.thread;

import com.edusoho.yunketang.bean.Course;

/**
 * Created by melomelon on 16/3/7.
 */
public class MyThreadEntity {
    private String id;
    private String threadId;
    private String courseId;
    private String courseTitle;
    private String title;
    private String content;
    private String type;
    private String isStick;
    private String isElite;
    private String isClosed;
    private Course course;
    private String postNum;
    private String hitNum;
    private String followNum;
    private String latestPostUserId;
    private String latestPostTime;
    private String createdTime;
    private String updatedTime;
    private String isPrivate;


    public String getId() {
        return id;
    }

    public String getThreadId() {
        return threadId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getType() {
        return type;
    }

    public String getIsStick() {
        return isStick;
    }

    public String getIsElite() {
        return isElite;
    }

    public String getIsClosed() {
        return isClosed;
    }

    public Course getCourse() {
        return course;
    }

    public String getPostNum() {
        return postNum;
    }

    public String getHitNum() {
        return hitNum;
    }

    public String getFollowNum() {
        return followNum;
    }

    public String getLatestPostUserId() {
        return latestPostUserId;
    }

    public String getLatestPostTime() {
        return latestPostTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }
}
