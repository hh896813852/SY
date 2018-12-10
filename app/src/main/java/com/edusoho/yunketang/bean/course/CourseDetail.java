package com.edusoho.yunketang.bean.course;

import com.edusoho.yunketang.bean.Course;
import com.edusoho.yunketang.bean.VipLevel;
import com.edusoho.yunketang.edu.bean.Member;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Zhang on 2016/12/13.
 */

public class CourseDetail implements Serializable {

    private Course course;
    private boolean userFavorited;
    private Member member;
    private Object discount;
    private List<VipLevel> vipLevels;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public boolean isUserFavorited() {
        return userFavorited;
    }

    public void setUserFavorited(boolean userFavorited) {
        this.userFavorited = userFavorited;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Object getDiscount() {
        return discount;
    }

    public void setDiscount(Object discount) {
        this.discount = discount;
    }

    public List<VipLevel> getVipLevels() {
        return vipLevels;
    }

    public void setVipLevels(List<VipLevel> vipLevels) {
        this.vipLevels = vipLevels;
    }

}
