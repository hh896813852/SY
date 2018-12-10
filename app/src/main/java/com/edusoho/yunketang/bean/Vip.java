package com.edusoho.yunketang.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by howzhi on 14-8-29.
 */
public class Vip implements Serializable {
    public int    seq;
    public int    userId;
    @SerializedName("levelId")
    public int    id;
    @SerializedName("vipName")
    public String name;
    @SerializedName("VipDeadLine")
    public String deadline;
    public String boughtType;
    public String boughtTime;
    public int    boughtDuration;
    public String boughtUnit;
    public double boughtAmount;
    public String createdTime;
}
