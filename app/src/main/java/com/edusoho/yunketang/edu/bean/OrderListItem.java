package com.edusoho.yunketang.edu.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.edusoho.yunketang.bean.Cover;

import java.io.Serializable;

public class OrderListItem implements Serializable {
    private String id;
    private String title;
    private String sn;
    @SerializedName("pay_amount")
    private String payAmount;
    @SerializedName("status")
    private String state;
    @SerializedName("created_time")
    private long   createdTime;
    private Cover  cover;
    private String targetType;
    private String targetId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getPayAmount() {
        float price = Float.valueOf(payAmount);
        return String.format("%.2f", price / 100);
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getCreatedTime() {
        return createdTime * 1000L;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public Cover getCover() {
        return cover;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        if (TextUtils.isEmpty(targetId)) {
            return "0";
        }
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
