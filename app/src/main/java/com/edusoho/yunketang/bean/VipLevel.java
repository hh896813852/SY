package com.edusoho.yunketang.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by howzhi on 14-8-31.
 */
public class VipLevel implements Serializable {

    public int    id;
    public int    seq;
    public String name;
    public String icon;
    public String picture;

    @SerializedName("monthPriceConvert")
    public Price monthPrice;

    @SerializedName("yearPriceConvert")
    public Price  yearPrice;
    public String description;
    public int    freeLearned;
    public int    enabled;
    public String createdTime;
    public String maxRate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Price getMonthPrice() {
        return monthPrice;
    }

    public void setMonthPrice(Price monthPrice) {
        this.monthPrice = monthPrice;
    }

    public Price getYearPrice() {
        return yearPrice;
    }

    public void setYearPrice(Price yearPrice) {
        this.yearPrice = yearPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFreeLearned() {
        return freeLearned;
    }

    public void setFreeLearned(int freeLearned) {
        this.freeLearned = freeLearned;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getMaxRate() {
        return maxRate;
    }

    public void setMaxRate(String maxRate) {
        this.maxRate = maxRate;
    }
}
