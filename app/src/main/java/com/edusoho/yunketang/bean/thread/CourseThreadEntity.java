package com.edusoho.yunketang.bean.thread;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JesseHuang on 15/12/23.
 */
public class CourseThreadEntity implements Serializable {
    public int id;
    public int courseId;
    public int lessonId;
    public int userId;
    /**
     * question
     * discuss
     */
    public String type;
    public int isStick;
    public int isElite;
    public int isClosed;
    @SerializedName("private")
    public String publicX;
    public String title;
    public String content;
    public String threadType;
    public int postNum;
    public int hitNum;
    public String followNum;
    public String latestPostUserId;
    public String latestPostTime;
    public String updatedTime;
    public String courseTitle;
    public String coursePicture;
    public boolean isTeacherPost;
    public String createdTime;
    public UserEntity user;

    public CourseThreadEntity() {
        if (user == null) {
            user = new UserEntity();
        }
    }

    public static class UserEntity {
        public int id;
        public String email;
        public String verifiedMobile;
        public String password;
        public String salt;
        public String payPassword;
        public String payPasswordSalt;
        public String uri;
        public String nickname;
        public String title;
        public String tags;
        public String type;
        public String point;
        public String coin;
        public String smallAvatar;
        public String mediumAvatar;
        public String largeAvatar;
        public String emailVerified;
        public String setup;
        public String promoted;
        public String promotedTime;
        public String locked;
        public String lastPasswordFailTime;
        public String lockDeadline;
        public String consecutivePasswordErrorTimes;
        public String loginTime;
        public String loginIp;
        public String loginSessionId;
        public String approvalTime;
        public String approvalStatus;
        public String newMessageNum;
        public String newNotificationNum;
        public String createdIp;
        public String createdTime;
        public String updatedTime;
        public Object inviteCode;
        public String following;
        public String follower;
        public List<String> roles;
    }
}
