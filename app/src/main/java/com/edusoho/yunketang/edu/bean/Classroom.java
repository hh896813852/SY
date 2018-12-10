package com.edusoho.yunketang.edu.bean;

import com.edusoho.yunketang.bean.Cover;
import com.edusoho.yunketang.bean.Price;
import com.edusoho.yunketang.edu.bean.innerbean.Access;
import com.edusoho.yunketang.edu.bean.innerbean.Avatar;
import com.edusoho.yunketang.edu.bean.innerbean.Teacher;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DF on 2017/5/23.
 */

public class Classroom implements Serializable {

    public int id;
    public String title;
    public String about;
    public float price;
    public String studentNum;
    public Cover cover;
    public String status;
    public int vipLevelId;
    public String postNum;
    public String createdTime;
    public String updatedTime;
    public String rating;
    public String ratingNum;
    public String buyable;
    public String expiryMode;
    public String expiryValue;
    public CreatorBean creator;
    public Object headTeacher;
    public Access access;
    public List<ServiceBean> service;
    public List<Teacher> teachers;
    public Price price2;

    public static class CreatorBean {
        public String id;
        public String nickname;
        public String title;
        public Avatar avatar;
    }

    public static class ServiceBean {
        public String code;
        public String shortName;
        public String fullName;
        public String summary;
        public int active;
    }

}
