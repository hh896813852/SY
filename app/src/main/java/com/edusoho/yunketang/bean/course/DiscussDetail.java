package com.edusoho.yunketang.bean.course;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DF on 2017/1/4.
 */
public class DiscussDetail implements Serializable {

    private int total;
    private List<ResourcesBean> resources;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ResourcesBean> getResources() {
        return resources;
    }

    public void setResources(List<ResourcesBean> resources) {
        this.resources = resources;
    }

    public static class ResourcesBean implements Serializable {

        private String id;
        private String courseId;
        private String lessonId;
        private String targetId;
        private String userId;
        private String type;
        private String isStick;
        private String sticky;
        private String nice;
        private String isElite;
        private String isClosed;
        @SerializedName("private")
        private String privateX;
        private String title;
        private String content;
        private String postNum;
        private String hitNum;
        private String followNum;
        private String latestPostUserId;
        private String latestPostTime;
        private String createdTime;
        private String updatedTime;
        private UserBean user;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCourseId() {
            return courseId;
        }

        public void setCourseId(String courseId) {
            this.courseId = courseId;
        }

        public String getLessonId() {
            return lessonId;
        }

        public void setLessonId(String lessonId) {
            this.lessonId = lessonId;
        }

        public String getTargetId() {
            return targetId;
        }

        public void setTargetId(String targetId){
            this.targetId = targetId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getIsStick() {
            return isStick != null ? isStick : sticky;
        }

        public void setIsStick(String isStick) {
            this.isStick = isStick;
        }

        public String getIsElite() {
            return isElite != null ? isElite : nice;
        }

        public void setIsElite(String isElite) {
            this.isElite = isElite;
        }

        public String getIsClosed() {
            return isClosed;
        }

        public void setIsClosed(String isClosed) {
            this.isClosed = isClosed;
        }

        public String getPrivateX() {
            return privateX;
        }

        public void setPrivateX(String privateX) {
            this.privateX = privateX;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPostNum() {
            return postNum;
        }

        public void setPostNum(String postNum) {
            this.postNum = postNum;
        }

        public String getHitNum() {
            return hitNum;
        }

        public void setHitNum(String hitNum) {
            this.hitNum = hitNum;
        }

        public String getFollowNum() {
            return followNum;
        }

        public void setFollowNum(String followNum) {
            this.followNum = followNum;
        }

        public String getLatestPostUserId() {
            return latestPostUserId;
        }

        public void setLatestPostUserId(String latestPostUserId) {
            this.latestPostUserId = latestPostUserId;
        }

        public String getLatestPostTime() {
            return latestPostTime;
        }

        public void setLatestPostTime(String latestPostTime) {
            this.latestPostTime = latestPostTime;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public String getUpdatedTime() {
            return updatedTime;
        }

        public void setUpdatedTime(String updatedTime) {
            this.updatedTime = updatedTime;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean implements Serializable{
            private String id;
            private String nickname;
            private String title;
            private String avatar;
            private List<String> roles;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public List<String> getRoles() {
                return roles;
            }

            public void setRoles(List<String> roles) {
                this.roles = roles;
            }
        }
    }
}
