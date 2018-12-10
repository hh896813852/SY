package com.edusoho.yunketang.bean;

public class M3U8DbModel {

    public int    id;
    public int    userId;
    public int    finish;
    public String host;
    public int    lessonId;
    public int    totalNum;
    public int    downloadNum;
    public String playList;
    public String type;

    @Override
    public String toString() {
        return "M3U8DbModle {" +
                "id=" + id +
                ", finish=" + finish +
                ", userId=" + userId +
                ", host='" + host + '\'' +
                ", lessonId=" + lessonId +
                ", totalNum=" + totalNum +
                ", downloadNum=" + downloadNum +
                ", playList='" + playList + '\'' +
                '}';
    }
}