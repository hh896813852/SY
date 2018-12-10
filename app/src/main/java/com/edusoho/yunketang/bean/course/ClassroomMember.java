package com.edusoho.yunketang.bean.course;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JesseHuang on 15/12/11.
 */
public class ClassroomMember implements Serializable {

    public UserEntity user;
    public String id;
    public String classroomId;
    public String userId;
    public String orderId;
    public String levelId;
    public String noteNum;
    public String threadNum;
    public String locked;
    public String remark;
    public String createdTime;
    public List<String> role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(String classroomId) {
        this.classroomId = classroomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

    public static class UserEntity {
        public int id;
        public String nickname;
        public String title;
        public String avatar;

        public String getAvatar() {
            int schemIndex = avatar.lastIndexOf("http://");
            if (schemIndex != -1) {
                return avatar.substring(schemIndex);
            }
            return avatar;
        }
    }

}
