package com.edusoho.yunketang.bean.thread;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JesseHuang on 15/12/23.
 */
public class CourseThreadPostEntity implements Serializable {

    public int pid;
    @SerializedName("id")
    public int postId;
    public int courseId;
    public int lessonId;
    public int threadId;
    public int isElite;
    public String content;
    public String headImgUrl;
    public int delivery = 2;
    public String type;
    public String createdTime;
    public String upyunMediaPutUrl;
    public String upyunMediaGetUrl;
    public HashMap<String, String> headers;
    public UserEntity user;

    public CourseThreadPostEntity() {
        if (user == null) {
            user = new UserEntity();
        }
    }

    public static class UserEntity {
        public int id;
        public String email;
        public String verifiedMobile;
        public String uri;
        public String nickname;
        public String title;
        public String type;
        public String smallAvatar;
        public String mediumAvatar;
        public String largeAvatar;
        public String emailVerified;
        public String setup;
        public String locked;
        public String lastPasswordFailTime;
        public String lockDeadline;
        public String consecutivePasswordErrorTimes;
        public String createdTime;
        public String updatedTime;
        public Object inviteCode;
        public Object signature;
        public String following;
        public String follower;
        public String mobile;
        public List<String> roles;

    }
}
