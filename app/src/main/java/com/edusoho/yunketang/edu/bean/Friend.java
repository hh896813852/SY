package com.edusoho.yunketang.edu.bean;

/**
 * Created by Melomelon on 2015/6/2.
 */
public class Friend {

    public int id;
    private String type = "user";
    public int avatarID;
    public String largeAvatar;
    public String mediumAvatar;
    public String smallAvatar;
    public String nickname;
    public String title;
    public String[] roles;
    public String friendship;
    public boolean isTeacher;

    public boolean isTop = false;
    public boolean isBottom = false;

    private String sortLetters;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAvatarID() {
        return avatarID;
    }

    public void setAvatarID(int avatarID) {
        this.avatarID = avatarID;
    }

    public String getLargeAvatar() {
        return largeAvatar;
    }

    public void setLargeAvatar(String largeAvatar) {
        this.largeAvatar = largeAvatar;
    }

    public String getMediumAvatar() {
        return mediumAvatar;
    }

    public void setMediumAvatar(String mediumAvatar) {
        this.mediumAvatar = mediumAvatar;
    }

    public String getSmallAvatar() {
        return smallAvatar;
    }

    public void setSmallAvatar(String smallAvatar) {
        this.smallAvatar = smallAvatar;
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

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public String getFriendship() {
        return friendship;
    }

    public void setFriendship(String friendship) {
        this.friendship = friendship;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean isTeacher) {
        this.isTeacher = isTeacher;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean isTop) {
        this.isTop = isTop;
    }

    public boolean isBottom() {
        return isBottom;
    }

    public void setBottom(boolean isBottom) {
        this.isBottom = isBottom;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
