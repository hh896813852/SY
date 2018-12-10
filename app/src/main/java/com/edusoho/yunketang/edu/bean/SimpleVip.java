package com.edusoho.yunketang.edu.bean;


import java.io.Serializable;

public class SimpleVip implements Serializable {
    private int    levelId;
    private String vipName;
    private int    seq;

    //2018-08-25T10:49:01+08:00
    private String deadline;
    //unix时间戳
    private String vipDeadline;

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public String getVipName() {
        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
